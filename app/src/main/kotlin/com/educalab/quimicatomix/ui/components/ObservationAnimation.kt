package com.educalab.quimicatomix.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.theme.LabCoral500
import com.educalab.quimicatomix.ui.theme.LabTeal300
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabViolet500
import com.educalab.quimicatomix.ui.theme.LabWhite
import kotlin.math.roundToInt

private enum class AnimationFamily { PHASE_CHANGE, FIZZ, COLOR_SHIFT, ORBIT }

private fun classifyFamily(vararg texts: String): AnimationFamily {
    val t = texts.joinToString(" ").lowercase()
    val phaseWords = listOf("fusion", "evaporacion", "condensacion", "solidificacion", "hielo", "vapor", "derrit", "congela", "cera")
    val fizzWords = listOf("burbuja", "efervescen", "reaccion", "oxid", "espuma", "gas")
    val colorWords = listOf("color", "repollo", "rosado", "morado", "verde", "rojo", "azul", "indicador")
    return when {
        phaseWords.any { it in t } -> AnimationFamily.PHASE_CHANGE
        fizzWords.any { it in t } -> AnimationFamily.FIZZ
        colorWords.any { it in t } -> AnimationFamily.COLOR_SHIFT
        else -> AnimationFamily.ORBIT
    }
}

/** Color de la sustancia según palabras clave del propio texto, para que la animación de
 * cambio de fase se vea como la sustancia real (chocolate marrón, cera dorada...) en vez de
 * un color genérico sin relación con el enunciado. */
private fun substanceColor(text: String): Color {
    val t = text.lowercase()
    return when {
        "chocolate" in t -> Color(0xFF6B4226)
        "cera" in t || "vela" in t -> Color(0xFFE8B84B)
        "mantequilla" in t -> Color(0xFFF3D67A)
        "hierro" in t -> Color(0xFFB0B7BF)
        else -> LabTeal300
    }
}

/** Traduce un nombre de color en español al [Color] real, para que la animación de cambio
 * de color termine EXACTAMENTE en el color que dice la respuesta correcta (p.ej. "rosado"),
 * en vez de un degradado fijo que puede no coincidir con ninguna opción mostrada. */
private fun namedColor(text: String): Color? {
    val t = text.lowercase()
    return when {
        "rosad" in t || "rosa" in t -> Color(0xFFF06BAF)
        "rojo" in t -> Color(0xFFE53935)
        "verde" in t -> Color(0xFF4CAF50)
        "azul" in t -> Color(0xFF2E7DFF)
        "morad" in t || "violeta" in t -> LabViolet500
        "naranja" in t -> Color(0xFFFF8A34)
        "amarill" in t -> Color(0xFFFFC107)
        "marron" in t || "café" in t || "cafe" in t -> Color(0xFF6B4226)
        else -> null
    }
}

/** Cantidad pequeña mencionada en el texto (p.ej. "3" capas, "2" hidrógenos): se usa para
 * que la animación de órbitas dibuje EXACTAMENTE esa cantidad de satélites en vez de un
 * número fijo que puede no coincidir con lo que dice el enunciado. */
private fun mentionedCount(text: String): Int? {
    val n = text.trim().toIntOrNull()
    return if (n != null && n in 1..8) n else null
}

/**
 * Animación genérica (sin autoría por experimento) mostrada en pasos OBSERVAR: elige una de
 * cuatro "familias" según palabras clave del propio texto del paso, y se puede repetir con
 * un botón. No depende de datos nuevos: funciona sobre cualquier instrucción/opciones ya
 * existentes en el contenido semilla.
 */
@Composable
fun ObservationAnimation(
    instructionText: String,
    optionsCsv: String,
    correctAnswerCsv: String,
    modifier: Modifier = Modifier
) {
    val family = remember(instructionText, optionsCsv, correctAnswerCsv) {
        classifyFamily(instructionText, optionsCsv, correctAnswerCsv)
    }
    val tint = remember(instructionText, optionsCsv, correctAnswerCsv) {
        substanceColor("$instructionText $optionsCsv $correctAnswerCsv")
    }
    var playToken by remember { mutableStateOf(0) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
            when (family) {
                AnimationFamily.PHASE_CHANGE -> PhaseChangeAnimation(playToken, tint)
                AnimationFamily.FIZZ -> FizzAnimation()
                AnimationFamily.COLOR_SHIFT -> ColorShiftAnimation(playToken, correctAnswerCsv)
                AnimationFamily.ORBIT -> OrbitAnimation(correctAnswerCsv)
            }
        }
        TextButton(onClick = { playToken++ }) {
            Text("▶ Repetir animación", style = MaterialTheme.typography.labelLarge, color = LabTeal300, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * Un solo bloque sólido que se aplana y ensancha hasta volverse un charco (con un par de
 * gotas y un poco de vapor al final) — en vez de intercambiar íconos sueltos, es UNA forma
 * que cambia continuamente, para que de verdad se vea como algo derritiéndose.
 */
@Composable
private fun PhaseChangeAnimation(playToken: Int, tint: Color) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(playToken) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(durationMillis = 2600, easing = LinearEasing))
    }
    val p = progress.value

    Canvas(modifier = Modifier.size(140.dp)) {
        val w = size.width
        val h = size.height
        val baseY = h * 0.78f

        val blockWidth = w * (0.34f + 0.32f * p)
        val blockHeight = h * (0.44f - 0.32f * p)
        val cornerRadius = w * (0.05f + 0.14f * p)
        val left = (w - blockWidth) / 2f
        val top = baseY - blockHeight

        drawLine(
            color = LabWhite.copy(alpha = 0.25f),
            start = Offset(w * 0.12f, baseY),
            end = Offset(w * 0.88f, baseY),
            strokeWidth = 3f
        )

        drawRoundRect(
            color = tint,
            topLeft = Offset(left, top),
            size = Size(blockWidth, blockHeight),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )
        drawRoundRect(
            color = LabWhite.copy(alpha = 0.16f * (1f - p * 0.6f)),
            topLeft = Offset(left + blockWidth * 0.14f, top + blockHeight * 0.16f),
            size = Size(blockWidth * 0.28f, blockHeight * 0.3f),
            cornerRadius = CornerRadius(cornerRadius * 0.5f, cornerRadius * 0.5f)
        )

        if (p > 0.3f) {
            val dropAlpha = ((p - 0.3f) / 0.35f).coerceIn(0f, 1f)
            drawCircle(tint.copy(alpha = dropAlpha), radius = w * 0.02f, center = Offset(left + blockWidth * 0.1f, baseY + h * 0.02f))
            drawCircle(tint.copy(alpha = dropAlpha * 0.8f), radius = w * 0.015f, center = Offset(left + blockWidth * 0.9f, baseY + h * 0.03f))
        }

        if (p > 0.6f) {
            val steamP = ((p - 0.6f) / 0.4f).coerceIn(0f, 1f)
            val wisp = Path().apply {
                moveTo(w * 0.5f, top - h * 0.02f)
                quadraticBezierTo(w * 0.58f, top - h * (0.12f + steamP * 0.08f), w * 0.5f, top - h * (0.24f + steamP * 0.14f))
            }
            drawPath(wisp, color = LabWhite.copy(alpha = steamP * 0.55f), style = Stroke(width = 4f, cap = StrokeCap.Round))
        }
    }
}

@Composable
private fun FizzAnimation() {
    val transition = rememberInfiniteTransition(label = "fizz")
    Box(contentAlignment = Alignment.Center) {
        LabIllustration(kind = IllustrationKind.BUBBLE_JAR, primaryColor = LabTeal500, sizeDp = 100)
        repeat(5) { index ->
            val delayMs = index * 260
            val riseFraction by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    tween(1300, delayMillis = delayMs, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "bubble$index"
            )
            val xOffset = (index - 2) * 14f
            LabIllustration(
                kind = IllustrationKind.DROPLET,
                primaryColor = LabWhite,
                sizeDp = 12,
                modifier = Modifier.graphicsLayer {
                    alpha = (1f - riseFraction) * 0.9f
                    translationY = -60f * riseFraction
                    translationX = xOffset
                    scaleX = 0.6f + 0.4f * riseFraction
                    scaleY = 0.6f + 0.4f * riseFraction
                }
            )
        }
    }
}

@Composable
private fun ColorShiftAnimation(playToken: Int, correctAnswerCsv: String) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(playToken) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(durationMillis = 1800, easing = LinearEasing))
    }
    // Termina EXACTAMENTE en el color de la respuesta correcta (si se reconoce la palabra),
    // no en un degradado fijo que podía no coincidir con ninguna opción mostrada.
    val target = remember(correctAnswerCsv) { namedColor(correctAnswerCsv) ?: LabCoral500 }
    val mixed = lerpColor(LabViolet500, target, progress.value)
    LabIllustration(kind = IllustrationKind.BEAKER, primaryColor = mixed, sizeDp = 100)
}

private fun lerpColor(start: Color, end: Color, fraction: Float): Color {
    val f = fraction.coerceIn(0f, 1f)
    return Color(
        red = start.red + (end.red - start.red) * f,
        green = start.green + (end.green - start.green) * f,
        blue = start.blue + (end.blue - start.blue) * f,
        alpha = 1f
    )
}

/**
 * Dibuja exactamente [satelliteCount] puntos orbitando un núcleo central (derivado de
 * correctAnswerCsv, p.ej. "3" capas de electrones o "2" hidrógenos): antes mostraba una
 * molécula genérica con una cantidad fija de puntos que no coincidía con lo que decía el
 * enunciado.
 */
@Composable
private fun OrbitAnimation(correctAnswerCsv: String) {
    val satelliteCount = remember(correctAnswerCsv) { mentionedCount(correctAnswerCsv) ?: 3 }
    val transition = rememberInfiniteTransition(label = "orbitObservation")
    val rotationDeg by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(5200, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "orbitRotation"
    )
    Canvas(modifier = Modifier.size(140.dp)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val orbitRadiusX = w * 0.36f
        val orbitRadiusY = h * 0.36f

        drawCircle(
            color = LabWhite.copy(alpha = 0.2f),
            radius = orbitRadiusX,
            center = Offset(cx, cy),
            style = Stroke(width = 2f)
        )
        drawCircle(color = LabTeal500, radius = w * 0.11f, center = Offset(cx, cy))

        for (i in 0 until satelliteCount) {
            val angle = Math.toRadians((rotationDeg + i * (360f / satelliteCount)).toDouble())
            val sx = cx + (orbitRadiusX * kotlin.math.cos(angle)).toFloat()
            val sy = cy + (orbitRadiusY * kotlin.math.sin(angle)).toFloat()
            drawCircle(color = LabCoral500, radius = w * 0.045f, center = Offset(sx, sy))
        }
    }
}

private enum class ConfigureFamily { THERMOMETER, INTENSITY }

private fun classifyConfigureFamily(options: List<String>): ConfigureFamily {
    val t = options.joinToString(" ").lowercase()
    val thermoWords = listOf("frio", "caliente", "templado", "congela", "temperatura")
    return if (thermoWords.any { it in t }) ConfigureFamily.THERMOMETER else ConfigureFamily.INTENSITY
}

/**
 * Visual en vivo para pasos CONFIGURAR: antes el control deslizante solo cambiaba una
 * palabra de texto, sin mostrar ningún efecto — igual que "mueve el control" prometía algo
 * que no pasaba. Ahora, según a qué se refieran las opciones, se ve un termómetro cuyo
 * nivel/color sigue la posición del control (temperatura) o un frasco cuya cantidad de
 * burbujas y velocidad aumentan con la posición (intensidad/cantidad) — reactivo en vivo
 * mientras se arrastra, sin necesidad de un botón de "reproducir".
 */
@Composable
fun ConfigureAnimation(options: List<String>, selectedIndex: Int, modifier: Modifier = Modifier) {
    val maxIndex = (options.size - 1).coerceAtLeast(1)
    val fraction = (selectedIndex.toFloat() / maxIndex).coerceIn(0f, 1f)
    val family = remember(options) { classifyConfigureFamily(options) }
    Box(modifier = modifier.size(140.dp), contentAlignment = Alignment.Center) {
        when (family) {
            ConfigureFamily.THERMOMETER -> ThermometerVisual(fraction)
            ConfigureFamily.INTENSITY -> IntensityBubblesVisual(fraction)
        }
    }
}

@Composable
private fun ThermometerVisual(fraction: Float) {
    val animatedFraction by animateFloatAsState(targetValue = fraction, label = "thermo")
    val mercuryColor = lerpColor(Color(0xFF5EC8E8), Color(0xFFE5533D), animatedFraction)

    Canvas(modifier = Modifier.size(140.dp)) {
        val w = size.width
        val h = size.height
        val tubeWidth = w * 0.14f
        val tubeTop = h * 0.12f
        val tubeBottom = h * 0.72f
        val bulbRadius = w * 0.14f
        val bulbCenter = Offset(w * 0.5f, h * 0.8f)
        val tubeLeft = w * 0.5f - tubeWidth / 2f

        drawRoundRect(
            color = LabWhite.copy(alpha = 0.25f),
            topLeft = Offset(tubeLeft, tubeTop),
            size = Size(tubeWidth, tubeBottom - tubeTop),
            cornerRadius = CornerRadius(tubeWidth / 2f, tubeWidth / 2f),
            style = Stroke(width = 3f)
        )
        drawCircle(LabWhite.copy(alpha = 0.25f), radius = bulbRadius, center = bulbCenter, style = Stroke(width = 3f))

        val fillHeight = (tubeBottom - tubeTop) * (0.15f + 0.75f * animatedFraction)
        drawRoundRect(
            color = mercuryColor,
            topLeft = Offset(tubeLeft, tubeBottom - fillHeight),
            size = Size(tubeWidth, fillHeight),
            cornerRadius = CornerRadius(tubeWidth / 2f, tubeWidth / 2f)
        )
        drawCircle(mercuryColor, radius = bulbRadius * 0.82f, center = bulbCenter)

        if (animatedFraction < 0.2f) {
            val iceAlpha = 1f - animatedFraction / 0.2f
            drawLine(
                LabWhite.copy(alpha = iceAlpha * 0.8f),
                Offset(bulbCenter.x - bulbRadius * 1.4f, bulbCenter.y),
                Offset(bulbCenter.x - bulbRadius * 0.6f, bulbCenter.y),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
            drawLine(
                LabWhite.copy(alpha = iceAlpha * 0.8f),
                Offset(bulbCenter.x + bulbRadius * 0.6f, bulbCenter.y),
                Offset(bulbCenter.x + bulbRadius * 1.4f, bulbCenter.y),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
        if (animatedFraction > 0.8f) {
            val heatAlpha = (animatedFraction - 0.8f) / 0.2f
            val wave = Path().apply {
                moveTo(w * 0.5f, tubeTop - h * 0.02f)
                quadraticBezierTo(w * 0.58f, tubeTop - h * 0.1f, w * 0.5f, tubeTop - h * 0.18f)
            }
            drawPath(wave, color = mercuryColor.copy(alpha = heatAlpha * 0.6f), style = Stroke(width = 3f, cap = StrokeCap.Round))
        }
    }
}

@Composable
private fun IntensityBubblesVisual(fraction: Float) {
    val bubbleCount = (1 + (fraction * 6).roundToInt()).coerceIn(1, 7)
    val durationMs = (2000 - (1300 * fraction)).roundToInt().coerceAtLeast(600)
    val transition = rememberInfiniteTransition(label = "intensity")
    Box(contentAlignment = Alignment.Center) {
        LabIllustration(kind = IllustrationKind.BUBBLE_JAR, primaryColor = LabTeal500, sizeDp = 100)
        repeat(bubbleCount) { index ->
            val delayMs = index * (durationMs / (bubbleCount + 1)).coerceAtLeast(80)
            val riseFraction by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    tween(durationMs, delayMillis = delayMs, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "intensityBubble$index"
            )
            val xOffset = (index - bubbleCount / 2) * 12f
            LabIllustration(
                kind = IllustrationKind.DROPLET,
                primaryColor = LabWhite,
                sizeDp = 12,
                modifier = Modifier.graphicsLayer {
                    alpha = (1f - riseFraction) * 0.9f
                    translationY = -60f * riseFraction
                    translationX = xOffset
                    scaleX = 0.6f + 0.4f * riseFraction
                    scaleY = 0.6f + 0.4f * riseFraction
                }
            )
        }
    }
}
