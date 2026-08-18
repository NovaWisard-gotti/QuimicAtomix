package com.educalab.quimicatomix.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.quimicatomix.AppContainer
import com.educalab.quimicatomix.data.local.entity.ChemicalTopic
import com.educalab.quimicatomix.data.local.entity.Progress
import com.educalab.quimicatomix.data.local.entity.UserProfile
import com.educalab.quimicatomix.domain.logic.XpEngine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class TopicWithProgress(val topic: ChemicalTopic, val progress: Progress?)

data class HomeUiState(
    val profile: UserProfile? = null,
    val topics: List<TopicWithProgress> = emptyList(),
    val progressWithinLevel: Float = 0f,
    val isLoading: Boolean = true
) {
    /** Sugerencia de próxima actividad: el primer tema disponible/iniciado con experimentos pendientes. */
    val suggestedTopic: TopicWithProgress?
        get() = topics.firstOrNull {
            val p = it.progress ?: return@firstOrNull false
            (p.mastery == com.educalab.quimicatomix.data.local.entity.MasteryState.DISPONIBLE ||
                p.mastery == com.educalab.quimicatomix.data.local.entity.MasteryState.INICIADO)
        }
}

class HomeViewModel(private val container: AppContainer) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        container.profileRepository.observeProfile(container.currentUserId),
        container.contentRepository.observeTopics(),
        container.progressRepository.observeProgress(container.currentUserId)
    ) { profile, topics, progressList ->
        val merged = topics.map { topic ->
            TopicWithProgress(topic, progressList.firstOrNull { it.topicId == topic.id })
        }
        HomeUiState(
            profile = profile,
            topics = merged,
            progressWithinLevel = XpEngine.progressWithinLevel(profile?.totalXp ?: 0),
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())
}
