package com.educalab.quimicatomix.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.theme.LabGold300
import com.educalab.quimicatomix.ui.theme.LabGold500
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabWhite

/** Barra de experiencia animada con nivel actual. Progreso derivado siempre de XP real. */
@Composable
fun XpBar(level: Int, progressWithinLevel: Float, modifier: Modifier = Modifier) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressWithinLevel.coerceIn(0f, 1f),
        animationSpec = tween(700),
        label = "xpProgress"
    )
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Text("Nivel $level", style = MaterialTheme.typography.labelLarge, color = LabWhite, fontWeight = FontWeight.Bold)
            Text("${(animatedProgress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium, color = LabGold300)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .padding(top = 4.dp)
                .clip(RoundedCornerShape(50))
                .background(LabNavy700)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(14.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Brush.horizontalGradient(listOf(LabGold500, LabGold300)))
            )
        }
    }
}
