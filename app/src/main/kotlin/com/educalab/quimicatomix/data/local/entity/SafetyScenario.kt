package com.educalab.quimicatomix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Escenario de seguridad (mínimo 35 en semilla). Enseña actitudes seguras en el laboratorio
 * SIN describir procedimientos peligrosos reales: siempre en positivo (qué hacer / a quién avisar).
 */
@Entity(tableName = "safety_scenario")
data class SafetyScenario(
    @PrimaryKey val id: String,
    val category: SafetyCategory,
    val title: String,
    val situationText: String,        // situación breve y concreta
    val correctActionText: String,    // acción segura correcta
    val distractorActionCsv: String,  // 2 alternativas incorrectas plausibles, separadas por "|"
    val explanation: String,          // por qué es la acción correcta (educativo, no alarmista)
    val iconKey: String,
    val orderIndex: Int
)
