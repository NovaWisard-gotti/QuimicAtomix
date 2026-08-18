package com.educalab.quimicatomix.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.theme.LabGold500
import com.educalab.quimicatomix.ui.theme.LabNavy700

/** Fila de 0-3 estrellas con una pequeña animación de aparición. Nunca solo decorativo: es feedback real de desempeño. */
@Composable
fun StarsRow(starsEarned: Int, modifier: Modifier = Modifier, starSizeDp: Int = 28) {
    Row(modifier = modifier) {
        for (i in 1..3) {
            val filled = i <= starsEarned
            val scale by animateFloatAsState(
                targetValue = if (filled) 1f else 0.85f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "star$i"
            )
            LabIllustration(
                kind = IllustrationKind.STAR_BADGE,
                primaryColor = if (filled) LabGold500 else LabNavy700,
                modifier = Modifier
                    .size(starSizeDp.dp)
                    .graphicsLayer { scaleX = scale; scaleY = scale }
            )
        }
    }
}
