package mcq.quiz.app.data.repository

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import mcq.quiz.app.domain.model.ModuleProgress
import mcq.quiz.app.domain.repository.ModuleProgressRepository
import mcq.quiz.app.utils.ModuleStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleProgressRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : ModuleProgressRepository {

    private val prefs = context.getSharedPreferences("module_progress", Context.MODE_PRIVATE)

    override suspend fun getModuleProgress(moduleId: String): ModuleProgress? {
        val json = prefs.getString(moduleId, null) ?: return null
        return try {
            gson.fromJson(json, ModuleProgress::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllModuleProgress(): List<ModuleProgress> {
        return prefs.all.mapNotNull { (_, value) ->
            try {
                gson.fromJson(value as String, ModuleProgress::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun saveModuleProgress(progress: ModuleProgress) {
        val json = gson.toJson(progress)
        prefs.edit().putString(progress.moduleId, json).apply()
    }

    override suspend fun updateModuleProgress(
        moduleId: String,
        correctAnswers: Int,
        totalQuestions: Int
    ) {
        val progress = ModuleProgress(
            moduleId = moduleId,
            status = ModuleStatus.COMPLETED,
            totalQuestions = totalQuestions,
            correctAnswers = correctAnswers,
            completedAt = System.currentTimeMillis()
        )
        saveModuleProgress(progress)
    }


}