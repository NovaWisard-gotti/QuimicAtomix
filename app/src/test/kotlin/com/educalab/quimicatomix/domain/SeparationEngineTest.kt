package com.educalab.quimicatomix.domain

import com.educalab.quimicatomix.domain.logic.SeparationEngine
import com.educalab.quimicatomix.domain.model.SeparationOutcome
import com.educalab.quimicatomix.domain.model.SeparationTechnique
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SeparationEngineTest {

    @Test
    fun `evaluate returns Correct when technique matches`() {
        val outcome = SeparationEngine.evaluate(SeparationTechnique.FILTRACION, SeparationTechnique.FILTRACION)
        assertTrue(outcome is SeparationOutcome.Correct)
    }

    @Test
    fun `evaluate returns Incorrect when technique does not match`() {
        val outcome = SeparationEngine.evaluate(SeparationTechnique.DECANTACION, SeparationTechnique.EVAPORACION)
        assertTrue(outcome is SeparationOutcome.Incorrect)
    }

    @Test
    fun `evaluate incorrect outcome carries the correct technique for feedback`() {
        val outcome = SeparationEngine.evaluate(SeparationTechnique.FILTRACION, SeparationTechnique.EVAPORACION) as SeparationOutcome.Incorrect
        assertTrue(outcome.correctTechnique == SeparationTechnique.EVAPORACION)
    }

    @Test
    fun `evaluateSequence returns true for exact matching order`() {
        assertTrue(SeparationEngine.evaluateSequence(listOf(1, 2, 3), listOf(1, 2, 3)))
    }

    @Test
    fun `evaluateSequence returns false for different order`() {
        assertFalse(SeparationEngine.evaluateSequence(listOf(2, 1, 3), listOf(1, 2, 3)))
    }

    @Test
    fun `evaluateSequence returns false for different length`() {
        assertFalse(SeparationEngine.evaluateSequence(listOf(1, 2), listOf(1, 2, 3)))
    }
}
