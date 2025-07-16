package mcq.quiz.app.domain.repository

import mcq.quiz.app.domain.model.CategoryModule
import mcq.quiz.app.domain.model.Question

interface ModuleRepository {
    suspend fun getModules(): List<CategoryModule>
    suspend fun getModuleQuestions(questionsUrl: String): List<Question>
}