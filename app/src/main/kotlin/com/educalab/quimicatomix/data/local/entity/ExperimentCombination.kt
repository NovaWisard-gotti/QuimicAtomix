package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Combinación permitida/no permitida entre dos sustancias virtuales dentro de un experimento
 * de mezclas o separación. Es la "receta" que valida el MixtureEngine.
 */
@Entity(
    tableName = "experiment_combination",
    foreignKeys = [
        ForeignKey(
            entity = Experiment::class,
            parentColumns = ["id"],
            childColumns = ["experimentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VirtualSubstance::class,
            parentColumns = ["id"],
            childColumns = ["substanceAId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VirtualSubstance::class,
            parentColumns = ["id"],
            childColumns = ["substanceBId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("experimentId"), Index("substanceAId"), Index("substanceBId")]
)
data class ExperimentCombination(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val experimentId: String,
    val substanceAId: String,
    val substanceBId: String,
    val isHomogeneous: Boolean,       // true = mezcla homogénea, false = heterogénea
    val recommendedSeparationTechnique: String, // "filtracion" | "decantacion" | "evaporacion" | "ninguna"
    val resultDescription: String
)
