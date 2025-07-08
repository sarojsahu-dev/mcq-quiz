package mcq.quiz.app.domain.usecase

import mcq.quiz.app.domain.model.Question
import mcq.quiz.app.domain.model.QuizResult
import javax.inject.Inject

class CalculateResultsUseCase @Inject constructor() {

    operator fun invoke(questions: List<Question>): QuizResult {
        val correctAnswers = questions.count {
            it.userAnswer == it.correctOptionIndex
        }
        val skippedQuestions = questions.count { it.isSkipped }
        val longestStreak = calculateLongestStreak(questions)

        return QuizResult(
            totalQuestions = questions.size,
            correctAnswers = correctAnswers,
            skippedQuestions = skippedQuestions,
            longestStreak = longestStreak
        )
    }

    private fun calculateLongestStreak(questions: List<Question>): Int {
        var longestStreak = 0
        var currentStreak = 0

        questions.forEach { question ->
            if (question.userAnswer == question.correctOptionIndex) {
                currentStreak++
                longestStreak = maxOf(longestStreak, currentStreak)
            } else {
                currentStreak = 0
            }
        }

        return longestStreak
    }
}