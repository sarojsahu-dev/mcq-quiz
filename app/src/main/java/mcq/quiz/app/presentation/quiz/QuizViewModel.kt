package mcq.quiz.app.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mcq.quiz.app.domain.usecase.CalculateResultsUseCase
import mcq.quiz.app.domain.usecase.GetQuestionsUseCase
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val calculateResultsUseCase: CalculateResultsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val questions = getQuestionsUseCase()
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

    fun selectAnswer(answerIndex: Int) {
        val currentState = _uiState.value
        if (currentState.showAnswer || currentState.isAnswerProcessing) return

        val currentQuestion = currentState.currentQuestion ?: return

        // Start processing
        _uiState.value = currentState.copy(
            selectedAnswer = answerIndex,
            isAnswerProcessing = true
        )

        viewModelScope.launch {
            delay(500)

            val isCorrect = answerIndex == currentQuestion.correctOptionIndex
            val newStreak = if (isCorrect) currentState.currentStreak + 1 else 0
            val newLongestStreak = maxOf(currentState.longestStreak, newStreak)

            // Update the question with user answer
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

            // Auto advance after showing feedback
            delay(1500)
            nextQuestion()
        }
    }

    fun skipQuestion() {
        val currentState = _uiState.value
        if (currentState.showAnswer || currentState.isAnswerProcessing) return

        val currentQuestion = currentState.currentQuestion ?: return

        // Update the question as skipped
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
            // Quiz completed
            _uiState.value = currentState.copy(
                isQuizCompleted = true
            )
        } else {
            // Move to next question
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

    fun getQuizResult() = calculateResultsUseCase(_uiState.value.questions)
}