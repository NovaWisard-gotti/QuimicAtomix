package com.educalab.quimicatomix.ui.screens.experiment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.quimicatomix.AppContainer
import com.educalab.quimicatomix.data.local.entity.Experiment
import com.educalab.quimicatomix.data.local.entity.ExperimentStep
import com.educalab.quimicatomix.data.local.entity.ExperimentType
import com.educalab.quimicatomix.data.local.entity.OutcomeType
import com.educalab.quimicatomix.data.local.entity.VirtualSubstance
import com.educalab.quimicatomix.domain.logic.AnswerEngine
import com.educalab.quimicatomix.domain.logic.MixtureEngine
import com.educalab.quimicatomix.domain.logic.SeparationEngine
import com.educalab.quimicatomix.domain.logic.XpEngine
import com.educalab.quimicatomix.domain.model.MixtureOutcome
import com.educalab.quimicatomix.domain.model.SeparationOutcome
import com.educalab.quimicatomix.domain.model.SeparationTechnique
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class PlayPhase { LOADING, MEZCLA_PREDICT, SEPARACION_CHOOSE, GENERIC_STEP, FEEDBACK, FINISHED }
enum class FeedbackTone { CORRECT, PARTIAL, INCORRECT }

data class ExperimentPlayUiState(
    val experiment: Experiment? = null,
    val phase: PlayPhase = PlayPhase.LOADING,
    // Mezclas
    val substanceA: VirtualSubstance? = null,
    val substanceB: VirtualSubstance? = null,
    // Separacion
    val separationOptions: List<SeparationTechnique> = listOf(SeparationTechnique.FILTRACION, SeparationTechnique.DECANTACION, SeparationTechnique.EVAPORACION),
    // Generico
    val currentStep: ExperimentStep? = null,
    // Feedback comun
    val feedbackTone: FeedbackTone? = null,
    val feedbackExplanation: String = "",
    val mistakesCount: Int = 0,
    val starsEarned: Int = 0,
    val xpEarned: Int = 0
)

class ExperimentPlayViewModel(
    private val container: AppContainer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val experimentId: String = checkNotNull(savedStateHandle["experimentId"])
    private val startedAt = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(ExperimentPlayUiState())
    val uiState: StateFlow<ExperimentPlayUiState> = _uiState.asStateFlow()

    private var pendingCombinationOutcome: MixtureOutcome? = null
    private var correctSeparationTechnique: SeparationTechnique? = null

    init {
        viewModelScope.launch {
            val experiment = container.contentRepository.getExperiment(experimentId) ?: return@launch
            val combinations = container.contentRepository.getCombinations(experimentId)
            when {
                experiment.type == ExperimentType.MEZCLA && combinations.isNotEmpty() -> {
                    val combo = combinations.first()
                    val subA = container.contentRepository.getSubstance(combo.substanceAId)
                    val subB = container.contentRepository.getSubstance(combo.substanceBId)
                    _uiState.value = _uiState.value.copy(
                        experiment = experiment, substanceA = subA, substanceB = subB, phase = PlayPhase.MEZCLA_PREDICT
                    )
                }
                experiment.type == ExperimentType.SEPARACION && combinations.isNotEmpty() -> {
                    val combo = combinations.first()
                    correctSeparationTechnique = SeparationTechnique.fromKey(combo.recommendedSeparationTechnique)
                    val subA = container.contentRepository.getSubstance(combo.substanceAId)
                    val subB = container.contentRepository.getSubstance(combo.substanceBId)
                    _uiState.value = _uiState.value.copy(
                        experiment = experiment, substanceA = subA, substanceB = subB, phase = PlayPhase.SEPARACION_CHOOSE
                    )
                }
                else -> {
                    val steps = container.contentRepository.getSteps(experimentId)
                    _uiState.value = _uiState.value.copy(
                        experiment = experiment, currentStep = steps.firstOrNull(), phase = PlayPhase.GENERIC_STEP
                    )
                }
            }
        }
    }

    fun submitMixturePrediction(predictedHomogeneous: Boolean) {
        val experiment = _uiState.value.experiment ?: return
        viewModelScope.launch {
            val combinations = container.contentRepository.getCombinations(experimentId)
            val combo = combinations.firstOrNull() ?: return@launch
            val outcome = MixtureEngine.combine(combinations, combo.substanceAId, combo.substanceBId)
            pendingCombinationOutcome = outcome
            val actualHomogeneous = outcome is MixtureOutcome.Homogeneous
            val isCorrect = predictedHomogeneous == actualHomogeneous
            val explanation = when (outcome) {
                is MixtureOutcome.Homogeneous -> outcome.resultDescription
                is MixtureOutcome.Heterogeneous -> outcome.resultDescription
                MixtureOutcome.NotDefined -> "Esta combinación todavía no está definida en el laboratorio."
            }
            handleAnswerResult(isCorrect = isCorrect, isPartial = false, explanation = explanation)
        }
    }

    fun submitSeparationChoice(chosen: SeparationTechnique) {
        val correct = correctSeparationTechnique ?: return
        val outcome = SeparationEngine.evaluate(chosen, correct)
        val isCorrect = outcome is SeparationOutcome.Correct
        val explanation = if (isCorrect) {
            "¡Elegiste bien! Esa es la técnica adecuada para esta mezcla."
        } else {
            "Esa técnica no es la ideal aquí. Piensa en las propiedades de la mezcla y vuelve a intentarlo."
        }
        handleAnswerResult(isCorrect = isCorrect, isPartial = false, explanation = explanation)
    }

    fun submitGenericAnswer(submittedCsv: String) {
        val step = _uiState.value.currentStep ?: return
        val outcome = AnswerEngine.evaluate(step.interactionType, step.correctAnswerCsv, submittedCsv)
        val explanation = if (outcome.isCorrect) step.explanationCorrect else step.explanationIncorrect
        handleAnswerResult(isCorrect = outcome.isCorrect, isPartial = outcome.isPartial, explanation = explanation)
    }

    private fun handleAnswerResult(isCorrect: Boolean, isPartial: Boolean, explanation: String) {
        val current = _uiState.value
        if (isCorrect) {
            val experiment = current.experiment ?: return
            val stars = when (current.mistakesCount) {
                0 -> 3
                1 -> 2
                else -> 1
            }
            val xp = XpEngine.xpForStars(experiment.xpReward, stars)
            _uiState.value = current.copy(
                phase = PlayPhase.FEEDBACK,
                feedbackTone = FeedbackTone.CORRECT,
                feedbackExplanation = explanation,
                starsEarned = stars,
                xpEarned = xp
            )
            persistCompletion(experiment.id, experiment.topicId, stars, xp, current.mistakesCount)
        } else {
            _uiState.value = current.copy(
                phase = PlayPhase.FEEDBACK,
                feedbackTone = if (isPartial) FeedbackTone.PARTIAL else FeedbackTone.INCORRECT,
                feedbackExplanation = explanation,
                mistakesCount = current.mistakesCount + 1
            )
        }
    }

    fun retry() {
        val current = _uiState.value
        val experiment = current.experiment ?: return
        val backPhase = when (experiment.type) {
            ExperimentType.MEZCLA -> if (current.substanceA != null) PlayPhase.MEZCLA_PREDICT else PlayPhase.GENERIC_STEP
            ExperimentType.SEPARACION -> if (correctSeparationTechnique != null) PlayPhase.SEPARACION_CHOOSE else PlayPhase.GENERIC_STEP
            else -> PlayPhase.GENERIC_STEP
        }
        _uiState.value = current.copy(phase = backPhase, feedbackTone = null, feedbackExplanation = "")
    }

    private fun persistCompletion(experimentId: String, topicId: String, stars: Int, xp: Int, mistakes: Int) {
        viewModelScope.launch {
            val userId = container.currentUserId
            val profile = container.profileRepository.getProfile(userId)
            val topic = container.contentRepository.getTopic(topicId)
            container.progressRepository.registerExperimentCompletion(
                userId = userId,
                topicId = topicId,
                experimentId = experimentId,
                outcome = OutcomeType.EXITO,
                stars = stars,
                xpEarned = xp,
                mistakes = mistakes,
                startedAt = startedAt,
                finishedAt = System.currentTimeMillis(),
                playerLevel = profile?.level ?: 1,
                topicMinLevel = topic?.minLevelToUnlock ?: 1
            )
            val xpResult = container.profileRepository.awardXp(userId, xp)
            if (xpResult.didLevelUp) {
                container.contentRepository.getTopics().forEach { t ->
                    container.progressRepository.refreshUnlockState(userId, t.id, xpResult.newLevel, t.minLevelToUnlock)
                }
            }
            val stats = container.gamificationRepository.buildPlayerStats(userId)
            val allBadges = container.database.badgeDao().getAll()
            val allEquipment = container.database.labEquipmentDao().getAll()
            container.gamificationRepository.evaluateAndAwardUnlocks(userId, stats, allBadges, allEquipment)
        }
    }
}
