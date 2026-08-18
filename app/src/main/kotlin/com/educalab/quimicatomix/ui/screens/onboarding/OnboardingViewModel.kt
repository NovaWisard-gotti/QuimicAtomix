package com.educalab.quimicatomix.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.quimicatomix.AppContainer
import com.educalab.quimicatomix.data.seed.DatabaseSeeder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val pageIndex: Int = 0,
    val alias: String = "",
    val avatarId: Int = 0,
    val isCreatingProfile: Boolean = false,
    val finished: Boolean = false
)

/** Onboarding de máximo 4 pantallas: propósito, mundo/Quimi, cómo avanzar, y alias+avatar. */
class OnboardingViewModel(private val container: AppContainer) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    val totalPages = 4

    fun nextPage() {
        _uiState.update { it.copy(pageIndex = (it.pageIndex + 1).coerceAtMost(totalPages - 1)) }
    }

    fun previousPage() {
        _uiState.update { it.copy(pageIndex = (it.pageIndex - 1).coerceAtLeast(0)) }
    }

    fun updateAlias(alias: String) {
        _uiState.update { it.copy(alias = alias.take(18)) }
    }

    fun selectAvatar(avatarId: Int) {
        _uiState.update { it.copy(avatarId = avatarId) }
    }

    fun finishOnboarding() {
        if (_uiState.value.isCreatingProfile) return
        _uiState.update { it.copy(isCreatingProfile = true) }
        viewModelScope.launch {
            val alias = _uiState.value.alias.ifBlank { "Explorador" }
            val userId = container.profileRepository.createProfile(alias, _uiState.value.avatarId)
            DatabaseSeeder.initializeProgressForUser(container.database, userId)
            container.switchProfile(userId)
            _uiState.update { it.copy(isCreatingProfile = false, finished = true) }
        }
    }
}
