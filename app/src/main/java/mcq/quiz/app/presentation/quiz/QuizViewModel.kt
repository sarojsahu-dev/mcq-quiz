package mcq.quiz.app.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mcq.quiz.app.domain.usecase.GetModuleQuestionsUseCase
import mcq.quiz.app.domain.usecase.GetQuestionsUseCase
import mcq.quiz.app.domain.usecase.SaveModuleProgressUseCase
import mcq.quiz.app.utils.NetworkStatus
import mcq.quiz.app.utils.NetworkStatusTracker
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val getModuleQuestionsUseCase: GetModuleQuestionsUseCase,
    private val saveModuleProgressUseCase: SaveModuleProgressUseCase,
    private val networkStatusTracker: NetworkStatusTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState = _uiState.asStateFlow()

    private var currentModuleId: String? = null

    init {
        observeNetwork()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collect { status ->
                if (status is NetworkStatus.Unavailable && _uiState.value.questions.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No internet connection"
                    )
                }
            }
        }
    }

    fun loadModuleQuestions(questionsUrl: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val questions = getModuleQuestionsUseCase(questionsUrl)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    questions = questions,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun setModuleId(moduleId: String) {
        currentModuleId = moduleId
    }

    fun retry() {
        if (_uiState.value.error == "No internet connection") {
            _uiState.value = _uiState.value.copy(error = null)
        }
    }

    fun selectAnswer(answerIndex: Int) {
        val currentState = _uiState.value
        if (currentState.showAnswer || currentState.isAnswerProcessing) return

        val currentQuestion = currentState.currentQuestion ?: return

        _uiState.value = currentState.copy(
            selectedAnswer = answerIndex,
            isAnswerProcessing = true
        )

        viewModelScope.launch {
            delay(500)

            val isCorrect = answerIndex == currentQuestion.correctOptionIndex
            val newStreak = if (isCorrect) currentState.currentStreak + 1 else 0
            val newLongestStreak = maxOf(currentState.longestStreak, newStreak)

            val updatedQuestions = currentState.questions.toMutableList()
            updatedQuestions[currentState.currentQuestionIndex] = currentQuestion.copy(
                userAnswer = answerIndex
            )

            _uiState.value = currentState.copy(
                selectedAnswer = answerIndex,
                showAnswer = true,
                currentStreak = newStreak,
                longestStreak = newLongestStreak,
                questions = updatedQuestions,
                isAnswerProcessing = false
            )

            delay(1500)
            nextQuestion()
        }
    }

    fun skipQuestion() {
        val currentState = _uiState.value
        if (currentState.showAnswer || currentState.isAnswerProcessing) return

        val currentQuestion = currentState.currentQuestion ?: return

        val updatedQuestions = currentState.questions.toMutableList()
        updatedQuestions[currentState.currentQuestionIndex] = currentQuestion.copy(
            isSkipped = true
        )

        _uiState.value = currentState.copy(
            questions = updatedQuestions,
            currentStreak = 0 // Reset streak on skip
        )

        nextQuestion()
    }

    private fun nextQuestion() {
        val currentState = _uiState.value
        val nextIndex = currentState.currentQuestionIndex + 1

        if (nextIndex >= currentState.questions.size) {
            currentModuleId?.let { moduleId ->
                viewModelScope.launch {
                    val correctAnswers = currentState.questions.count {
                        it.userAnswer == it.correctOptionIndex
                    }
                    saveModuleProgressUseCase(moduleId, correctAnswers, currentState.questions.size)
                }
            }

            _uiState.value = currentState.copy(
                isQuizCompleted = true
            )
        } else {
            _uiState.value = currentState.copy(
                currentQuestionIndex = nextIndex,
                selectedAnswer = null,
                showAnswer = false,
                isAnswerProcessing = false
            )
        }
    }

    fun restartQuiz() {
        val currentState = _uiState.value
        val resetQuestions = currentState.questions.map { question ->
            question.copy(userAnswer = null, isSkipped = false)
        }

        _uiState.value = QuizUiState(
            questions = resetQuestions
        )
    }
}