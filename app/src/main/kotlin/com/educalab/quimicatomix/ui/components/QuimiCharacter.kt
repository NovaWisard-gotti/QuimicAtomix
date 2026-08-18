package com.educalab.quimicatomix.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.theme.LabCoral500
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabTeal300
import com.educalab.quimicatomix.ui.theme.LabWhite

/** Estados de ánimo de Quimi, la mascota guía del laboratorio. */
enum class QuimiMood { NEUTRAL, HAPPY, THINKING, CELEBRATING, ENCOURAGING }

/**
 * Quimi: un átomo amistoso con gafas de laboratorio que guía al jugador. Aparece de forma
 * puntual (no interrumpe constantemente) para presentar misiones, celebrar avances o dar
 * una pista corta. El diseño evita rasgos "bebé": es un personaje curioso y aventurero.
 */
@Composable
fun QuimiCharacter(
    mood: QuimiMood = QuimiMood.NEUTRAL,
    modifier: Modifier = Modifier,
    sizeDp: Int = 96,
    animated: Boolean = true
) {
    val transition = rememberInfiniteTransition(label = "quimi")
    val bobOffset by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1400), repeatMode = RepeatMode.Reverse),
        label = "bob"
    )
    val orbitRotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(6000), repeatMode = RepeatMode.Restart),
        label = "orbit"
    )

    Canvas(modifier = modifier.size(sizeDp.dp)) {
        val w = size.width
        val h = size.height
        val bob = if (animated) bobOffset * h * 0.015f else 0f
        val cx = w * 0.5f
        val cy = h * 0.52f + bob

        // Orbitas (giran lentamente para dar sensación de vida)
        rotate(if (animated) orbitRotation else 0f, pivot = Offset(cx, cy)) {
            drawOval(
                color = LabTeal300.copy(alpha = 0.85f),
                topLeft = Offset(cx - w * 0.42f, cy - h * 0.18f),
                size = androidx.compose.ui.geometry.Size(w * 0.84f, h * 0.36f),
                style = Stroke(width = w * 0.025f)
            )
        }
        rotate(if (animated) -orbitRotation * 0.6f else 60f, pivot = Offset(cx, cy)) {
            drawOval(
                color = LabWhite.copy(alpha = 0.5f),
                topLeft = Offset(cx - w * 0.36f, cy - h * 0.24f),
                size = androidx.compose.ui.geometry.Size(w * 0.72f, h * 0.48f),
                style = Stroke(width = w * 0.02f)
            )
        }

        // Cuerpo (nucleo)
        drawCircle(LabCoral500, radius = w * 0.28f, center = Offset(cx, cy))
        drawCircle(LabWhite.copy(alpha = 0.18f), radius = w * 0.28f, center = Offset(cx - w * 0.08f, cy - h * 0.08f), style = Stroke(width = w * 0.01f))

        // Gafas de laboratorio
        val eyeY = cy - h * 0.02f
        drawCircle(LabWhite, radius = w * 0.1f, center = Offset(cx - w * 0.11f, eyeY))
        drawCircle(LabWhite, radius = w * 0.1f, center = Offset(cx + w * 0.11f, eyeY))
        drawLine(LabWhite, Offset(cx - w * 0.02f, eyeY), Offset(cx + w * 0.02f, eyeY), strokeWidth = w * 0.025f)

        // Ojos (expresión según mood)
        val eyeRadius = if (mood == QuimiMood.CELEBRATING) w * 0.02f else w * 0.035f
        drawCircle(LabInk, radius = eyeRadius, center = Offset(cx - w * 0.11f, eyeY))
        drawCircle(LabInk, radius = eyeRadius, center = Offset(cx + w * 0.11f, eyeY))

        // Boca (expresión según mood)
        val mouthPath = Path()
        val mouthY = cy + h * 0.14f
        when (mood) {
            QuimiMood.HAPPY, QuimiMood.CELEBRATING -> {
                mouthPath.moveTo(cx - w * 0.09f, mouthY)
                mouthPath.quadraticBezierTo(cx, mouthY + h * 0.09f, cx + w * 0.09f, mouthY)
            }
            QuimiMood.THINKING -> {
                mouthPath.moveTo(cx - w * 0.06f, mouthY + h * 0.02f)
                mouthPath.lineTo(cx + w * 0.08f, mouthY - h * 0.01f)
            }
            QuimiMood.ENCOURAGING -> {
                mouthPath.moveTo(cx - w * 0.07f, mouthY + h * 0.02f)
                mouthPath.quadraticBezierTo(cx, mouthY + h * 0.06f, cx + w * 0.07f, mouthY + h * 0.02f)
            }
            QuimiMood.NEUTRAL -> {
                mouthPath.moveTo(cx - w * 0.07f, mouthY)
                mouthPath.lineTo(cx + w * 0.07f, mouthY)
            }
        }
        drawPath(mouthPath, color = LabInk, style = Stroke(width = w * 0.025f, cap = StrokeCap.Round))
    }
}
