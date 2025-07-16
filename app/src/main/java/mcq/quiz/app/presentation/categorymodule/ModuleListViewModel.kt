package mcq.quiz.app.presentation.categorymodule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mcq.quiz.app.domain.usecase.GetAllModuleProgressUseCase
import mcq.quiz.app.domain.usecase.GetModulesUseCase
import mcq.quiz.app.utils.NetworkStatus
import mcq.quiz.app.utils.NetworkStatusTracker
import javax.inject.Inject

@HiltViewModel
class ModuleListViewModel @Inject constructor(
    private val getModulesUseCase: GetModulesUseCase,
    private val getAllModuleProgressUseCase: GetAllModuleProgressUseCase,
    private val networkStatusTracker: NetworkStatusTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModuleListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadModules()
        observeNetwork()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collect { status ->
                if (status is NetworkStatus.Unavailable && _uiState.value.modules.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No internet connection"
                    )
                }
            }
        }
    }

    fun loadModules() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val modules = getModulesUseCase()
                val progressList = getAllModuleProgressUseCase()
                val progressMap = progressList.associateBy { it.moduleId }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    modules = modules,
                    moduleProgress = progressMap,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun refreshProgress() {
        viewModelScope.launch {
            try {
                val progressList = getAllModuleProgressUseCase()
                val progressMap = progressList.associateBy { it.moduleId }
                _uiState.value = _uiState.value.copy(moduleProgress = progressMap)
            } catch (e: Exception) {

            }
        }
    }
}