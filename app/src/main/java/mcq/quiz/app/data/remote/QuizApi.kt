package mcq.quiz.app.data.remote

import mcq.quiz.app.data.remote.dto.QuestionDto
import retrofit2.http.GET

interface QuizApi {
    @GET("dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw/")
    suspend fun getQuestions(): List<QuestionDto>

    companion object {
        const val BASE_URL = "https://gist.githubusercontent.com/"
    }
}