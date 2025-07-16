package mcq.quiz.app.domain.usecase

import mcq.quiz.app.domain.model.CategoryModule
import mcq.quiz.app.domain.repository.ModuleRepository
import javax.inject.Inject

class GetModulesUseCase @Inject constructor(
    private val moduleRepository: ModuleRepository
) {
    suspend operator fun invoke(): List<CategoryModule> {
        return moduleRepository.getModules()
    }
}