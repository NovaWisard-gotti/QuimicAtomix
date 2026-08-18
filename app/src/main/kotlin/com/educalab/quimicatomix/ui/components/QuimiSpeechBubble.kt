package com.educalab.quimicatomix.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabWhite

/**
 * Burbuja narrativa breve de Quimi. Se usa con moderación (introducir misión, celebrar,
 * explicar un error) y NUNCA con diálogos extensos, tal como exige la especificación.
 */
@Composable
fun QuimiSpeechBubble(
    text: String,
    mood: QuimiMood = QuimiMood.NEUTRAL,
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }),
        exit = fadeOut()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(LabNavy700)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuimiCharacter(mood = mood, sizeDp = 56, animated = true)
            Spacer(Modifier.padding(start = 10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = LabWhite,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
