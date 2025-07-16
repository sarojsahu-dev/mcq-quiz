package mcq.quiz.app.presentation.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import mcq.quiz.app.domain.usecase.CalculateResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mcq.quiz.app.domain.model.Question
import mcq.quiz.app.domain.model.QuizResult
import mcq.quiz.app.domain.usecase.SaveModuleProgressUseCase
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val calculateResultsUseCase: CalculateResultsUseCase,
    private val saveModuleProgressUseCase: SaveModuleProgressUseCase
) : ViewModel() {

    fun calculateResults(questions: List<Question>): QuizResult {
        return calculateResultsUseCase(questions)
    }

    fun saveProgress(moduleId: String, correctAnswers: Int, totalQuestions: Int) {
        viewModelScope.launch {
            try {
                saveModuleProgressUseCase(moduleId, correctAnswers, totalQuestions)
            } catch (e: Exception) {
            }
        }
    }
}