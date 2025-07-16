package mcq.quiz.app.data.remote.dto

import androidx.annotation.Keep
import mcq.quiz.app.domain.model.CategoryModule

@Keep
data class ModuleDto(
    val id: String,
    val title: String,
    val description: String,
    val questions_url: String
)

fun ModuleDto.toDomain(): CategoryModule {
    return CategoryModule(
        id = id,
        title = title,
        description = description,
        questionsUrl = questions_url
    )
}