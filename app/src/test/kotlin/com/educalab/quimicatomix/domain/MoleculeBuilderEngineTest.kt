package com.educalab.quimicatomix.domain

import com.educalab.quimicatomix.domain.logic.MoleculeBuilderEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MoleculeBuilderEngineTest {

    @Test
    fun `parseComposition reads simple formula`() {
        val map = MoleculeBuilderEngine.parseComposition("H:2,O:1")
        assertEquals(2, map["H"])
        assertEquals(1, map["O"])
    }

    @Test
    fun `parseComposition ignores malformed entries`() {
        val map = MoleculeBuilderEngine.parseComposition("H:2,,O:abc,N:3")
        assertEquals(2, map["H"])
        assertEquals(3, map["N"])
        assertFalse(map.containsKey("O"))
    }

    @Test
    fun `parseComposition on blank string returns empty map`() {
        assertTrue(MoleculeBuilderEngine.parseComposition("").isEmpty())
    }

    @Test
    fun `evaluate returns complete for exact match`() {
        val outcome = MoleculeBuilderEngine.evaluate("H:2,O:1", mapOf("H" to 2, "O" to 1))
        assertTrue(outcome.isComplete)
        assertTrue(outcome.missingAtoms.isEmpty())
        assertTrue(outcome.extraAtoms.isEmpty())
    }

    @Test
    fun `evaluate reports missing atoms when build is incomplete`() {
        val outcome = MoleculeBuilderEngine.evaluate("H:2,O:1", mapOf("H" to 1))
        assertFalse(outcome.isComplete)
        assertEquals(1, outcome.missingAtoms["H"])
        assertEquals(1, outcome.missingAtoms["O"])
    }

    @Test
    fun `evaluate reports extra atoms when build has too many`() {
        val outcome = MoleculeBuilderEngine.evaluate("H:2,O:1", mapOf("H" to 2, "O" to 1, "N" to 1))
        assertFalse(outcome.isComplete)
        assertEquals(1, outcome.extraAtoms["N"])
    }

    @Test
    fun `evaluate reports both missing and extra atoms simultaneously`() {
        val outcome = MoleculeBuilderEngine.evaluate("H:2,O:1", mapOf("H" to 3))
        assertFalse(outcome.isComplete)
        assertEquals(1, outcome.missingAtoms["O"])
        assertEquals(1, outcome.extraAtoms["H"])
        assertEquals(2, outcome.correctAtoms["H"])
    }

    @Test
    fun `evaluate on empty build reports everything missing`() {
        val outcome = MoleculeBuilderEngine.evaluate("N:1,H:3", emptyMap())
        assertFalse(outcome.isComplete)
        assertEquals(1, outcome.missingAtoms["N"])
        assertEquals(3, outcome.missingAtoms["H"])
    }

    @Test
    fun `accuracyPercent is 100 for a complete correct build`() {
        val outcome = MoleculeBuilderEngine.evaluate("H:2,O:1", mapOf("H" to 2, "O" to 1))
        assertEquals(100, outcome.accuracyPercent)
    }

    @Test
    fun `accuracyPercent is partial for incomplete build`() {
        val outcome = MoleculeBuilderEngine.evaluate("H:4", mapOf("H" to 2))
        assertEquals(50, outcome.accuracyPercent)
    }
}
