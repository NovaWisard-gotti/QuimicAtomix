package com.educalab.quimicatomix.domain

import com.educalab.quimicatomix.domain.logic.XpEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class XpEngineTest {

    @Test
    fun `level for zero xp is 1`() {
        assertEquals(1, XpEngine.levelForXp(0))
    }

    @Test
    fun `level for negative xp defensively returns 1`() {
        assertEquals(1, XpEngine.levelForXp(-10))
    }

    @Test
    fun `level increases at expected xp thresholds`() {
        assertEquals(1, XpEngine.levelForXp(10))
        assertEquals(2, XpEngine.levelForXp(50))
        assertEquals(3, XpEngine.levelForXp(200))
        assertEquals(4, XpEngine.levelForXp(450))
    }

    @Test
    fun `xpRequiredForLevel is inverse of levelForXp at boundaries`() {
        val xpForLevel3 = XpEngine.xpRequiredForLevel(3)
        assertEquals(3, XpEngine.levelForXp(xpForLevel3))
    }

    @Test
    fun `xpForStars gives full bonus for three stars`() {
        val base = 20
        val xp3 = XpEngine.xpForStars(base, 3)
        val xp2 = XpEngine.xpForStars(base, 2)
        val xp1 = XpEngine.xpForStars(base, 1)
        val xp0 = XpEngine.xpForStars(base, 0)
        assertTrue(xp3 > xp2)
        assertTrue(xp2 > xp1)
        assertTrue(xp1 > xp0)
    }

    @Test
    fun `xpForStars never returns zero or negative`() {
        assertTrue(XpEngine.xpForStars(1, 0) >= 1)
    }

    @Test
    fun `award detects level up correctly`() {
        val result = XpEngine.award(currentTotalXp = 40, xpToAdd = 20)
        assertEquals(60, result.newTotalXp)
        assertTrue(result.didLevelUp)
    }

    @Test
    fun `award does not report level up when staying within same level`() {
        val result = XpEngine.award(currentTotalXp = 0, xpToAdd = 5)
        assertFalse(result.didLevelUp)
    }

    @Test
    fun `progressWithinLevel stays within 0 and 1`() {
        val progress = XpEngine.progressWithinLevel(75)
        assertTrue(progress in 0f..1f)
    }
}
