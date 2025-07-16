package mcq.quiz.app.data.repository

import mcq.quiz.app.data.remote.QuizApi
import mcq.quiz.app.data.remote.dto.toDomain
import mcq.quiz.app.domain.model.CategoryModule
import mcq.quiz.app.domain.model.Question
import mcq.quiz.app.domain.repository.ModuleRepository
import javax.inject.Inject

class ModuleRepositoryImpl @Inject constructor(
    private val api: QuizApi
) : ModuleRepository {

    override suspend fun getModules(): List<CategoryModule> {
        return api.getModules().map { it.toDomain() }
    }

    override suspend fun getModuleQuestions(questionsUrl: String): List<Question> {
        return api.getQuestions(questionsUrl).map { it.toDomain() }
    }
}