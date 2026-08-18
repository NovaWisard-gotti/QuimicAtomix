package com.educalab.quimicatomix.domain

import com.educalab.quimicatomix.data.local.entity.CriteriaType
import com.educalab.quimicatomix.domain.logic.CriteriaEvaluator
import com.educalab.quimicatomix.domain.model.PlayerStats
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CriteriaEvaluatorTest {

    private val stats = PlayerStats(
        experimentsCompleted = 12,
        topicsMastered = 1,
        currentSuccessStreak = 6,
        safetyScenariosCompleted = 8,
        moleculesBuilt = 3,
        level = 4,
        totalStars = 30
    )

    @Test
    fun `isSatisfied true when stat meets exact threshold`() {
        assertTrue(CriteriaEvaluator.isSatisfied(CriteriaType.EXPERIMENTOS_COMPLETADOS, 12, stats))
    }

    @Test
    fun `isSatisfied true when stat exceeds threshold`() {
        assertTrue(CriteriaEvaluator.isSatisfied(CriteriaType.RACHA_ACIERTOS, 5, stats))
    }

    @Test
    fun `isSatisfied false when stat is below threshold`() {
        assertFalse(CriteriaEvaluator.isSatisfied(CriteriaType.MOLECULAS_CONSTRUIDAS, 5, stats))
    }

    @Test
    fun `isSatisfied checks the correct stat field for each criteria type`() {
        assertTrue(CriteriaEvaluator.isSatisfied(CriteriaType.NIVEL_ALCANZADO, 4, stats))
        assertTrue(CriteriaEvaluator.isSatisfied(CriteriaType.ESTRELLAS_TOTALES, 30, stats))
        assertTrue(CriteriaEvaluator.isSatisfied(CriteriaType.ESCENARIOS_SEGURIDAD, 8, stats))
        assertTrue(CriteriaEvaluator.isSatisfied(CriteriaType.TEMA_DOMINADO, 1, stats))
    }

    @Test
    fun `evaluateAll excludes ids already unlocked`() {
        val candidates = listOf(Triple("badge1", CriteriaType.EXPERIMENTOS_COMPLETADOS, 1))
        val result = CriteriaEvaluator.evaluateAll(candidates, alreadyUnlockedIds = setOf("badge1"), stats = stats)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `evaluateAll returns all newly satisfied ids`() {
        val candidates = listOf(
            Triple("badge_low", CriteriaType.EXPERIMENTOS_COMPLETADOS, 1),
            Triple("badge_high", CriteriaType.EXPERIMENTOS_COMPLETADOS, 100)
        )
        val result = CriteriaEvaluator.evaluateAll(candidates, alreadyUnlockedIds = emptySet(), stats = stats)
        assertTrue(result.contains("badge_low"))
        assertFalse(result.contains("badge_high"))
    }
}
