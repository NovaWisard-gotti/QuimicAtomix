package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Relación N:M materializada: qué equipamiento desbloqueó cada usuario y cuándo. */
@Entity(
    tableName = "unlocked_equipment",
    foreignKeys = [
        ForeignKey(entity = UserProfile::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = LabEquipment::class, parentColumns = ["id"], childColumns = ["equipmentId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["userId", "equipmentId"], unique = true)]
)
data class UnlockedEquipment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val equipmentId: String,
    val unlockedAt: Long
)
