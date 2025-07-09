package mcq.quiz.app.presentation.quiz

import mcq.quiz.app.domain.model.Question

data class QuizUiState(
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: Int? = null,
    val showAnswer: Boolean = false,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val isQuizCompleted: Boolean = false,
    val error: String? = null,
    val isAnswerProcessing: Boolean = false,

) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    val progress: Float
        get() = if (questions.isNotEmpty()) (currentQuestionIndex + 1).toFloat() / questions.size else 0f

    val isStreakActive: Boolean
        get() = currentStreak >= 3

    val questionNumber: String
        get() = "${currentQuestionIndex + 1} of ${questions.size}"

    val showRetry: Boolean
        get() = !isLoading && error == "No internet connection"

}
