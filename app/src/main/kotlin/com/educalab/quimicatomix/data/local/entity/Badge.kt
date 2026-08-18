package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Definición de una insignia (mínimo 8 recompensas ilustradas). */
@Entity(tableName = "badge")
data class Badge(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val category: BadgeCategory,
    val iconKey: String,
    val criteriaType: CriteriaType,
    val criteriaValue: Int,
    val orderIndex: Int
)
