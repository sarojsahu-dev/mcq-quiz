package mcq.quiz.app.domain.usecase

import mcq.quiz.app.domain.model.ModuleProgress
import mcq.quiz.app.domain.repository.ModuleProgressRepository
import javax.inject.Inject

class GetModuleProgressUseCase @Inject constructor(
    private val progressRepository: ModuleProgressRepository
) {
    suspend operator fun invoke(moduleId: String): ModuleProgress? {
        return progressRepository.getModuleProgress(moduleId)
    }
}