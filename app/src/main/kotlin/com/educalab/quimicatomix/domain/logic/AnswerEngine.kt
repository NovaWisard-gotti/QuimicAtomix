package com.educalab.quimicatomix.domain.logic

import com.educalab.quimicatomix.data.local.entity.InteractionType
import com.educalab.quimicatomix.domain.model.StepAnswerOutcome

/**
 * Motor genérico de validación de pasos de experimento (usado por Estados de la materia,
 * Átomos, Reacciones virtuales, Moléculas introductorias y como respaldo de Separación).
 *
 * Formatos de correctAnswerCsv según el tipo de interacción:
 *  - ORDENAR: lista separada por comas; el ORDEN importa exactamente.
 *  - CLASIFICAR: grupos separados por "|", cada grupo con elementos separados por ",".
 *    El número de grupos y su posición deben coincidir; el orden DENTRO de cada grupo no importa.
 *  - CONECTAR: pares "izquierda-derecha" separados por ",". El orden de los pares no importa,
 *    pero cada par debe coincidir exactamente como conjunto de pares.
 *  - Cualquier otro tipo (CONFIGURAR, PREDECIR, OBSERVAR, SELECCION_IMAGEN, CONSTRUIR,
 *    ARRASTRAR_SOLTAR, OPCION_MULTIPLE): conjunto de valores separados por comas, sin orden.
 */
object AnswerEngine {

    fun evaluate(
        interactionType: InteractionType,
        correctAnswerCsv: String,
        submittedAnswerCsv: String
    ): StepAnswerOutcome {
        if (correctAnswerCsv.isBlank()) return StepAnswerOutcome(isCorrect = false)

        return when (interactionType) {
            InteractionType.ORDENAR -> evaluateOrdered(correctAnswerCsv, submittedAnswerCsv)
            InteractionType.CLASIFICAR -> evaluateGrouped(correctAnswerCsv, submittedAnswerCsv)
            else -> evaluateSet(correctAnswerCsv, submittedAnswerCsv)
        }
    }

    private fun evaluateOrdered(correctCsv: String, submittedCsv: String): StepAnswerOutcome {
        val correct = splitCsv(correctCsv)
        val submitted = splitCsv(submittedCsv)
        return StepAnswerOutcome(isCorrect = correct == submitted)
    }

    private fun evaluateGrouped(correctCsv: String, submittedCsv: String): StepAnswerOutcome {
        val correctGroups = correctCsv.split("|").map { splitCsv(it).toSet() }
        val submittedGroups = submittedCsv.split("|").map { splitCsv(it).toSet() }
        if (correctGroups.size != submittedGroups.size) {
            return StepAnswerOutcome(isCorrect = false, isPartial = partialGroupMatch(correctGroups, submittedGroups))
        }
        val allMatch = correctGroups.indices.all { correctGroups[it] == submittedGroups[it] }
        if (allMatch) return StepAnswerOutcome(isCorrect = true)
        return StepAnswerOutcome(isCorrect = false, isPartial = partialGroupMatch(correctGroups, submittedGroups))
    }

    private fun partialGroupMatch(correctGroups: List<Set<String>>, submittedGroups: List<Set<String>>): Boolean {
        val totalItems = correctGroups.sumOf { it.size }
        if (totalItems == 0) return false
        var matches = 0
        for (i in correctGroups.indices) {
            val submitted = submittedGroups.getOrNull(i) ?: emptySet()
            matches += correctGroups[i].intersect(submitted).size
        }
        return matches.toDouble() / totalItems >= 0.5
    }

    private fun evaluateSet(correctCsv: String, submittedCsv: String): StepAnswerOutcome {
        val correctSet = splitCsv(correctCsv).toSet()
        val submittedSet = splitCsv(submittedCsv).toSet()
        if (correctSet == submittedSet) return StepAnswerOutcome(isCorrect = true)
        val matches = correctSet.intersect(submittedSet).size
        val noExtras = submittedSet.subtract(correctSet).isEmpty()
        val ratio = if (correctSet.isEmpty()) 0.0 else matches.toDouble() / correctSet.size
        return StepAnswerOutcome(isCorrect = false, isPartial = ratio >= 0.5 && noExtras)
    }

    private fun splitCsv(csv: String): List<String> =
        csv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
