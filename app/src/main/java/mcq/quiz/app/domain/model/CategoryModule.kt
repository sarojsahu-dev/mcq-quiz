package mcq.quiz.app.domain.model

import androidx.annotation.Keep
import mcq.quiz.app.utils.ModuleStatus

@Keep
data class CategoryModule(
    val id: String,
    val title: String,
    val description: String,
    val questionsUrl: String
)

@Keep
data class ModuleProgress(
    val moduleId: String,
    val status: ModuleStatus,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val completedAt: Long? = null
) {
    val accuracy: Float
        get() = if (totalQuestions > 0) (correctAnswers.toFloat() / totalQuestions) * 100 else 0f
}
