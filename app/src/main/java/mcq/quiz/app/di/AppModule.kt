package mcq.quiz.app.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mcq.quiz.app.data.remote.QuizApi
import mcq.quiz.app.data.repository.ModuleProgressRepositoryImpl
import mcq.quiz.app.data.repository.ModuleRepositoryImpl
import mcq.quiz.app.data.repository.QuizRepositoryImpl
import mcq.quiz.app.domain.repository.ModuleProgressRepository
import mcq.quiz.app.domain.repository.ModuleRepository
import mcq.quiz.app.domain.repository.QuizRepository
import mcq.quiz.app.utils.NetworkStatusTracker
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(QuizApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideQuizApi(retrofit: Retrofit): QuizApi {
        return retrofit.create(QuizApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideModuleRepository(api: QuizApi): ModuleRepository {
        return ModuleRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideModuleProgressRepository(
        @ApplicationContext context: Context,
        gson: Gson
    ): ModuleProgressRepository {
        return ModuleProgressRepositoryImpl(context, gson)
    }

    @Provides
    @Singleton
    fun provideQuizRepository(api: QuizApi): QuizRepository {
        return QuizRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideNetworkStatusTracker(@ApplicationContext context: Context): NetworkStatusTracker {
        return NetworkStatusTracker(context)
    }
}