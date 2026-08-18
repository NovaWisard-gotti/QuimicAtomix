package com.educalab.quimicatomix.ui.screens.safety

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.quimicatomix.AppContainer
import com.educalab.quimicatomix.data.local.entity.SafetyScenario
import com.educalab.quimicatomix.domain.logic.XpEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class SafetyHubUiState(
    val scenarios: List<SafetyScenario> = emptyList(),
    val completedIds: Set<String> = emptySet()
)

data class SafetyPlayUiState(
    val scenario: SafetyScenario? = null,
    val options: List<String> = emptyList(),
    val answered: Boolean = false,
    val wasCorrect: Boolean = false
)

private const val SAFETY_XP = 15

class SafetyViewModel(
    private val container: AppContainer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val scenarioId: String? = savedStateHandle["scenarioId"]
    private val startedAt = System.currentTimeMillis()

    private val _hubState = MutableStateFlow(SafetyHubUiState())
    val hubState: StateFlow<SafetyHubUiState> = _hubState.asStateFlow()

    private val _playState = MutableStateFlow(SafetyPlayUiState())
    val playState: StateFlow<SafetyPlayUiState> = _playState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                container.contentRepository.observeSafetyScenarios(),
                container.progressRepository.observeAttempts(container.currentUserId)
            ) { scenarios, attempts ->
                val completed = attempts.filter { it.success && it.safetyScenarioId != null }
                    .mapNotNull { it.safetyScenarioId }.toSet()
                SafetyHubUiState(scenarios = scenarios, completedIds = completed)
            }.collect { _hubState.value = it }
        }
        if (scenarioId != null) {
            viewModelScope.launch {
                val scenario = container.contentRepository.getSafetyScenarios().firstOrNull { it.id == scenarioId }
                val options = scenario?.let {
                    (listOf(it.correctActionText) + it.distractorActionCsv.split("|")).shuffled()
                }.orEmpty()
                _playState.value = SafetyPlayUiState(scenario = scenario, options = options)
            }
        }
    }

    fun answer(selected: String) {
        val scenario = _playState.value.scenario ?: return
        val correct = selected == scenario.correctActionText
        _playState.value = _playState.value.copy(answered = true, wasCorrect = correct)
        viewModelScope.launch {
            val userId = container.currentUserId
            val xp = if (correct) SAFETY_XP else 0
            container.progressRepository.registerSafetyAttempt(
                userId = userId,
                safetyScenarioId = scenario.id,
                success = correct,
                xpEarned = xp,
                startedAt = startedAt,
                finishedAt = System.currentTimeMillis()
            )
            if (correct) {
                container.profileRepository.awardXp(userId, xp)
                val stats = container.gamificationRepository.buildPlayerStats(userId)
                val allBadges = container.database.badgeDao().getAll()
                val allEquipment = container.database.labEquipmentDao().getAll()
                container.gamificationRepository.evaluateAndAwardUnlocks(userId, stats, allBadges, allEquipment)
            }
        }
    }

    fun retry() {
        _playState.value = _playState.value.copy(answered = false, wasCorrect = false)
    }
}
