package mcq.quiz.app.data.remote

import mcq.quiz.app.data.remote.dto.ModuleDto
import mcq.quiz.app.data.remote.dto.QuestionDto
import retrofit2.http.GET
import retrofit2.http.Url

interface QuizApi {
    @GET("dr-samrat/ee986f16da9d8303c1acfd364ece22c5/raw")
    suspend fun getModules(): List<ModuleDto>

    @GET
    suspend fun getQuestions(@Url url: String): List<QuestionDto>

    companion object {
        const val BASE_URL = "https://gist.githubusercontent.com/"
    }
}