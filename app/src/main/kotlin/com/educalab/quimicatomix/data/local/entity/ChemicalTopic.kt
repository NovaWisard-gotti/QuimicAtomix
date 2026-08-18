package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Un área temática de la Academia (Materia, Mezclas, Separación, Átomos, Moléculas, Reacciones...). */
@Entity(tableName = "chemical_topic")
data class ChemicalTopic(
    @PrimaryKey val id: String,       // código estable, p.ej. "estados"
    val title: String,
    val shortDescription: String,
    val narrativeIntro: String,       // frase narrativa ligera al entrar al tema
    val iconKey: String,              // clave de icono temático (ver IconCatalog)
    val colorHex: String,
    val orderIndex: Int,
    val minLevelToUnlock: Int
)
