package mcq.quiz.app.domain.model

import androidx.annotation.Keep

@Keep
data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val skippedQuestions: Int,
    val longestStreak: Int,
    val accuracy: Float = if (totalQuestions > 0) (correctAnswers.toFloat() / totalQuestions) * 100 else 0f
)
