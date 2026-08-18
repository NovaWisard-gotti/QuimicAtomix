package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Resultado consolidado de un intento sobre un experimento (para historial/estadísticas reales). */
@Entity(
    tableName = "experiment_result",
    foreignKeys = [
        ForeignKey(
            entity = Attempt::class,
            parentColumns = ["id"],
            childColumns = ["attemptId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Experiment::class,
            parentColumns = ["id"],
            childColumns = ["experimentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("attemptId"), Index("experimentId")]
)
data class ExperimentResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val attemptId: Long,
    val experimentId: String,
    val outcome: OutcomeType,
    val starsEarned: Int,             // 0..3
    val xpEarned: Int,
    val mistakesCount: Int,
    val timestamp: Long
)
