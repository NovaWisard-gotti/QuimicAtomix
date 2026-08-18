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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabWhite
import kotlin.math.cos
import kotlin.math.sin

/**
 * Dibuja una ilustración vectorial procedural del catálogo [IllustrationKind] usando
 * Compose Canvas. No depende de ningún recurso raster ni de red: todo el arte se genera
 * a partir de formas geométricas parametrizadas por color, por lo que un mismo "molde"
 * (p.ej. BEAKER) puede representar decenas de sustancias distintas simplemente cambiando
 * el color principal.
 */
@Composable
fun LabIllustration(
    kind: IllustrationKind,
    modifier: Modifier = Modifier,
    primaryColor: Color,
    accentColor: Color = LabWhite,
    sizeDp: Int = 64
) {
    Canvas(modifier = modifier.size(sizeDp.dp)) {
        val w = size.width
        val h = size.height
        when (kind) {
            IllustrationKind.BEAKER -> drawBeaker(w, h, primaryColor, accentColor)
            IllustrationKind.FLASK -> drawFlask(w, h, primaryColor, accentColor)
            IllustrationKind.TEST_TUBE -> drawTestTube(w, h, primaryColor, accentColor)
            IllustrationKind.DROPLET -> drawDroplet(w, h, primaryColor, accentColor)
            IllustrationKind.MAGNET -> drawMagnet(w, h, primaryColor, accentColor)
            IllustrationKind.FUNNEL -> drawFunnel(w, h, primaryColor, accentColor)
            IllustrationKind.THERMOMETER -> drawThermometer(w, h, primaryColor, accentColor)
            IllustrationKind.MICROSCOPE -> drawMicroscope(w, h, primaryColor, accentColor)
            IllustrationKind.GLOVES -> drawGloves(w, h, primaryColor, accentColor)
            IllustrationKind.GOGGLES -> drawGoggles(w, h, primaryColor, accentColor)
            IllustrationKind.LAB_COAT -> drawLabCoat(w, h, primaryColor, accentColor)
            IllustrationKind.ATOM -> drawAtom(w, h, primaryColor, accentColor)
            IllustrationKind.MOLECULE -> drawMolecule(w, h, primaryColor, accentColor)
            IllustrationKind.CRYSTAL_GRAIN -> drawCrystal(w, h, primaryColor, accentColor)
            IllustrationKind.LEAF -> drawLeaf(w, h, primaryColor, accentColor)
            IllustrationKind.MEDAL -> drawMedal(w, h, primaryColor, accentColor)
            IllustrationKind.SHIELD -> drawShield(w, h, primaryColor, accentColor)
            IllustrationKind.STAR_BADGE -> drawStarBadge(w, h, primaryColor, accentColor)
            IllustrationKind.TROPHY -> drawTrophy(w, h, primaryColor, accentColor)
            IllustrationKind.WARNING_TRIANGLE -> drawWarningTriangle(w, h, primaryColor, accentColor)
            IllustrationKind.FLAME -> drawFlame(w, h, primaryColor, accentColor)
            IllustrationKind.SKULL -> drawSkull(w, h, primaryColor, accentColor)
            IllustrationKind.CORROSIVE -> drawCorrosive(w, h, primaryColor, accentColor)
            IllustrationKind.RECYCLE -> drawRecycle(w, h, primaryColor, accentColor)
            IllustrationKind.FIRST_AID -> drawFirstAid(w, h, primaryColor, accentColor)
            IllustrationKind.BOOK -> drawBook(w, h, primaryColor, accentColor)
            IllustrationKind.MAP -> drawMap(w, h, primaryColor, accentColor)
            IllustrationKind.ICE_CUBE -> drawIceCube(w, h, primaryColor, accentColor)
            IllustrationKind.STEAM_CLOUD -> drawSteamCloud(w, h, primaryColor, accentColor)
            IllustrationKind.BUBBLE_JAR -> drawBubbleJar(w, h, primaryColor, accentColor)
            IllustrationKind.PERIODIC_CARD -> drawPeriodicCard(w, h, primaryColor, accentColor)
        }
    }
}

private fun DrawScope.drawBeaker(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.28f, h * 0.12f)
        lineTo(w * 0.28f, h * 0.4f)
        lineTo(w * 0.12f, h * 0.85f)
        quadraticBezierTo(w * 0.1f, h * 0.95f, w * 0.22f, h * 0.95f)
        lineTo(w * 0.78f, h * 0.95f)
        quadraticBezierTo(w * 0.9f, h * 0.95f, w * 0.88f, h * 0.85f)
        lineTo(w * 0.72f, h * 0.4f)
        lineTo(w * 0.72f, h * 0.12f)
        close()
    }
    drawPath(path, color = accent.copy(alpha = 0.16f))
    drawPath(path, color = accent, style = Stroke(width = w * 0.045f, cap = StrokeCap.Round))
    // liquido
    val liquid = Path().apply {
        moveTo(w * 0.16f, h * 0.9f)
        lineTo(w * 0.2f, h * 0.55f)
        lineTo(w * 0.8f, h * 0.55f)
        lineTo(w * 0.84f, h * 0.9f)
        close()
    }
    drawPath(liquid, color = primary.copy(alpha = 0.85f))
    // linea de boca
    drawLine(accent, Offset(w * 0.22f, h * 0.12f), Offset(w * 0.78f, h * 0.12f), strokeWidth = w * 0.04f, cap = StrokeCap.Round)
    // burbujas
    drawCircle(accent.copy(alpha = 0.7f), radius = w * 0.03f, center = Offset(w * 0.42f, h * 0.72f))
    drawCircle(accent.copy(alpha = 0.5f), radius = w * 0.022f, center = Offset(w * 0.58f, h * 0.65f))
}

private fun DrawScope.drawFlask(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.42f, h * 0.1f)
        lineTo(w * 0.42f, h * 0.38f)
        lineTo(w * 0.15f, h * 0.88f)
        quadraticBezierTo(w * 0.1f, h * 0.96f, w * 0.24f, h * 0.96f)
        lineTo(w * 0.76f, h * 0.96f)
        quadraticBezierTo(w * 0.9f, h * 0.96f, w * 0.85f, h * 0.88f)
        lineTo(w * 0.58f, h * 0.38f)
        lineTo(w * 0.58f, h * 0.1f)
        close()
    }
    drawPath(path, color = accent.copy(alpha = 0.14f))
    drawPath(path, color = accent, style = Stroke(width = w * 0.045f, cap = StrokeCap.Round))
    val liquid = Path().apply {
        moveTo(w * 0.22f, h * 0.9f)
        lineTo(w * 0.32f, h * 0.62f)
        lineTo(w * 0.68f, h * 0.62f)
        lineTo(w * 0.78f, h * 0.9f)
        close()
    }
    drawPath(liquid, color = primary.copy(alpha = 0.85f))
    drawLine(accent, Offset(w * 0.4f, h * 0.1f), Offset(w * 0.6f, h * 0.1f), strokeWidth = w * 0.045f, cap = StrokeCap.Round)
}

private fun DrawScope.drawTestTube(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.38f, h * 0.08f)
        lineTo(w * 0.38f, h * 0.72f)
        quadraticBezierTo(w * 0.38f, h * 0.94f, w * 0.5f, h * 0.94f)
        quadraticBezierTo(w * 0.62f, h * 0.94f, w * 0.62f, h * 0.72f)
        lineTo(w * 0.62f, h * 0.08f)
    }
    drawPath(path, color = accent, style = Stroke(width = w * 0.045f, cap = StrokeCap.Round))
    val liquid = Path().apply {
        moveTo(w * 0.4f, h * 0.9f)
        lineTo(w * 0.4f, h * 0.5f)
        lineTo(w * 0.6f, h * 0.5f)
        lineTo(w * 0.6f, h * 0.9f)
        quadraticBezierTo(w * 0.6f, h * 0.94f, w * 0.5f, h * 0.94f)
        quadraticBezierTo(w * 0.4f, h * 0.94f, w * 0.4f, h * 0.9f)
        close()
    }
    drawPath(liquid, color = primary.copy(alpha = 0.85f))
    drawLine(accent, Offset(w * 0.3f, h * 0.08f), Offset(w * 0.7f, h * 0.08f), strokeWidth = w * 0.05f, cap = StrokeCap.Round)
}

private fun DrawScope.drawDroplet(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.5f, h * 0.08f)
        quadraticBezierTo(w * 0.86f, h * 0.55f, w * 0.5f, h * 0.92f)
        quadraticBezierTo(w * 0.14f, h * 0.55f, w * 0.5f, h * 0.08f)
        close()
    }
    drawPath(path, color = primary)
    drawCircle(accent.copy(alpha = 0.55f), radius = w * 0.07f, center = Offset(w * 0.4f, h * 0.55f))
}

private fun DrawScope.drawMagnet(w: Float, h: Float, primary: Color, accent: Color) {
    val stroke = Stroke(width = w * 0.16f, cap = StrokeCap.Round)
    drawArc(
        color = primary,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(w * 0.2f, h * 0.1f),
        size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.6f),
        style = stroke
    )
    drawLine(primary, Offset(w * 0.2f, h * 0.4f), Offset(w * 0.2f, h * 0.85f), strokeWidth = w * 0.16f, cap = StrokeCap.Round)
    drawLine(primary, Offset(w * 0.8f, h * 0.4f), Offset(w * 0.8f, h * 0.85f), strokeWidth = w * 0.16f, cap = StrokeCap.Round)
    drawRect(accent, topLeft = Offset(w * 0.12f, h * 0.78f), size = androidx.compose.ui.geometry.Size(w * 0.2f, h * 0.14f))
    drawRect(accent, topLeft = Offset(w * 0.68f, h * 0.78f), size = androidx.compose.ui.geometry.Size(w * 0.2f, h * 0.14f))
}

private fun DrawScope.drawFunnel(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.12f, h * 0.14f)
        lineTo(w * 0.88f, h * 0.14f)
        lineTo(w * 0.56f, h * 0.6f)
        lineTo(w * 0.56f, h * 0.92f)
        lineTo(w * 0.44f, h * 0.92f)
        lineTo(w * 0.44f, h * 0.6f)
        close()
    }
    drawPath(path, color = accent.copy(alpha = 0.18f))
    drawPath(path, color = accent, style = Stroke(width = w * 0.04f, cap = StrokeCap.Round))
    drawCircle(primary, radius = w * 0.05f, center = Offset(w * 0.35f, h * 0.24f))
    drawCircle(primary, radius = w * 0.04f, center = Offset(w * 0.55f, h * 0.2f))
    drawCircle(primary, radius = w * 0.045f, center = Offset(w * 0.65f, h * 0.28f))
}

private fun DrawScope.drawThermometer(w: Float, h: Float, primary: Color, accent: Color) {
    drawRoundRect(accent, topLeft = Offset(w * 0.42f, h * 0.08f), size = androidx.compose.ui.geometry.Size(w * 0.16f, h * 0.62f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.08f))
    drawCircle(accent, radius = w * 0.16f, center = Offset(w * 0.5f, h * 0.82f))
    drawRoundRect(primary, topLeft = Offset(w * 0.46f, h * 0.3f), size = androidx.compose.ui.geometry.Size(w * 0.08f, h * 0.42f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.04f))
    drawCircle(primary, radius = w * 0.12f, center = Offset(w * 0.5f, h * 0.82f))
}

private fun DrawScope.drawMicroscope(w: Float, h: Float, primary: Color, accent: Color) {
    drawLine(accent, Offset(w * 0.3f, h * 0.9f), Offset(w * 0.7f, h * 0.9f), strokeWidth = w * 0.05f, cap = StrokeCap.Round)
    drawLine(accent, Offset(w * 0.4f, h * 0.9f), Offset(w * 0.4f, h * 0.55f), strokeWidth = w * 0.05f, cap = StrokeCap.Round)
    val path = Path().apply {
        moveTo(w * 0.4f, h * 0.55f)
        lineTo(w * 0.68f, h * 0.2f)
        lineTo(w * 0.6f, h * 0.12f)
        lineTo(w * 0.3f, h * 0.42f)
    }
    drawPath(path, color = primary, style = Stroke(width = w * 0.07f, cap = StrokeCap.Round))
    drawCircle(accent, radius = w * 0.06f, center = Offset(w * 0.63f, h * 0.16f))
}

private fun DrawScope.drawGloves(w: Float, h: Float, primary: Color, accent: Color) {
    for (i in 0..4) {
        val fx = w * (0.22f + i * 0.14f)
        drawRoundRect(primary, topLeft = Offset(fx, h * 0.08f), size = androidx.compose.ui.geometry.Size(w * 0.1f, h * (if (i == 2) 0.34f else 0.26f)), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.05f))
    }
    drawRoundRect(primary, topLeft = Offset(w * 0.16f, h * 0.32f), size = androidx.compose.ui.geometry.Size(w * 0.68f, h * 0.58f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.16f))
    drawLine(accent, Offset(w * 0.16f, h * 0.5f), Offset(w * 0.84f, h * 0.5f), strokeWidth = w * 0.025f)
}

private fun DrawScope.drawGoggles(w: Float, h: Float, primary: Color, accent: Color) {
    drawCircle(accent, radius = w * 0.22f, center = Offset(w * 0.3f, h * 0.5f))
    drawCircle(accent, radius = w * 0.22f, center = Offset(w * 0.7f, h * 0.5f))
    drawCircle(primary, radius = w * 0.15f, center = Offset(w * 0.3f, h * 0.5f))
    drawCircle(primary, radius = w * 0.15f, center = Offset(w * 0.7f, h * 0.5f))
    drawLine(accent, Offset(w * 0.42f, h * 0.5f), Offset(w * 0.58f, h * 0.5f), strokeWidth = w * 0.05f)
    drawLine(accent, Offset(w * 0.08f, h * 0.42f), Offset(w * 0.16f, h * 0.4f), strokeWidth = w * 0.04f, cap = StrokeCap.Round)
    drawLine(accent, Offset(w * 0.92f, h * 0.42f), Offset(w * 0.84f, h * 0.4f), strokeWidth = w * 0.04f, cap = StrokeCap.Round)
}

private fun DrawScope.drawLabCoat(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.38f, h * 0.08f)
        lineTo(w * 0.62f, h * 0.08f)
        lineTo(w * 0.7f, h * 0.22f)
        lineTo(w * 0.86f, h * 0.3f)
        lineTo(w * 0.78f, h * 0.46f)
        lineTo(w * 0.72f, h * 0.4f)
        lineTo(w * 0.72f, h * 0.92f)
        lineTo(w * 0.28f, h * 0.92f)
        lineTo(w * 0.28f, h * 0.4f)
        lineTo(w * 0.22f, h * 0.46f)
        lineTo(w * 0.14f, h * 0.3f)
        lineTo(w * 0.3f, h * 0.22f)
        close()
    }
    drawPath(path, color = accent)
    drawLine(primary, Offset(w * 0.5f, h * 0.3f), Offset(w * 0.5f, h * 0.86f), strokeWidth = w * 0.02f)
    drawCircle(primary, radius = w * 0.025f, center = Offset(w * 0.5f, h * 0.42f))
    drawCircle(primary, radius = w * 0.025f, center = Offset(w * 0.5f, h * 0.55f))
}

private fun DrawScope.drawAtom(w: Float, h: Float, primary: Color, accent: Color) {
    val cx = w * 0.5f; val cy = h * 0.5f
    val orbitStroke = Stroke(width = w * 0.035f)
    for (angleDeg in listOf(-18f, 42f, 102f)) {
        rotate(angleDeg) {
            drawOval(
                color = accent.copy(alpha = 0.85f),
                topLeft = Offset(cx - w * 0.36f, cy - h * 0.16f),
                size = androidx.compose.ui.geometry.Size(w * 0.72f, h * 0.32f),
                style = orbitStroke
            )
        }
    }
    drawCircle(primary, radius = w * 0.16f, center = Offset(cx, cy))
    drawCircle(accent, radius = w * 0.045f, center = Offset(cx, cy - h * 0.32f))
    drawCircle(accent, radius = w * 0.045f, center = Offset(cx + w * 0.3f, cy + h * 0.08f))
    drawCircle(accent, radius = w * 0.045f, center = Offset(cx - w * 0.3f, cy + h * 0.14f))
}

private fun DrawScope.drawMolecule(w: Float, h: Float, primary: Color, accent: Color) {
    val centers = listOf(
        Offset(w * 0.5f, h * 0.4f),
        Offset(w * 0.24f, h * 0.72f),
        Offset(w * 0.76f, h * 0.72f)
    )
    drawLine(accent, centers[0], centers[1], strokeWidth = w * 0.045f, cap = StrokeCap.Round)
    drawLine(accent, centers[0], centers[2], strokeWidth = w * 0.045f, cap = StrokeCap.Round)
    drawCircle(primary, radius = w * 0.16f, center = centers[0])
    drawCircle(accent, radius = w * 0.12f, center = centers[1])
    drawCircle(accent, radius = w * 0.12f, center = centers[2])
}

private fun DrawScope.drawCrystal(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.5f, h * 0.08f)
        lineTo(w * 0.82f, h * 0.38f)
        lineTo(w * 0.68f, h * 0.92f)
        lineTo(w * 0.32f, h * 0.92f)
        lineTo(w * 0.18f, h * 0.38f)
        close()
    }
    drawPath(path, color = primary)
    drawLine(accent.copy(alpha = 0.6f), Offset(w * 0.5f, h * 0.08f), Offset(w * 0.5f, h * 0.92f), strokeWidth = w * 0.02f)
    drawLine(accent.copy(alpha = 0.6f), Offset(w * 0.18f, h * 0.38f), Offset(w * 0.82f, h * 0.38f), strokeWidth = w * 0.02f)
}

private fun DrawScope.drawLeaf(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.5f, h * 0.1f)
        quadraticBezierTo(w * 0.95f, h * 0.35f, w * 0.5f, h * 0.92f)
        quadraticBezierTo(w * 0.05f, h * 0.35f, w * 0.5f, h * 0.1f)
        close()
    }
    drawPath(path, color = primary)
    drawLine(accent, Offset(w * 0.5f, h * 0.18f), Offset(w * 0.5f, h * 0.85f), strokeWidth = w * 0.025f)
}

private fun DrawScope.drawMedal(w: Float, h: Float, primary: Color, accent: Color) {
    drawLine(accent, Offset(w * 0.35f, h * 0.05f), Offset(w * 0.5f, h * 0.4f), strokeWidth = w * 0.1f)
    drawLine(accent, Offset(w * 0.65f, h * 0.05f), Offset(w * 0.5f, h * 0.4f), strokeWidth = w * 0.1f)
    drawCircle(primary, radius = w * 0.3f, center = Offset(w * 0.5f, h * 0.62f))
    drawCircle(accent, radius = w * 0.3f, center = Offset(w * 0.5f, h * 0.62f), style = Stroke(width = w * 0.035f))
    drawStarPath(w * 0.5f, h * 0.62f, w * 0.15f, accent)
}

private fun DrawScope.drawShield(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.5f, h * 0.06f)
        lineTo(w * 0.86f, h * 0.2f)
        lineTo(w * 0.86f, h * 0.5f)
        quadraticBezierTo(w * 0.86f, h * 0.82f, w * 0.5f, h * 0.96f)
        quadraticBezierTo(w * 0.14f, h * 0.82f, w * 0.14f, h * 0.5f)
        lineTo(w * 0.14f, h * 0.2f)
        close()
    }
    drawPath(path, color = primary)
    drawPath(path, color = accent, style = Stroke(width = w * 0.03f))
    drawCheckmark(w * 0.5f, h * 0.5f, w * 0.18f, accent)
}

private fun DrawScope.drawStarBadge(w: Float, h: Float, primary: Color, accent: Color) {
    drawCircle(primary, radius = w * 0.42f, center = Offset(w * 0.5f, h * 0.5f))
    drawStarPath(w * 0.5f, h * 0.5f, w * 0.28f, accent)
}

private fun DrawScope.drawTrophy(w: Float, h: Float, primary: Color, accent: Color) {
    drawRoundRect(primary, topLeft = Offset(w * 0.32f, h * 0.12f), size = androidx.compose.ui.geometry.Size(w * 0.36f, h * 0.4f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.1f))
    drawArc(accent, startAngle = -90f, sweepAngle = 180f, useCenter = false, topLeft = Offset(w * 0.1f, h * 0.14f), size = androidx.compose.ui.geometry.Size(w * 0.24f, h * 0.24f), style = Stroke(width = w * 0.035f))
    drawArc(accent, startAngle = -90f, sweepAngle = -180f, useCenter = false, topLeft = Offset(w * 0.66f, h * 0.14f), size = androidx.compose.ui.geometry.Size(w * 0.24f, h * 0.24f), style = Stroke(width = w * 0.035f))
    drawRect(accent, topLeft = Offset(w * 0.44f, h * 0.5f), size = androidx.compose.ui.geometry.Size(w * 0.12f, h * 0.18f))
    drawRoundRect(accent, topLeft = Offset(w * 0.28f, h * 0.68f), size = androidx.compose.ui.geometry.Size(w * 0.44f, h * 0.12f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.03f))
}

private fun DrawScope.drawWarningTriangle(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.5f, h * 0.08f)
        lineTo(w * 0.92f, h * 0.88f)
        lineTo(w * 0.08f, h * 0.88f)
        close()
    }
    drawPath(path, color = primary)
    drawPath(path, color = accent, style = Stroke(width = w * 0.035f))
    drawRoundRect(accent, topLeft = Offset(w * 0.47f, h * 0.38f), size = androidx.compose.ui.geometry.Size(w * 0.06f, h * 0.24f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.03f))
    drawCircle(accent, radius = w * 0.035f, center = Offset(w * 0.5f, h * 0.72f))
}

private fun DrawScope.drawFlame(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.5f, h * 0.06f)
        quadraticBezierTo(w * 0.85f, h * 0.4f, w * 0.62f, h * 0.6f)
        quadraticBezierTo(w * 0.7f, h * 0.42f, w * 0.5f, h * 0.32f)
        quadraticBezierTo(w * 0.4f, h * 0.55f, w * 0.48f, h * 0.6f)
        quadraticBezierTo(w * 0.2f, h * 0.5f, w * 0.3f, h * 0.2f)
        quadraticBezierTo(w * 0.2f, h * 0.75f, w * 0.5f, h * 0.94f)
        quadraticBezierTo(w * 0.8f, h * 0.75f, w * 0.68f, h * 0.5f)
        quadraticBezierTo(w * 0.6f, h * 0.62f, w * 0.5f, h * 0.06f)
        close()
    }
    drawPath(path, color = primary)
    drawCircle(accent.copy(alpha = 0.8f), radius = w * 0.08f, center = Offset(w * 0.5f, h * 0.7f))
}

private fun DrawScope.drawSkull(w: Float, h: Float, primary: Color, accent: Color) {
    drawCircle(primary, radius = w * 0.32f, center = Offset(w * 0.5f, h * 0.42f))
    drawRoundRect(primary, topLeft = Offset(w * 0.32f, h * 0.55f), size = androidx.compose.ui.geometry.Size(w * 0.36f, h * 0.28f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.08f))
    drawCircle(accent, radius = w * 0.07f, center = Offset(w * 0.38f, h * 0.4f))
    drawCircle(accent, radius = w * 0.07f, center = Offset(w * 0.62f, h * 0.4f))
    val path = Path().apply {
        moveTo(w * 0.5f, h * 0.42f)
        lineTo(w * 0.45f, h * 0.55f)
        lineTo(w * 0.55f, h * 0.55f)
        close()
    }
    drawPath(path, color = accent)
}

private fun DrawScope.drawCorrosive(w: Float, h: Float, primary: Color, accent: Color) {
    drawRoundRect(accent, topLeft = Offset(w * 0.56f, h * 0.1f), size = androidx.compose.ui.geometry.Size(w * 0.3f, h * 0.22f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.04f))
    drawCircle(primary, radius = w * 0.03f, center = Offset(w * 0.6f, h * 0.4f))
    drawCircle(primary, radius = w * 0.04f, center = Offset(w * 0.66f, h * 0.55f))
    val hand = Path().apply {
        moveTo(w * 0.1f, h * 0.7f)
        quadraticBezierTo(w * 0.05f, h * 0.9f, w * 0.3f, h * 0.94f)
        quadraticBezierTo(w * 0.55f, h * 0.9f, w * 0.5f, h * 0.68f)
        quadraticBezierTo(w * 0.45f, h * 0.55f, w * 0.35f, h * 0.62f)
        close()
    }
    drawPath(hand, color = accent)
}

private fun DrawScope.drawRecycle(w: Float, h: Float, primary: Color, accent: Color) {
    for (angle in listOf(0f, 120f, 240f)) {
        rotate(angle, pivot = Offset(w * 0.5f, h * 0.5f)) {
            val path = Path().apply {
                moveTo(w * 0.5f, h * 0.5f)
                lineTo(w * 0.5f, h * 0.18f)
                lineTo(w * 0.62f, h * 0.3f)
            }
            drawPath(path, color = primary, style = Stroke(width = w * 0.06f, cap = StrokeCap.Round))
        }
    }
}

private fun DrawScope.drawFirstAid(w: Float, h: Float, primary: Color, accent: Color) {
    drawRoundRect(primary, topLeft = Offset(w * 0.12f, h * 0.24f), size = androidx.compose.ui.geometry.Size(w * 0.76f, h * 0.6f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.1f))
    drawRoundRect(accent, topLeft = Offset(w * 0.34f, h * 0.14f), size = androidx.compose.ui.geometry.Size(w * 0.32f, h * 0.16f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.05f))
    drawRect(accent, topLeft = Offset(w * 0.44f, h * 0.38f), size = androidx.compose.ui.geometry.Size(w * 0.12f, h * 0.32f))
    drawRect(accent, topLeft = Offset(w * 0.32f, h * 0.48f), size = androidx.compose.ui.geometry.Size(w * 0.36f, h * 0.12f))
}

private fun DrawScope.drawBook(w: Float, h: Float, primary: Color, accent: Color) {
    drawRoundRect(primary, topLeft = Offset(w * 0.14f, h * 0.14f), size = androidx.compose.ui.geometry.Size(w * 0.72f, h * 0.72f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.04f))
    drawLine(accent, Offset(w * 0.5f, h * 0.16f), Offset(w * 0.5f, h * 0.84f), strokeWidth = w * 0.025f)
    for (i in 0..2) {
        drawLine(accent.copy(alpha = 0.7f), Offset(w * 0.22f, h * (0.32f + i * 0.14f)), Offset(w * 0.42f, h * (0.32f + i * 0.14f)), strokeWidth = w * 0.02f)
        drawLine(accent.copy(alpha = 0.7f), Offset(w * 0.58f, h * (0.32f + i * 0.14f)), Offset(w * 0.78f, h * (0.32f + i * 0.14f)), strokeWidth = w * 0.02f)
    }
}

private fun DrawScope.drawMap(w: Float, h: Float, primary: Color, accent: Color) {
    val path = Path().apply {
        moveTo(w * 0.12f, h * 0.2f); lineTo(w * 0.4f, h * 0.1f); lineTo(w * 0.62f, h * 0.22f)
        lineTo(w * 0.9f, h * 0.12f); lineTo(w * 0.9f, h * 0.82f); lineTo(w * 0.62f, h * 0.92f)
        lineTo(w * 0.4f, h * 0.8f); lineTo(w * 0.12f, h * 0.9f); close()
    }
    drawPath(path, color = accent.copy(alpha = 0.16f))
    drawPath(path, color = accent, style = Stroke(width = w * 0.03f))
    drawLine(accent.copy(alpha = 0.5f), Offset(w * 0.4f, h * 0.1f), Offset(w * 0.4f, h * 0.8f), strokeWidth = w * 0.015f)
    drawLine(accent.copy(alpha = 0.5f), Offset(w * 0.62f, h * 0.22f), Offset(w * 0.62f, h * 0.92f), strokeWidth = w * 0.015f)
    drawCircle(primary, radius = w * 0.05f, center = Offset(w * 0.52f, h * 0.5f))
}

private fun DrawScope.drawIceCube(w: Float, h: Float, primary: Color, accent: Color) {
    drawRoundRect(primary.copy(alpha = 0.85f), topLeft = Offset(w * 0.2f, h * 0.2f), size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.6f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.06f))
    drawLine(accent.copy(alpha = 0.7f), Offset(w * 0.28f, h * 0.28f), Offset(w * 0.5f, h * 0.5f), strokeWidth = w * 0.02f)
    drawLine(accent.copy(alpha = 0.7f), Offset(w * 0.5f, h * 0.5f), Offset(w * 0.72f, h * 0.28f), strokeWidth = w * 0.02f)
}

private fun DrawScope.drawSteamCloud(w: Float, h: Float, primary: Color, accent: Color) {
    drawCircle(primary.copy(alpha = 0.8f), radius = w * 0.18f, center = Offset(w * 0.34f, h * 0.55f))
    drawCircle(primary.copy(alpha = 0.8f), radius = w * 0.22f, center = Offset(w * 0.56f, h * 0.5f))
    drawCircle(primary.copy(alpha = 0.8f), radius = w * 0.16f, center = Offset(w * 0.72f, h * 0.58f))
}

private fun DrawScope.drawBubbleJar(w: Float, h: Float, primary: Color, accent: Color) {
    drawRoundRect(accent.copy(alpha = 0.16f), topLeft = Offset(w * 0.2f, h * 0.24f), size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.66f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.1f))
    drawRoundRect(accent, topLeft = Offset(w * 0.2f, h * 0.24f), size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.66f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.1f), style = Stroke(width = w * 0.03f))
    drawCircle(primary, radius = w * 0.06f, center = Offset(w * 0.38f, h * 0.65f))
    drawCircle(primary, radius = w * 0.04f, center = Offset(w * 0.58f, h * 0.55f))
    drawCircle(primary, radius = w * 0.05f, center = Offset(w * 0.5f, h * 0.75f))
    drawRoundRect(accent, topLeft = Offset(w * 0.32f, h * 0.1f), size = androidx.compose.ui.geometry.Size(w * 0.36f, h * 0.14f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.04f))
}

private fun DrawScope.drawPeriodicCard(w: Float, h: Float, primary: Color, accent: Color) {
    drawRoundRect(primary, topLeft = Offset(w * 0.18f, h * 0.12f), size = androidx.compose.ui.geometry.Size(w * 0.64f, h * 0.76f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.08f))
    drawLine(accent, Offset(w * 0.3f, h * 0.36f), Offset(w * 0.7f, h * 0.36f), strokeWidth = w * 0.02f)
}

private fun DrawScope.drawStarPath(cx: Float, cy: Float, radius: Float, color: Color) {
    val path = Path()
    val points = 5
    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) radius else radius * 0.42f
        val angle = Math.PI / points * i - Math.PI / 2
        val x = cx + (r * cos(angle)).toFloat()
        val y = cy + (r * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color = color)
}

private fun DrawScope.drawCheckmark(cx: Float, cy: Float, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(cx - size, cy)
        lineTo(cx - size * 0.2f, cy + size * 0.7f)
        lineTo(cx + size, cy - size * 0.6f)
    }
    drawPath(path, color = color, style = Stroke(width = size * 0.28f, cap = StrokeCap.Round))
}
