package com.educalab.quimicatomix.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.theme.LabCoral500
import com.educalab.quimicatomix.ui.theme.LabGold500
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabLime500
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabWhite

enum class FeedbackKind { CORRECT, PARTIAL, INCORRECT }

/**
 * Panel de retroalimentación tras responder un paso. NUNCA se limita a "Correcto/Incorrecto":
 * siempre añade una explicación educativa breve y ofrece continuar o reintentar sin castigo.
 */
@Composable
fun FeedbackPanel(
    kind: FeedbackKind,
    explanation: String,
    starsEarned: Int? = null,
    onContinue: () -> Unit,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var appeared by remember { mutableStateOf(false) }
    androidx.compose.runtime.LaunchedEffect(Unit) { appeared = true }
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (appeared) 1f else 0.9f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "feedbackScale"
    )

    val (accentColor, headline, mood) = when (kind) {
        FeedbackKind.CORRECT -> Triple(LabLime500, "¡Muy bien!", QuimiMood.HAPPY)
        FeedbackKind.PARTIAL -> Triple(LabGold500, "¡Casi lo tienes!", QuimiMood.ENCOURAGING)
        FeedbackKind.INCORRECT -> Triple(LabCoral500, "Vamos a intentarlo de nuevo", QuimiMood.ENCOURAGING)
    }

    Column(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(LabNavy800)
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            QuimiCharacter(mood = mood, sizeDp = 60)
            Spacer(Modifier.padding(start = 10.dp))
            Column {
                Text(headline, style = MaterialTheme.typography.titleLarge, color = accentColor, fontWeight = FontWeight.ExtraBold)
                if (starsEarned != null) {
                    StarsRow(starsEarned = starsEarned, starSizeDp = 22)
                }
            }
        }
        Spacer(Modifier.padding(top = 10.dp))
        Text(explanation, style = MaterialTheme.typography.bodyLarge, color = LabWhite)
        Spacer(Modifier.padding(top = 16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            if (onRetry != null && kind != FeedbackKind.CORRECT) {
                OutlinedButton(onClick = onRetry, modifier = Modifier.weight(1f)) {
                    Text("Reintentar")
                }
            }
            Button(
                onClick = onContinue,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = LabInk)
            ) {
                Text("Continuar", fontWeight = FontWeight.Bold)
            }
        }
    }
}
