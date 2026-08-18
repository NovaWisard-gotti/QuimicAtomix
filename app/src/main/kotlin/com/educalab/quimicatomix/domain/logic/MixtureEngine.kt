package com.educalab.quimicatomix.domain.logic

import com.educalab.quimicatomix.data.local.entity.ExperimentCombination
import com.educalab.quimicatomix.domain.model.MixtureOutcome
import com.educalab.quimicatomix.domain.model.SeparationTechnique

/**
 * Motor de mezclas virtuales (módulo 4). Determina si combinar dos sustancias produce
 * una mezcla homogénea o heterogénea, consultando las combinaciones semilla definidas
 * para el experimento. No contiene ningún procedimiento real: opera sobre IDs de
 * sustancias conceptuales (agua, arena, aceite, sal...).
 */
object MixtureEngine {

    /**
     * @param combinations combinaciones válidas conocidas para el experimento actual.
     * @param substanceAId sustancia elegida primero por el jugador.
     * @param substanceBId sustancia elegida segunda por el jugador.
     */
    fun combine(
        combinations: List<ExperimentCombination>,
        substanceAId: String,
        substanceBId: String
    ): MixtureOutcome {
        val match = combinations.firstOrNull {
            (it.substanceAId == substanceAId && it.substanceBId == substanceBId) ||
                (it.substanceAId == substanceBId && it.substanceBId == substanceAId)
        } ?: return MixtureOutcome.NotDefined

        return if (match.isHomogeneous) {
            MixtureOutcome.Homogeneous(match.resultDescription)
        } else {
            MixtureOutcome.Heterogeneous(
                resultDescription = match.resultDescription,
                suggestedTechnique = SeparationTechnique.fromKey(match.recommendedSeparationTechnique)
            )
        }
    }
}
