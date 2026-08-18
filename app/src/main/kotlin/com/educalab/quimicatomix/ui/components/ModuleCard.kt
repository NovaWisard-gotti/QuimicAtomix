package com.educalab.quimicatomix.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.data.local.entity.MasteryState
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabWhite

/**
 * Tarjeta ilustrada de un módulo/tema para la Academia. NO es una tarjeta plana de
 * "título + botón": incluye ilustración temática grande, progreso visual y estado.
 */
@Composable
fun ModuleCard(
    title: String,
    subtitle: String,
    illustration: IllustrationKind,
    tint: Color,
    mastery: MasteryState,
    progressFraction: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cardScale"
    )
    val enabled = mastery != MasteryState.BLOQUEADO

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.horizontalGradient(listOf(tint.copy(alpha = 0.28f), LabNavy700)))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled
            ) { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(tint.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                LabIllustration(kind = illustration, primaryColor = tint, sizeDp = 52)
            }
            Spacer(Modifier.padding(start = 14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.75f))
                Spacer(Modifier.padding(top = 8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(LabNavy700)
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(progressFraction.coerceIn(0f, 1f))
                                .height(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(tint)
                        )
                    }
                    Spacer(Modifier.padding(start = 8.dp))
                    MasteryChip(mastery = mastery)
                }
            }
        }
    }
}
