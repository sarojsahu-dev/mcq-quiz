package mcq.quiz.app.domain.usecase

import mcq.quiz.app.domain.model.Question
import mcq.quiz.app.domain.repository.ModuleRepository
import javax.inject.Inject

class GetModuleQuestionsUseCase @Inject constructor(
    private val moduleRepository: ModuleRepository
) {
    suspend operator fun invoke(questionsUrl: String): List<Question> {
        return moduleRepository.getModuleQuestions(questionsUrl)
    }
}