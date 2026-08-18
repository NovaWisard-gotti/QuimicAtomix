package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Sustancia 100% virtual/conceptual usada en mezclas y separación.
 * No representa procedimientos reales peligrosos: son metáforas visuales seguras
 * (agua, arena, aceite, sal, limaduras de hierro conceptuales, etc.).
 */
@Entity(
    tableName = "virtual_substance",
    foreignKeys = [
        ForeignKey(
            entity = ChemicalTopic::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("topicId")]
)
data class VirtualSubstance(
    @PrimaryKey val id: String,
    val topicId: String,
    val name: String,
    val symbolOrFormula: String,
    val state: MatterState,
    val colorHex: String,
    val description: String,
    val iconKey: String,
    val isMiscible: Boolean,          // se disuelve/mezcla homogéneamente con agua conceptual
    val isMagneticConceptual: Boolean,
    val densityTier: Int              // 1 = flota, 2 = intermedio, 3 = se hunde (conceptual, sin cifras reales)
)
