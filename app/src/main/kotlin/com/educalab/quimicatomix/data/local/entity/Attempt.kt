package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Un intento del jugador sobre CUALQUIER actividad (experimento, molécula o escenario de
 * seguridad). Exactamente uno de los tres FK opcionales debe estar presente; se valida en
 * capa de dominio (AttemptValidator) para mantener la entidad simple y testeable.
 */
@Entity(
    tableName = "attempt",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("experimentId"), Index("moleculeChallengeId"), Index("safetyScenarioId")]
)
data class Attempt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val experimentId: String? = null,
    val moleculeChallengeId: String? = null,
    val safetyScenarioId: String? = null,
    val startedAt: Long,
    val finishedAt: Long,
    val success: Boolean,
    val starsEarned: Int,
    val xpEarned: Int,
    val mistakesCount: Int
)
