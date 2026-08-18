package com.educalab.quimicatomix.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.theme.LabBlue500
import com.educalab.quimicatomix.ui.theme.LabCoral500
import com.educalab.quimicatomix.ui.theme.LabGold500
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabLime500
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabViolet500
import com.educalab.quimicatomix.ui.theme.LabWhite

/**
 * Definición de los 10 avatares locales seleccionables en el perfil (sin fotos reales).
 * Cada uno tiene una silueta de cabeza propia (no solo un color distinto sobre la misma
 * forma) para que se distingan claramente entre sí a simple vista.
 */
enum class AvatarShape { ORBIT, DROP, LEAF, BOLT, DIAMOND, MOON, STAR, FLAME, BUBBLE, CRYSTAL }
data class AvatarSpec(val primary: Color, val shape: AvatarShape)

val AVATAR_SPECS = listOf(
    AvatarSpec(LabTeal500, AvatarShape.ORBIT),
    AvatarSpec(LabBlue500, AvatarShape.DROP),
    AvatarSpec(LabViolet500, AvatarShape.LEAF),
    AvatarSpec(LabCoral500, AvatarShape.BOLT),
    AvatarSpec(LabGold500, AvatarShape.DIAMOND),
    AvatarSpec(Color(0xFF4DD0E1), AvatarShape.MOON),
    AvatarSpec(LabLime500, AvatarShape.STAR),
    AvatarSpec(Color(0xFFFF8A65), AvatarShape.FLAME),
    AvatarSpec(Color(0xFFF06BC7), AvatarShape.BUBBLE),
    AvatarSpec(Color(0xFF7FE0D0), AvatarShape.CRYSTAL),
)

@Composable
fun AvatarIllustration(avatarId: Int, modifier: Modifier = Modifier, sizeDp: Int = 72) {
    val spec = AVATAR_SPECS.getOrElse(avatarId) { AVATAR_SPECS[0] }
    Canvas(modifier = modifier.size(sizeDp.dp)) {
        val w = size.width; val h = size.height
        val cx = w * 0.5f; val cy = h * 0.5f

        drawHaloFor(spec.shape, cx, cy, w, h, spec.primary)
        val facePath = headPathFor(spec.shape, cx, cy, w, h)
        drawPath(facePath, color = spec.primary)

        val (eyeY, eyeSpread, mouthY) = anchorsFor(spec.shape, cy, w, h)
        drawCircle(LabWhite, radius = w * 0.075f, center = Offset(cx - eyeSpread, eyeY))
        drawCircle(LabWhite, radius = w * 0.075f, center = Offset(cx + eyeSpread, eyeY))
        drawCircle(LabInk, radius = w * 0.032f, center = Offset(cx - eyeSpread, eyeY))
        drawCircle(LabInk, radius = w * 0.032f, center = Offset(cx + eyeSpread, eyeY))
        val mouth = Path().apply {
            moveTo(cx - w * 0.08f, mouthY)
            quadraticBezierTo(cx, mouthY + h * 0.07f, cx + w * 0.08f, mouthY)
        }
        drawPath(mouth, color = LabInk, style = Stroke(width = w * 0.02f, cap = StrokeCap.Round))

        drawShapeFlourish(spec.shape, cx, cy, w, h)
    }
}

private fun DrawScope.drawHaloFor(shape: AvatarShape, cx: Float, cy: Float, w: Float, h: Float, color: Color) {
    when (shape) {
        AvatarShape.BUBBLE -> {
            drawCircle(color.copy(alpha = 0.18f), radius = w * 0.5f, center = Offset(cx, cy))
        }
        else -> drawCircle(color.copy(alpha = 0.2f), radius = w * 0.48f, center = Offset(cx, cy))
    }
}

/** Devuelve (eyeY, eyeSpread, mouthY) adaptados a cada silueta. */
private fun anchorsFor(shape: AvatarShape, cy: Float, w: Float, h: Float): Triple<Float, Float, Float> {
    val eyeSpread = w * 0.11f
    return when (shape) {
        AvatarShape.DROP -> Triple(cy + h * 0.02f, eyeSpread, cy + h * 0.16f)
        AvatarShape.LEAF -> Triple(cy - h * 0.01f, eyeSpread * 0.9f, cy + h * 0.13f)
        AvatarShape.FLAME -> Triple(cy + h * 0.06f, eyeSpread * 0.85f, cy + h * 0.19f)
        AvatarShape.MOON -> Triple(cy - h * 0.04f, eyeSpread * 0.8f, cy + h * 0.09f)
        AvatarShape.STAR -> Triple(cy - h * 0.03f, eyeSpread, cy + h * 0.11f)
        AvatarShape.DIAMOND -> Triple(cy - h * 0.02f, eyeSpread * 0.85f, cy + h * 0.12f)
        AvatarShape.BUBBLE -> Triple(cy - h * 0.05f, eyeSpread, cy + h * 0.08f)
        AvatarShape.CRYSTAL -> Triple(cy - h * 0.03f, eyeSpread * 0.85f, cy + h * 0.11f)
        else -> Triple(cy - h * 0.02f, eyeSpread, cy + h * 0.12f)
    }
}

private fun headPathFor(shape: AvatarShape, cx: Float, cy: Float, w: Float, h: Float): Path {
    val r = w * 0.3f
    return when (shape) {
        AvatarShape.ORBIT -> Path().apply { addOval(androidx.compose.ui.geometry.Rect(Offset(cx, cy), r)) }
        AvatarShape.DROP -> Path().apply {
            moveTo(cx, cy - r * 1.25f)
            quadraticBezierTo(cx + r * 1.15f, cy + r * 0.25f, cx, cy + r * 1.05f)
            quadraticBezierTo(cx - r * 1.15f, cy + r * 0.25f, cx, cy - r * 1.25f)
            close()
        }
        AvatarShape.LEAF -> Path().apply {
            moveTo(cx, cy - r * 1.15f)
            quadraticBezierTo(cx + r * 1.35f, cy - r * 0.2f, cx, cy + r * 1.15f)
            quadraticBezierTo(cx - r * 1.35f, cy - r * 0.2f, cx, cy - r * 1.15f)
            close()
        }
        AvatarShape.BOLT -> polygonPath(cx, cy, r * 1.08f, sides = 6, rotationDeg = -90f)
        AvatarShape.DIAMOND -> Path().apply {
            moveTo(cx, cy - r * 1.2f)
            lineTo(cx + r * 1.05f, cy)
            lineTo(cx, cy + r * 1.2f)
            lineTo(cx - r * 1.05f, cy)
            close()
        }
        AvatarShape.MOON -> {
            val full = Path().apply { addOval(androidx.compose.ui.geometry.Rect(Offset(cx, cy), r)) }
            val bite = Path().apply { addOval(androidx.compose.ui.geometry.Rect(Offset(cx + r * 0.62f, cy - r * 0.28f), r * 0.82f)) }
            Path().apply { op(full, bite, PathOperation.Difference) }
        }
        AvatarShape.STAR -> starPath(cx, cy, r * 1.25f, r * 0.55f, points = 5)
        AvatarShape.FLAME -> Path().apply {
            moveTo(cx, cy - r * 1.3f)
            quadraticBezierTo(cx + r * 1.1f, cy - r * 0.2f, cx + r * 0.45f, cy + r * 0.6f)
            quadraticBezierTo(cx + r * 0.25f, cy + r * 1.15f, cx, cy + r * 1.05f)
            quadraticBezierTo(cx - r * 0.25f, cy + r * 1.15f, cx - r * 0.45f, cy + r * 0.6f)
            quadraticBezierTo(cx - r * 1.1f, cy - r * 0.2f, cx, cy - r * 1.3f)
            close()
        }
        AvatarShape.BUBBLE -> Path().apply {
            addOval(androidx.compose.ui.geometry.Rect(Offset(cx, cy - r * 0.1f), r * 0.98f))
        }
        AvatarShape.CRYSTAL -> polygonPath(cx, cy, r * 1.12f, sides = 6, rotationDeg = -30f)
    }
}

private fun DrawScope.drawShapeFlourish(shape: AvatarShape, cx: Float, cy: Float, w: Float, h: Float) {
    when (shape) {
        AvatarShape.ORBIT -> {
            val r = w * 0.3f
            drawOval(
                color = LabWhite.copy(alpha = 0.55f),
                topLeft = Offset(cx - r * 1.5f, cy - r * 0.55f),
                size = androidx.compose.ui.geometry.Size(r * 3f, r * 1.1f),
                style = Stroke(width = w * 0.018f)
            )
        }
        AvatarShape.BUBBLE -> {
            val r = w * 0.3f
            drawCircle(LabWhite.copy(alpha = 0.35f), radius = r * 0.32f, center = Offset(cx + r * 0.85f, cy - r * 0.55f))
            drawCircle(LabWhite.copy(alpha = 0.25f), radius = r * 0.2f, center = Offset(cx - r * 0.8f, cy + r * 0.65f))
        }
        AvatarShape.CRYSTAL -> {
            drawLine(LabWhite.copy(alpha = 0.4f), Offset(cx, cy - h * 0.16f), Offset(cx, cy + h * 0.1f), strokeWidth = w * 0.012f)
        }
        AvatarShape.STAR -> {
            drawCircle(LabWhite.copy(alpha = 0.5f), radius = w * 0.035f, center = Offset(cx + w * 0.24f, cy - h * 0.22f))
        }
        else -> {}
    }
}

private fun polygonPath(cx: Float, cy: Float, radius: Float, sides: Int, rotationDeg: Float): Path {
    val path = Path()
    val rot = Math.toRadians(rotationDeg.toDouble())
    for (i in 0 until sides) {
        val angle = rot + 2 * Math.PI / sides * i
        val x = cx + (radius * kotlin.math.cos(angle)).toFloat()
        val y = cy + (radius * kotlin.math.sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

private fun starPath(cx: Float, cy: Float, outerRadius: Float, innerRadius: Float, points: Int): Path {
    val path = Path()
    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) outerRadius else innerRadius
        val angle = Math.PI / points * i - Math.PI / 2
        val x = cx + (r * kotlin.math.cos(angle)).toFloat()
        val y = cy + (r * kotlin.math.sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}
