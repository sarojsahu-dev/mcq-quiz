package mcq.quiz.app.data.remote.dto

import mcq.quiz.app.domain.model.Question

data class QuestionDto(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int
)

fun QuestionDto.toDomain(): Question {
    return Question(
        id = id,
        question = question,
        options = options,
        correctOptionIndex = correctOptionIndex
    )
}