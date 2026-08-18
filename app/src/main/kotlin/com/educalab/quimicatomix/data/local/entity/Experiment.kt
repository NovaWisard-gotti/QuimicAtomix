package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Una práctica virtual del laboratorio (mínimo 55 en el contenido semilla).
 * "Experimento" es la unidad jugable central de los módulos 3 a 8.
 */
@Entity(
    tableName = "experiment",
    foreignKeys = [
        ForeignKey(
            entity = ChemicalTopic::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("topicId"), Index(value = ["code"], unique = true)]
)
data class Experiment(
    @PrimaryKey val id: String,
    val code: String,                 // código único legible, p.ej. "MEZ-014"
    val topicId: String,
    val type: ExperimentType,
    val title: String,
    val narrativeHook: String,        // gancho narrativo breve ("Quimi necesita separar...")
    val description: String,
    val difficulty: Int,              // 1..3
    val primaryInteraction: InteractionType,
    val xpReward: Int,
    val orderIndex: Int,
    val requiredLevel: Int,
    val iconKey: String
)
