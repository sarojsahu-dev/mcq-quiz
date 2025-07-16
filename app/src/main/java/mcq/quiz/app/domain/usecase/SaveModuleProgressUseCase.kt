package mcq.quiz.app.domain.usecase

import mcq.quiz.app.domain.repository.ModuleProgressRepository
import javax.inject.Inject

class SaveModuleProgressUseCase @Inject constructor(
    private val progressRepository: ModuleProgressRepository
) {
    suspend operator fun invoke(
        moduleId: String,
        correctAnswers: Int,
        totalQuestions: Int
    ) {
        progressRepository.updateModuleProgress(moduleId, correctAnswers, totalQuestions)
    }
}