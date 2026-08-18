package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Progreso agregado y persistido de un usuario dentro de un tema (para el mapa de Academia). */
@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(entity = UserProfile::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ChemicalTopic::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["userId", "topicId"], unique = true)]
)
data class Progress(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val topicId: String,
    val experimentsCompleted: Int = 0,
    val experimentsTotal: Int,
    val starsTotal: Int = 0,
    val mastery: MasteryState = MasteryState.BLOQUEADO,
    val lastPlayedAt: Long? = null
)
