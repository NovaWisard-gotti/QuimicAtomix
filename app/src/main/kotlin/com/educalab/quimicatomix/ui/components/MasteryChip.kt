package com.educalab.quimicatomix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.data.local.entity.MasteryState
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabWhite
import com.educalab.quimicatomix.ui.theme.StateAvailable
import com.educalab.quimicatomix.ui.theme.StateCompleted
import com.educalab.quimicatomix.ui.theme.StateLocked
import com.educalab.quimicatomix.ui.theme.StateMastered
import com.educalab.quimicatomix.ui.theme.StateStarted

/**
 * Indicador visual de estado (bloqueado/disponible/iniciado/completado/dominado). Combina
 * SIEMPRE color + icono + texto para no depender únicamente del color (accesibilidad).
 */
@Composable
fun MasteryChip(mastery: MasteryState, modifier: Modifier = Modifier) {
    val (color, icon, label) = when (mastery) {
        MasteryState.BLOQUEADO -> Triple(StateLocked, Icons.Filled.Lock, "Bloqueado")
        MasteryState.DISPONIBLE -> Triple(StateAvailable, Icons.Filled.PlayArrow, "Disponible")
        MasteryState.INICIADO -> Triple(StateStarted, Icons.Filled.Star, "Iniciado")
        MasteryState.COMPLETADO -> Triple(StateCompleted, Icons.Filled.CheckCircle, "Completado")
        MasteryState.DOMINADO -> Triple(StateMastered, Icons.Filled.WorkspacePremium, "¡Dominado!")
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = LabWhite, modifier = Modifier.padding(end = 4.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = LabWhite)
    }
}
