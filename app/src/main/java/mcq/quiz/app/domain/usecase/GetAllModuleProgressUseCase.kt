package mcq.quiz.app.domain.usecase

import mcq.quiz.app.domain.model.ModuleProgress
import mcq.quiz.app.domain.repository.ModuleProgressRepository
import javax.inject.Inject

class GetAllModuleProgressUseCase @Inject constructor(
    private val progressRepository: ModuleProgressRepository
) {
    suspend operator fun invoke(): List<ModuleProgress> {
        return progressRepository.getAllModuleProgress()
    }
}