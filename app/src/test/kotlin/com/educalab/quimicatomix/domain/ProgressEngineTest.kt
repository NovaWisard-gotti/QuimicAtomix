package com.educalab.quimicatomix.domain

import com.educalab.quimicatomix.data.local.entity.MasteryState
import com.educalab.quimicatomix.domain.logic.ProgressEngine
import org.junit.Assert.assertEquals
import org.junit.Test

class ProgressEngineTest {

    @Test
    fun `topic is BLOQUEADO when player level is below requirement`() {
        val mastery = ProgressEngine.computeMastery(completed = 5, total = 10, starsTotal = 10, isUnlockedByLevel = false)
        assertEquals(MasteryState.BLOQUEADO, mastery)
    }

    @Test
    fun `topic is DISPONIBLE when unlocked but nothing completed yet`() {
        val mastery = ProgressEngine.computeMastery(completed = 0, total = 10, starsTotal = 0, isUnlockedByLevel = true)
        assertEquals(MasteryState.DISPONIBLE, mastery)
    }

    @Test
    fun `topic is INICIADO when partially completed`() {
        val mastery = ProgressEngine.computeMastery(completed = 4, total = 10, starsTotal = 8, isUnlockedByLevel = true)
        assertEquals(MasteryState.INICIADO, mastery)
    }

    @Test
    fun `topic is COMPLETADO when finished with modest star average`() {
        val mastery = ProgressEngine.computeMastery(completed = 10, total = 10, starsTotal = 15, isUnlockedByLevel = true)
        assertEquals(MasteryState.COMPLETADO, mastery)
    }

    @Test
    fun `topic is DOMINADO when finished with high star average`() {
        val mastery = ProgressEngine.computeMastery(completed = 10, total = 10, starsTotal = 29, isUnlockedByLevel = true)
        assertEquals(MasteryState.DOMINADO, mastery)
    }

    @Test
    fun `topic with zero total experiments does not crash and is not DOMINADO`() {
        val mastery = ProgressEngine.computeMastery(completed = 0, total = 0, starsTotal = 0, isUnlockedByLevel = true)
        assertEquals(MasteryState.DISPONIBLE, mastery)
    }
}
