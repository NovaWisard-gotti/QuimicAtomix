package com.educalab.quimicatomix.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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

/** Definición de los 8 avatares locales seleccionables en el perfil (sin fotos reales). */
data class AvatarSpec(val primary: Color, val accessory: AvatarAccessory)
enum class AvatarAccessory { STAR, DROP, LEAF, BOLT, DIAMOND, MOON, COMET, FLAME }

val AVATAR_SPECS = listOf(
    AvatarSpec(LabTeal500, AvatarAccessory.STAR),
    AvatarSpec(LabBlue500, AvatarAccessory.DROP),
    AvatarSpec(LabViolet500, AvatarAccessory.LEAF),
    AvatarSpec(LabCoral500, AvatarAccessory.BOLT),
    AvatarSpec(LabGold500, AvatarAccessory.DIAMOND),
    AvatarSpec(Color(0xFF4DD0E1), AvatarAccessory.MOON),
    AvatarSpec(LabLime500, AvatarAccessory.COMET),
    AvatarSpec(Color(0xFFFF8A65), AvatarAccessory.FLAME),
)

@Composable
fun AvatarIllustration(avatarId: Int, modifier: Modifier = Modifier, sizeDp: Int = 72) {
    val spec = AVATAR_SPECS.getOrElse(avatarId) { AVATAR_SPECS[0] }
    Canvas(modifier = modifier.size(sizeDp.dp)) {
        val w = size.width; val h = size.height
        val cx = w * 0.5f; val cy = h * 0.5f
        drawCircle(spec.primary.copy(alpha = 0.22f), radius = w * 0.48f, center = Offset(cx, cy))
        drawCircle(spec.primary, radius = w * 0.3f, center = Offset(cx, cy))
        // ojos simples
        drawCircle(LabWhite, radius = w * 0.075f, center = Offset(cx - w * 0.11f, cy - h * 0.02f))
        drawCircle(LabWhite, radius = w * 0.075f, center = Offset(cx + w * 0.11f, cy - h * 0.02f))
        drawCircle(LabInk, radius = w * 0.032f, center = Offset(cx - w * 0.11f, cy - h * 0.02f))
        drawCircle(LabInk, radius = w * 0.032f, center = Offset(cx + w * 0.11f, cy - h * 0.02f))
        val mouth = Path().apply {
            moveTo(cx - w * 0.08f, cy + h * 0.12f)
            quadraticBezierTo(cx, cy + h * 0.19f, cx + w * 0.08f, cy + h * 0.12f)
        }
        drawPath(mouth, color = LabInk, style = Stroke(width = w * 0.02f, cap = StrokeCap.Round))
        drawAccessory(spec.accessory, w, h, spec.primary)
    }
}

private fun DrawScope.drawAccessory(accessory: AvatarAccessory, w: Float, h: Float, color: Color) {
    val ax = w * 0.78f; val ay = h * 0.24f; val r = w * 0.1f
    when (accessory) {
        AvatarAccessory.STAR -> drawSimpleStar(ax, ay, r, LabWhite)
        AvatarAccessory.DROP -> {
            val path = Path().apply {
                moveTo(ax, ay - r)
                quadraticBezierTo(ax + r, ay + r * 0.4f, ax, ay + r)
                quadraticBezierTo(ax - r, ay + r * 0.4f, ax, ay - r)
                close()
            }
            drawPath(path, color = LabWhite)
        }
        AvatarAccessory.LEAF -> {
            val path = Path().apply {
                moveTo(ax, ay - r)
                quadraticBezierTo(ax + r * 1.3f, ay, ax, ay + r)
                quadraticBezierTo(ax - r * 1.3f, ay, ax, ay - r)
                close()
            }
            drawPath(path, color = LabWhite)
        }
        AvatarAccessory.BOLT -> {
            val path = Path().apply {
                moveTo(ax + r * 0.3f, ay - r)
                lineTo(ax - r * 0.5f, ay + r * 0.15f)
                lineTo(ax, ay + r * 0.15f)
                lineTo(ax - r * 0.3f, ay + r)
                lineTo(ax + r * 0.5f, ay - r * 0.1f)
                lineTo(ax, ay - r * 0.1f)
                close()
            }
            drawPath(path, color = LabWhite)
        }
        AvatarAccessory.DIAMOND -> {
            val path = Path().apply {
                moveTo(ax, ay - r); lineTo(ax + r, ay); lineTo(ax, ay + r); lineTo(ax - r, ay); close()
            }
            drawPath(path, color = LabWhite)
        }
        AvatarAccessory.MOON -> {
            drawCircle(LabWhite, radius = r, center = Offset(ax, ay))
            drawCircle(color, radius = r, center = Offset(ax + r * 0.5f, ay - r * 0.2f))
        }
        AvatarAccessory.COMET -> {
            drawCircle(LabWhite, radius = r * 0.6f, center = Offset(ax, ay))
            drawLine(LabWhite.copy(alpha = 0.7f), Offset(ax, ay), Offset(ax - r * 1.4f, ay + r * 1.1f), strokeWidth = r * 0.35f, cap = StrokeCap.Round)
        }
        AvatarAccessory.FLAME -> {
            val path = Path().apply {
                moveTo(ax, ay - r)
                quadraticBezierTo(ax + r, ay, ax + r * 0.3f, ay + r)
                quadraticBezierTo(ax, ay + r * 0.6f, ax - r * 0.3f, ay + r)
                quadraticBezierTo(ax - r, ay, ax, ay - r)
                close()
            }
            drawPath(path, color = LabWhite)
        }
    }
}

private fun DrawScope.drawSimpleStar(cx: Float, cy: Float, radius: Float, color: Color) {
    val path = Path()
    val points = 5
    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) radius else radius * 0.45f
        val angle = Math.PI / points * i - Math.PI / 2
        val x = cx + (r * kotlin.math.cos(angle)).toFloat()
        val y = cy + (r * kotlin.math.sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color = color)
}
