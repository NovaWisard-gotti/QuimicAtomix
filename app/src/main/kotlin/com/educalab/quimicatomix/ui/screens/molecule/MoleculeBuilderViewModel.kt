package com.educalab.quimicatomix.ui.screens.molecule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.quimicatomix.AppContainer
import com.educalab.quimicatomix.data.local.entity.Atom
import com.educalab.quimicatomix.data.local.entity.MoleculeChallenge
import com.educalab.quimicatomix.domain.logic.MoleculeBuilderEngine
import com.educalab.quimicatomix.domain.logic.XpEngine
import com.educalab.quimicatomix.domain.model.MoleculeBuildOutcome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MoleculeHubUiState(
    val challenges: List<MoleculeChallenge> = emptyList(),
    val builtIds: Set<String> = emptySet(),
    val playerLevel: Int = 1
)

data class MoleculeBuilderUiState(
    val challenge: MoleculeChallenge? = null,
    val palette: List<Atom> = emptyList(),
    val builtAtoms: Map<String, Int> = emptyMap(),
    val outcome: MoleculeBuildOutcome? = null,
    val starsEarned: Int = 0,
    val xpEarned: Int = 0,
    val attemptsCount: Int = 0
)

/** Maneja tanto el hub (lista de retos) como la construcción activa de un reto concreto. */
class MoleculeBuilderViewModel(
    private val container: AppContainer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val moleculeId: String? = savedStateHandle["moleculeId"]
    private val startedAt = System.currentTimeMillis()

    private val _hubState = MutableStateFlow(MoleculeHubUiState())
    val hubState: StateFlow<MoleculeHubUiState> = _hubState.asStateFlow()

    private val _builderState = MutableStateFlow(MoleculeBuilderUiState())
    val builderState: StateFlow<MoleculeBuilderUiState> = _builderState.asStateFlow()

    init {
        viewModelScope.launch {
            kotlinx.coroutines.flow.combine(
                container.contentRepository.observeMolecules(),
                container.progressRepository.observeAttempts(container.currentUserId),
                container.profileRepository.observeProfile(container.currentUserId)
            ) { challenges, attempts, profile ->
                val successfulIds = attempts
                    .filter { it.success && it.moleculeChallengeId != null }
                    .mapNotNull { it.moleculeChallengeId }
                    .toSet()
                MoleculeHubUiState(challenges = challenges, builtIds = successfulIds, playerLevel = profile?.level ?: 1)
            }.collect { _hubState.value = it }
        }
        if (moleculeId != null) {
            viewModelScope.launch {
                val challenge = container.contentRepository.getMolecule(moleculeId)
                val palette = container.contentRepository.getAtoms()
                _builderState.value = MoleculeBuilderUiState(challenge = challenge, palette = palette)
            }
        }
    }

    fun addAtom(symbol: String) {
        val current = _builderState.value.builtAtoms
        _builderState.value = _builderState.value.copy(
            builtAtoms = current + (symbol to (current[symbol] ?: 0) + 1),
            outcome = null
        )
    }

    fun removeAtom(symbol: String) {
        val current = _builderState.value.builtAtoms
        val count = (current[symbol] ?: 0) - 1
        _builderState.value = _builderState.value.copy(
            builtAtoms = if (count <= 0) current - symbol else current + (symbol to count),
            outcome = null
        )
    }

    fun resetBuild() {
        _builderState.value = _builderState.value.copy(builtAtoms = emptyMap(), outcome = null)
    }

    fun checkMolecule() {
        val state = _builderState.value
        val challenge = state.challenge ?: return
        val outcome = MoleculeBuilderEngine.evaluate(challenge.compositionCsv, state.builtAtoms)
        val newAttempts = state.attemptsCount + 1
        _builderState.value = state.copy(outcome = outcome, attemptsCount = newAttempts)

        if (outcome.isComplete) {
            val stars = when {
                newAttempts <= 1 -> 3
                newAttempts == 2 -> 2
                else -> 1
            }
            val xp = XpEngine.xpForStars(challenge.xpReward, stars)
            _builderState.value = _builderState.value.copy(starsEarned = stars, xpEarned = xp)
            viewModelScope.launch {
                val userId = container.currentUserId
                container.progressRepository.registerMoleculeAttempt(
                    userId = userId,
                    moleculeChallengeId = challenge.id,
                    success = true,
                    stars = stars,
                    xpEarned = xp,
                    mistakes = newAttempts - 1,
                    startedAt = startedAt,
                    finishedAt = System.currentTimeMillis()
                )
                container.profileRepository.awardXp(userId, xp)
                val stats = container.gamificationRepository.buildPlayerStats(userId)
                val allBadges = container.database.badgeDao().getAll()
                val allEquipment = container.database.labEquipmentDao().getAll()
                container.gamificationRepository.evaluateAndAwardUnlocks(userId, stats, allBadges, allEquipment)
            }
        }
    }
}
