package com.educalab.quimicatomix.domain.logic

import com.educalab.quimicatomix.domain.model.SeparationOutcome
import com.educalab.quimicatomix.domain.model.SeparationTechnique

/**
 * Motor de separación de mezclas (módulo 5). Compara la técnica elegida por el jugador
 * (filtración, decantación o evaporación conceptual) contra la técnica correcta asociada
 * a la mezcla heterogénea que se está resolviendo.
 */
object SeparationEngine {

    fun evaluate(
        chosenTechnique: SeparationTechnique,
        correctTechnique: SeparationTechnique
    ): SeparationOutcome {
        return if (chosenTechnique == correctTechnique) {
            SeparationOutcome.Correct(chosenTechnique)
        } else {
            SeparationOutcome.Incorrect(chosenTechnique, correctTechnique)
        }
    }

    /**
     * Evalúa una secuencia de pasos ordenados (p.ej. para evaporación: 1-calentar
     * conceptualmente, 2-esperar, 3-recoger residuo). Devuelve true solo si el orden
     * completo coincide.
     */
    fun evaluateSequence(chosenOrder: List<Int>, correctOrder: List<Int>): Boolean {
        if (chosenOrder.size != correctOrder.size) return false
        return chosenOrder == correctOrder
    }
}
