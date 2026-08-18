package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Pieza de equipamiento de laboratorio coleccionable (desbloqueable por progreso real). */
@Entity(tableName = "lab_equipment")
data class LabEquipment(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val rarity: EquipmentRarity,
    val unlockCriteriaType: CriteriaType,
    val unlockCriteriaValue: Int,
    val orderIndex: Int
)
