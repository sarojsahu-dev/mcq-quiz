package mcq.quiz.app.data.repository

import mcq.quiz.app.data.remote.QuizApi
import mcq.quiz.app.data.remote.dto.toDomain
import mcq.quiz.app.domain.model.Question
import mcq.quiz.app.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val api: QuizApi
) : QuizRepository {

    override suspend fun getQuestions(): List<Question> {
        return api.getQuestions().map { it.toDomain() }
    }
}