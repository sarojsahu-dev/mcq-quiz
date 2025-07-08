package mcq.quiz.app.domain.usecase

import mcq.quiz.app.domain.model.Question
import mcq.quiz.app.domain.repository.QuizRepository
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    suspend operator fun invoke(): List<Question> {
        return repository.getQuestions()
    }
}