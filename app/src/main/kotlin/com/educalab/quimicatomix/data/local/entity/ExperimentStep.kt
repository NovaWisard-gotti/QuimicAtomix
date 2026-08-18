package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Paso individual dentro de un experimento (una práctica puede tener 1-4 pasos). */
@Entity(
    tableName = "experiment_step",
    foreignKeys = [
        ForeignKey(
            entity = Experiment::class,
            parentColumns = ["id"],
            childColumns = ["experimentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("experimentId")]
)
data class ExperimentStep(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val experimentId: String,
    val stepIndex: Int,
    val instruction: String,
    val interactionType: InteractionType,
    // Datos del reto codificados como CSV simple y mantenible (sin JSON externo):
    // p.ej. opciones="agua,arena,sal" correcta="arena" ó secuencia="1,3,2"
    val optionsCsv: String,
    val correctAnswerCsv: String,
    val explanationCorrect: String,
    val explanationIncorrect: String
)
