package com.educalab.quimicatomix.domain.logic

import com.educalab.quimicatomix.domain.model.XpAwardResult
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Motor de experiencia y niveles. Curva de nivel simple y predecible (raíz cuadrada),
 * fácil de testear y de balancear: nivel = floor(sqrt(xpTotal / 50)) + 1.
 * Esto da, por ejemplo: 0xp=Nv1, 50xp=Nv2, 200xp=Nv3, 450xp=Nv4, 800xp=Nv5...
 */
object XpEngine {

    private const val XP_PER_LEVEL_UNIT = 50

    fun levelForXp(totalXp: Int): Int {
        if (totalXp <= 0) return 1
        return floor(sqrt(totalXp / XP_PER_LEVEL_UNIT.toDouble())).toInt() + 1
    }

    fun xpRequiredForLevel(level: Int): Int {
        if (level <= 1) return 0
        val n = (level - 1)
        return n * n * XP_PER_LEVEL_UNIT
    }

    fun xpForStars(baseXp: Int, stars: Int): Int {
        val bonus = when (stars) {
            3 -> 1.2
            2 -> 1.0
            1 -> 0.6
            else -> 0.25 // intento registrado aunque no se complete, mínimo esfuerzo reconocido
        }
        return (baseXp * bonus).toInt().coerceAtLeast(1)
    }

    fun award(currentTotalXp: Int, xpToAdd: Int): XpAwardResult {
        val previousLevel = levelForXp(currentTotalXp)
        val newTotal = currentTotalXp + xpToAdd
        val newLevel = levelForXp(newTotal)
        return XpAwardResult(
            xpAwarded = xpToAdd,
            newTotalXp = newTotal,
            previousLevel = previousLevel,
            newLevel = newLevel
        )
    }

    fun progressWithinLevel(totalXp: Int): Float {
        val level = levelForXp(totalXp)
        val floorXp = xpRequiredForLevel(level)
        val ceilXp = xpRequiredForLevel(level + 1)
        val span = (ceilXp - floorXp).coerceAtLeast(1)
        return ((totalXp - floorXp).toFloat() / span).coerceIn(0f, 1f)
    }
}
