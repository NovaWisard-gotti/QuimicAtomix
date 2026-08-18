package com.educalab.quimicatomix.domain

import com.educalab.quimicatomix.data.local.entity.ExperimentCombination
import com.educalab.quimicatomix.domain.logic.MixtureEngine
import com.educalab.quimicatomix.domain.model.MixtureOutcome
import com.educalab.quimicatomix.domain.model.SeparationTechnique
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MixtureEngineTest {

    private val combos = listOf(
        ExperimentCombination(
            experimentId = "exp1", substanceAId = "agua", substanceBId = "sal",
            isHomogeneous = true, recommendedSeparationTechnique = "evaporacion",
            resultDescription = "Se disuelve por completo"
        ),
        ExperimentCombination(
            experimentId = "exp1", substanceAId = "agua", substanceBId = "arena",
            isHomogeneous = false, recommendedSeparationTechnique = "filtracion",
            resultDescription = "La arena no se disuelve"
        )
    )

    @Test
    fun `combine returns homogeneous for matching pair`() {
        val outcome = MixtureEngine.combine(combos, "agua", "sal")
        assertTrue(outcome is MixtureOutcome.Homogeneous)
    }

    @Test
    fun `combine returns heterogeneous with suggested technique`() {
        val outcome = MixtureEngine.combine(combos, "agua", "arena")
        assertTrue(outcome is MixtureOutcome.Heterogeneous)
        assertEquals(SeparationTechnique.FILTRACION, (outcome as MixtureOutcome.Heterogeneous).suggestedTechnique)
    }

    @Test
    fun `combine works regardless of substance order`() {
        val outcome = MixtureEngine.combine(combos, "arena", "agua")
        assertTrue(outcome is MixtureOutcome.Heterogeneous)
    }

    @Test
    fun `combine returns NotDefined for unknown pair`() {
        val outcome = MixtureEngine.combine(combos, "aceite", "jabon_liquido")
        assertEquals(MixtureOutcome.NotDefined, outcome)
    }

    @Test
    fun `combine on empty combination list returns NotDefined`() {
        val outcome = MixtureEngine.combine(emptyList(), "agua", "sal")
        assertEquals(MixtureOutcome.NotDefined, outcome)
    }
}
