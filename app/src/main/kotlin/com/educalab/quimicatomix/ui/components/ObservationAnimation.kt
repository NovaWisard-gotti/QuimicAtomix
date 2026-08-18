package com.educalab.quimicatomix.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.theme.LabCoral500
import com.educalab.quimicatomix.ui.theme.LabTeal300
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabViolet500
import com.educalab.quimicatomix.ui.theme.LabWhite

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
    var playToken by remember { mutableStateOf(0) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
            when (family) {
                AnimationFamily.PHASE_CHANGE -> PhaseChangeAnimation(playToken)
                AnimationFamily.FIZZ -> FizzAnimation()
                AnimationFamily.COLOR_SHIFT -> ColorShiftAnimation(playToken)
                AnimationFamily.ORBIT -> OrbitAnimation()
            }
        }
        TextButton(onClick = { playToken++ }) {
            Text("▶ Repetir animación", style = MaterialTheme.typography.labelLarge, color = LabTeal300, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PhaseChangeAnimation(playToken: Int) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(playToken) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(durationMillis = 2200, easing = LinearEasing))
    }
    val p = progress.value
    val iceAlpha = (1f - p / 0.5f).coerceIn(0f, 1f)
    val liquidAlpha = ((p - 0.2f) / 0.5f).coerceIn(0f, 1f)
    val steamProgress = ((p - 0.55f) / 0.45f).coerceIn(0f, 1f)

    Box(contentAlignment = Alignment.Center) {
        LabIllustration(
            kind = IllustrationKind.STEAM_CLOUD,
            primaryColor = LabWhite,
            sizeDp = 60,
            modifier = Modifier.graphicsLayer {
                alpha = steamProgress * 0.85f
                translationY = -70f * steamProgress
            }
        )
        LabIllustration(
            kind = IllustrationKind.BUBBLE_JAR,
            primaryColor = LabTeal500,
            sizeDp = 92,
            modifier = Modifier.graphicsLayer { alpha = liquidAlpha }
        )
        LabIllustration(
            kind = IllustrationKind.ICE_CUBE,
            primaryColor = LabTeal300,
            sizeDp = 92,
            modifier = Modifier.graphicsLayer {
                alpha = iceAlpha
                val squash = 1f - 0.35f * (1f - iceAlpha)
                scaleX = squash + 0.15f
                scaleY = squash
            }
        )
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
private fun ColorShiftAnimation(playToken: Int) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(playToken) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(durationMillis = 1800, easing = LinearEasing))
    }
    val mixed = lerpColor(LabViolet500, LabCoral500, progress.value)
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

@Composable
private fun OrbitAnimation() {
    val transition = rememberInfiniteTransition(label = "orbitObservation")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4200, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "orbitRotation"
    )
    Box(
        modifier = Modifier.graphicsLayer { rotationZ = rotation },
        contentAlignment = Alignment.Center
    ) {
        LabIllustration(kind = IllustrationKind.MOLECULE, primaryColor = LabTeal500, sizeDp = 100)
    }
}
