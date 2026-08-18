package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Reto del constructor molecular: construir una molécula conocida a partir de átomos. */
@Entity(tableName = "molecule_challenge")
data class MoleculeChallenge(
    @PrimaryKey val id: String,
    val formula: String,              // p.ej. "H2O"
    val commonName: String,           // p.ej. "Agua"
    val description: String,
    // Composición requerida como CSV "simbolo:cantidad", p.ej. "H:2,O:1"
    val compositionCsv: String,
    val difficulty: Int,
    val xpReward: Int,
    val unlockLevel: Int,
    val funFact: String,
    val iconKey: String
)
