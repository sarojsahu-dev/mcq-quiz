package mcq.quiz.app.presentation.results

import androidx.lifecycle.ViewModel
import mcq.quiz.app.domain.usecase.CalculateResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import mcq.quiz.app.domain.model.Question
import mcq.quiz.app.domain.model.QuizResult
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val calculateResultsUseCase: CalculateResultsUseCase
) : ViewModel() {

    fun calculateResults(questions: List<Question>): QuizResult {
        return calculateResultsUseCase(questions)
    }
}