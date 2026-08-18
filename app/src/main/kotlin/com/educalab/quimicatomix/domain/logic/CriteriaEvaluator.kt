package com.educalab.quimicatomix.domain.logic

import com.educalab.quimicatomix.data.local.entity.CriteriaType
import com.educalab.quimicatomix.domain.model.PlayerStats

/**
 * Evalúa si unas estadísticas de jugador cumplen un criterio de desbloqueo. Es el motor
 * compartido por insignias (Badge) y equipamiento de laboratorio (LabEquipment): ambos
 * usan el mismo [CriteriaType] + valor umbral, evitando duplicar reglas de negocio.
 */
object CriteriaEvaluator {

    fun isSatisfied(criteriaType: CriteriaType, criteriaValue: Int, stats: PlayerStats): Boolean {
        val actual = when (criteriaType) {
            CriteriaType.EXPERIMENTOS_COMPLETADOS -> stats.experimentsCompleted
            CriteriaType.TEMA_DOMINADO -> stats.topicsMastered
            CriteriaType.RACHA_ACIERTOS -> stats.currentSuccessStreak
            CriteriaType.ESCENARIOS_SEGURIDAD -> stats.safetyScenariosCompleted
            CriteriaType.MOLECULAS_CONSTRUIDAS -> stats.moleculesBuilt
            CriteriaType.NIVEL_ALCANZADO -> stats.level
            CriteriaType.ESTRELLAS_TOTALES -> stats.totalStars
        }
        return actual >= criteriaValue
    }

    /** Devuelve, de una lista candidata (id -> criterio/valor), los ids recién satisfechos. */
    fun evaluateAll(
        candidates: List<Triple<String, CriteriaType, Int>>,
        alreadyUnlockedIds: Set<String>,
        stats: PlayerStats
    ): List<String> {
        return candidates
            .filter { (id, _, _) -> id !in alreadyUnlockedIds }
            .filter { (_, type, value) -> isSatisfied(type, value, stats) }
            .map { it.first }
    }
}
