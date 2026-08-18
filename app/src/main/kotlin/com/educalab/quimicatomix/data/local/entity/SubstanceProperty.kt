package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Propiedad observable de una sustancia virtual (para tarjetas de propiedades y comparaciones). */
@Entity(
    tableName = "substance_property",
    foreignKeys = [
        ForeignKey(
            entity = VirtualSubstance::class,
            parentColumns = ["id"],
            childColumns = ["substanceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("substanceId")]
)
data class SubstanceProperty(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val substanceId: String,
    val propertyKey: String,          // p.ej. "color", "solubilidad", "magnetismo", "brillo"
    val propertyValue: String,        // p.ej. "Se disuelve en agua"
    val iconKey: String
)
