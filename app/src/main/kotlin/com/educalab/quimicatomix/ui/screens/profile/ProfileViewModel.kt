package com.educalab.quimicatomix.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.quimicatomix.AppContainer
import com.educalab.quimicatomix.data.local.entity.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(private val container: AppContainer) : ViewModel() {

    val profile: StateFlow<UserProfile?> = container.profileRepository
        .observeProfile(container.currentUserId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch { container.profileRepository.setSoundEnabled(container.currentUserId, enabled) }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch { container.profileRepository.setHapticsEnabled(container.currentUserId, enabled) }
    }
}
