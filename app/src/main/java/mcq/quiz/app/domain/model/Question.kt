package mcq.quiz.app.domain.model

import androidx.annotation.Keep

@Keep
data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val userAnswer: Int? = null,
    val isSkipped: Boolean = false
)
