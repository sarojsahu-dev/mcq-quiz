package mcq.quiz.app.presentation.categorymodule

import mcq.quiz.app.domain.model.CategoryModule
import mcq.quiz.app.domain.model.ModuleProgress

data class ModuleListUiState(
    val isLoading: Boolean = false,
    val modules: List<CategoryModule> = emptyList(),
    val moduleProgress: Map<String, ModuleProgress> = emptyMap(),
    val error: String? = null
)
