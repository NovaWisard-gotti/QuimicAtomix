package com.educalab.quimicatomix.domain.model

/** Resultado de intentar combinar dos sustancias virtuales en el módulo de Mezclas. */
sealed class MixtureOutcome {
    data class Homogeneous(val resultDescription: String) : MixtureOutcome()
    data class Heterogeneous(val resultDescription: String, val suggestedTechnique: SeparationTechnique) : MixtureOutcome()
    /** La combinación no está definida como práctica segura para este experimento. */
    object NotDefined : MixtureOutcome()
}

enum class SeparationTechnique { FILTRACION, DECANTACION, EVAPORACION, NINGUNA;

    companion object {
        fun fromKey(key: String): SeparationTechnique = when (key.trim().lowercase()) {
            "filtracion", "filtración" -> FILTRACION
            "decantacion", "decantación" -> DECANTACION
            "evaporacion", "evaporación" -> EVAPORACION
            else -> NINGUNA
        }
    }
}

/** Resultado de aplicar una técnica de separación a una mezcla heterogénea. */
sealed class SeparationOutcome {
    data class Correct(val techniqueUsed: SeparationTechnique) : SeparationOutcome()
    data class Incorrect(val techniqueUsed: SeparationTechnique, val correctTechnique: SeparationTechnique) : SeparationOutcome()
}

/** Resultado de comparar la composición construida por el jugador contra el reto molecular. */
data class MoleculeBuildOutcome(
    val isComplete: Boolean,
    val missingAtoms: Map<String, Int>,   // símbolo -> cantidad que falta
    val extraAtoms: Map<String, Int>,     // símbolo -> cantidad sobrante
    val correctAtoms: Map<String, Int>    // símbolo -> cantidad correctamente colocada
) {
    val accuracyPercent: Int
        get() {
            val totalRequired = correctAtoms.values.sum() + missingAtoms.values.sum()
            if (totalRequired == 0) return 0
            return ((correctAtoms.values.sum().toDouble() / totalRequired) * 100).toInt()
        }
}

/** Resultado genérico de validar la respuesta de un paso de experimento. */
data class StepAnswerOutcome(
    val isCorrect: Boolean,
    val isPartial: Boolean = false
)

/** Snapshot de estadísticas agregadas de un jugador, usado para evaluar desbloqueos. */
data class PlayerStats(
    val experimentsCompleted: Int,
    val topicsMastered: Int,
    val currentSuccessStreak: Int,
    val safetyScenariosCompleted: Int,
    val moleculesBuilt: Int,
    val level: Int,
    val totalStars: Int
)

/** Resultado de otorgar XP: nuevo total y si hubo subida de nivel. */
data class XpAwardResult(
    val xpAwarded: Int,
    val newTotalXp: Int,
    val previousLevel: Int,
    val newLevel: Int
) {
    val didLevelUp: Boolean get() = newLevel > previousLevel
}
