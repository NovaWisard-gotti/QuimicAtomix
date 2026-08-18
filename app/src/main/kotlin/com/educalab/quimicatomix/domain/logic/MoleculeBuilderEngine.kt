package com.educalab.quimicatomix.domain.logic

import com.educalab.quimicatomix.domain.model.MoleculeBuildOutcome

/**
 * Motor del constructor molecular (módulo 7). El jugador arrastra átomos a una zona de
 * construcción; este motor compara la composición resultante contra la composición
 * objetivo (p.ej. "H2O" => H:2, O:1) y explica exactamente qué falta o sobra, en vez de
 * dar simplemente correcto/incorrecto.
 */
object MoleculeBuilderEngine {

    /** Convierte un CSV "H:2,O:1" en un mapa símbolo -> cantidad. Ignora entradas vacías. */
    fun parseComposition(compositionCsv: String): Map<String, Int> {
        if (compositionCsv.isBlank()) return emptyMap()
        return compositionCsv.split(",")
            .mapNotNull { entry ->
                val parts = entry.trim().split(":")
                if (parts.size != 2) return@mapNotNull null
                val symbol = parts[0].trim()
                val count = parts[1].trim().toIntOrNull() ?: return@mapNotNull null
                if (symbol.isEmpty() || count <= 0) null else symbol to count
            }
            .toMap()
    }

    fun evaluate(targetCompositionCsv: String, builtAtoms: Map<String, Int>): MoleculeBuildOutcome {
        val target = parseComposition(targetCompositionCsv)

        val missing = mutableMapOf<String, Int>()
        val extra = mutableMapOf<String, Int>()
        val correct = mutableMapOf<String, Int>()

        val allSymbols = (target.keys + builtAtoms.keys).toSet()
        for (symbol in allSymbols) {
            val required = target[symbol] ?: 0
            val built = builtAtoms[symbol] ?: 0
            when {
                built == required && required > 0 -> correct[symbol] = built
                built < required -> {
                    if (built > 0) correct[symbol] = built
                    missing[symbol] = required - built
                }
                built > required -> {
                    if (required > 0) correct[symbol] = required
                    extra[symbol] = built - required
                }
            }
        }

        val isComplete = missing.isEmpty() && extra.isEmpty() && target.isNotEmpty()
        return MoleculeBuildOutcome(
            isComplete = isComplete,
            missingAtoms = missing.filterValues { it > 0 },
            extraAtoms = extra.filterValues { it > 0 },
            correctAtoms = correct.filterValues { it > 0 }
        )
    }
}
