package mcq.quiz.app.domain.repository

import mcq.quiz.app.domain.model.Question

interface QuizRepository {
    suspend fun getQuestions(url: String): List<Question>
}