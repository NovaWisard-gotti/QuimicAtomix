package com.educalab.quimicatomix.domain.logic

import com.educalab.quimicatomix.data.local.entity.MasteryState

/**
 * Motor de progresión de un tema (módulo Academia / módulo 10 Progreso-equipamiento).
 * El estado visual se deriva SIEMPRE de acciones reales persistidas (experimentos
 * completados y estrellas), nunca se asigna manualmente desde la UI.
 */
object ProgressEngine {

    /**
     * @param completed experimentos completados del tema.
     * @param total experimentos totales del tema.
     * @param starsTotal estrellas acumuladas del tema.
     * @param isUnlockedByLevel si el nivel del jugador alcanza el mínimo requerido por el tema.
     */
    fun computeMastery(
        completed: Int,
        total: Int,
        starsTotal: Int,
        isUnlockedByLevel: Boolean
    ): MasteryState {
        if (!isUnlockedByLevel) return MasteryState.BLOQUEADO
        if (completed <= 0) return MasteryState.DISPONIBLE
        if (completed < total) return MasteryState.INICIADO
        // Completado el 100% de experimentos: distinguir COMPLETADO de DOMINADO
        // según la calidad media (>=2.5 estrellas de media exige dominio real, no solo intentos).
        val maxPossibleStars = total * 3
        val ratio = if (maxPossibleStars == 0) 0.0 else starsTotal.toDouble() / maxPossibleStars
        return if (ratio >= 0.85) MasteryState.DOMINADO else MasteryState.COMPLETADO
    }
}
