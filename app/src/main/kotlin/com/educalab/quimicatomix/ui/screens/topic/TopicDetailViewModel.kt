package com.educalab.quimicatomix.ui.screens.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.quimicatomix.AppContainer
import com.educalab.quimicatomix.data.local.entity.Attempt
import com.educalab.quimicatomix.data.local.entity.ChemicalTopic
import com.educalab.quimicatomix.data.local.entity.Experiment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ExperimentWithStatus(
    val experiment: Experiment,
    val bestStars: Int,
    val isCompleted: Boolean
)

data class TopicDetailUiState(
    val topic: ChemicalTopic? = null,
    val experiments: List<ExperimentWithStatus> = emptyList(),
    val isLoading: Boolean = true
)

class TopicDetailViewModel(
    private val container: AppContainer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val topicId: String = checkNotNull(savedStateHandle["topicId"])

    private val _uiState = MutableStateFlow(TopicDetailUiState())
    val uiState: StateFlow<TopicDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val topic = container.contentRepository.getTopic(topicId)
            combine(
                container.contentRepository.observeExperimentsByTopic(topicId),
                container.progressRepository.observeAttempts(container.currentUserId)
            ) { experiments, attempts ->
                val byExperiment = attempts.filter { it.experimentId != null }.groupBy { it.experimentId }
                val merged = experiments.map { exp ->
                    val expAttempts = byExperiment[exp.id].orEmpty()
                    ExperimentWithStatus(
                        experiment = exp,
                        bestStars = expAttempts.maxOfOrNull { it.starsEarned } ?: 0,
                        isCompleted = expAttempts.any { it.success }
                    )
                }
                TopicDetailUiState(topic = topic, experiments = merged, isLoading = false)
            }.collect { _uiState.value = it }
        }
    }
}
