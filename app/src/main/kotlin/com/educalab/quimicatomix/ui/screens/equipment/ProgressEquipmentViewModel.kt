package com.educalab.quimicatomix.ui.screens.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.quimicatomix.AppContainer
import com.educalab.quimicatomix.data.local.entity.Badge
import com.educalab.quimicatomix.data.local.entity.LabEquipment
import com.educalab.quimicatomix.data.local.entity.Progress
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ProgressEquipmentUiState(
    val badges: List<Badge> = emptyList(),
    val earnedBadgeIds: Set<String> = emptySet(),
    val equipment: List<LabEquipment> = emptyList(),
    val unlockedEquipmentIds: Set<String> = emptySet(),
    val progress: List<Progress> = emptyList(),
    val totalStars: Int = 0,
    val totalExperimentsCompleted: Int = 0
)

class ProgressEquipmentViewModel(private val container: AppContainer) : ViewModel() {

    val uiState: StateFlow<ProgressEquipmentUiState> = combine(
        container.gamificationRepository.observeBadges(),
        container.gamificationRepository.observeEarnedBadgeIds(container.currentUserId),
        container.gamificationRepository.observeEquipment(),
        container.gamificationRepository.observeUnlockedEquipmentIds(container.currentUserId),
        container.progressRepository.observeProgress(container.currentUserId)
    ) { badges, earned, equipment, unlocked, progress ->
        ProgressEquipmentUiState(
            badges = badges,
            earnedBadgeIds = earned.toSet(),
            equipment = equipment,
            unlockedEquipmentIds = unlocked.toSet(),
            progress = progress,
            totalStars = progress.sumOf { it.starsTotal },
            totalExperimentsCompleted = progress.sumOf { it.experimentsCompleted }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProgressEquipmentUiState())
}
