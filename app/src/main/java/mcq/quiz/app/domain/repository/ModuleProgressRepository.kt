package mcq.quiz.app.domain.repository

import mcq.quiz.app.domain.model.ModuleProgress

interface ModuleProgressRepository {
    suspend fun getModuleProgress(moduleId: String): ModuleProgress?
    suspend fun getAllModuleProgress(): List<ModuleProgress>
    suspend fun saveModuleProgress(progress: ModuleProgress)
    suspend fun updateModuleProgress(moduleId: String, correctAnswers: Int, totalQuestions: Int)
}