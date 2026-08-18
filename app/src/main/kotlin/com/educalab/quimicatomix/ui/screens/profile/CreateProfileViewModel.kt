package com.educalab.quimicatomix.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.quimicatomix.AppContainer
import com.educalab.quimicatomix.data.seed.DatabaseSeeder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateProfileUiState(
    val alias: String = "",
    val avatarId: Int = 0,
    val isCreating: Boolean = false,
    val created: Boolean = false
)

/** Crea un perfil adicional (progreso propio desde cero) sin afectar los perfiles existentes. */
class CreateProfileViewModel(private val container: AppContainer) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProfileUiState())
    val uiState: StateFlow<CreateProfileUiState> = _uiState.asStateFlow()

    fun updateAlias(alias: String) {
        _uiState.update { it.copy(alias = alias.take(18)) }
    }

    fun selectAvatar(avatarId: Int) {
        _uiState.update { it.copy(avatarId = avatarId) }
    }

    fun createProfile() {
        if (_uiState.value.isCreating) return
        _uiState.update { it.copy(isCreating = true) }
        viewModelScope.launch {
            val alias = _uiState.value.alias.ifBlank { "Explorador" }
            val userId = container.profileRepository.createProfile(alias, _uiState.value.avatarId)
            DatabaseSeeder.initializeProgressForUser(container.database, userId)
            container.switchProfile(userId)
            _uiState.update { it.copy(isCreating = false, created = true) }
        }
    }
}
