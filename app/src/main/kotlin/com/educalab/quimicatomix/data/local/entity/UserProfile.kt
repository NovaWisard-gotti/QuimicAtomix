package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Perfil local del jugador. Sin datos personales reales: solo alias elegido y avatar local.
 * No se solicita email, teléfono ni ninguna identificación real (privacidad infantil).
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val alias: String,
    val avatarId: Int,               // índice 0..7 sobre los 8 avatares locales
    val createdAt: Long,
    val lastActiveAt: Long,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val totalXp: Int = 0,
    val level: Int = 1
)
