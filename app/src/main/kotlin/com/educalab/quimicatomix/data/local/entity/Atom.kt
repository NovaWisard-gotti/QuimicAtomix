package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Átomo simplificado con fines educativos 8-12 (sin isótopos ni química avanzada). */
@Entity(tableName = "atom")
data class Atom(
    @PrimaryKey val id: String,       // símbolo, p.ej. "H", "O", "Na"
    val symbol: String,
    val name: String,
    val protons: Int,
    val electronsShellsCsv: String,   // p.ej. "2,1" para Na (capas conceptuales)
    val category: AtomCategory,
    val colorHex: String,
    val funFact: String,
    val commonValence: Int            // enlaces típicos permitidos en el constructor (simplificado)
)
