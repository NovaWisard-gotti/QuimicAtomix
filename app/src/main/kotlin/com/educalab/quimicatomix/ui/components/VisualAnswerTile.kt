package com.educalab.quimicatomix.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabWhite
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Tarjeta grande e ilustrada usada como "opción de respuesta" en toda la app, en lugar de
 * texto plano: imagen (vía [LabIllustration]) + etiqueta, con estado seleccionado/deshabilitado.
 */
@Composable
fun VisualAnswerTile(
    label: String,
    kind: IllustrationKind,
    tint: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    AnswerTileVisual(
        label = label,
        kind = kind,
        tint = tint,
        selected = selected,
        enabled = enabled,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
    )
}

/** Solo la parte visual de la tarjeta, sin gesto propio: la usa [VisualAnswerTile] (con
 * tap) y [DraggableTile] (con arrastre) para no anidar dos detectores de gesto distintos. */
@Composable
private fun AnswerTileVisual(
    label: String,
    kind: IllustrationKind,
    tint: Color,
    selected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(84.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) tint.copy(alpha = 0.28f) else LabNavy700)
            .border(
                width = if (selected) 2.dp else 0.dp,
                color = if (selected) tint else Color.Transparent,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LabIllustration(kind = kind, primaryColor = tint, sizeDp = 48)
        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 4.dp))
        Text(
            label.replace('_', ' '),
            color = LabWhite.copy(alpha = if (enabled) 1f else 0.5f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

/** Registro compartido de zonas de destino: id -> rectángulo en coordenadas de ventana. */
typealias DropZoneRegistry = SnapshotStateMap<String, Rect>

@Composable
fun rememberDropZoneRegistry(): DropZoneRegistry = remember { mutableStateMapOf() }

fun Modifier.registerDropZone(id: String, registry: DropZoneRegistry): Modifier =
    this.onGloballyPositioned { coordinates -> registry[id] = coordinates.boundsInWindow() }

/**
 * Igual que [VisualAnswerTile] pero arrastrable de verdad: se puede tomar con el dedo y
 * soltar sobre cualquier zona registrada en [zones]. Si no cae sobre ninguna zona, vuelve a
 * su lugar con una animación. El resultado del soltar se reporta como el id de la zona (o
 * null si no acertó) a través de [onDropped].
 */
@Composable
fun DraggableTile(
    label: String,
    kind: IllustrationKind,
    tint: Color,
    zones: DropZoneRegistry,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onDropped: (zoneId: String?) -> Unit
) {
    val scope = rememberCoroutineScope()
    // Mientras se arrastra, el offset se lleva en estado PLANO (escritura síncrona, sin
    // corrutina) para que no haya ninguna condición de carrera: cada evento onDrag llega en
    // orden y actualiza el valor inmediatamente. El Animatable solo entra en juego UNA VEZ,
    // al soltar, para la animación de "volver a su lugar" (antes, cada evento de arrastre
    // lanzaba su propia corrutina para animar el offset, y con varias en vuelo a la vez
    // podían pisarse entre sí, perdiendo movimientos y dejando el tile en una posición
    // incorrecta — por eso a veces no aterrizaba en la casilla correcta o no se soltaba).
    var dragOffsetX by remember { mutableStateOf(0f) }
    var dragOffsetY by remember { mutableStateOf(0f) }
    val snapX = remember { Animatable(0f) }
    val snapY = remember { Animatable(0f) }
    var dragging by remember { mutableStateOf(false) }
    // Centro REAL en pantalla del tile, actualizado en cada pase de layout. Se coloca
    // onGloballyPositioned DESPUÉS de offset en la cadena de modificadores a propósito:
    // así refleja la posición ya desplazada mientras se arrastra, no la posición de reposo
    // (si fuera antes de offset, siempre reportaría la posición sin arrastrar y el
    // arrastre nunca acertaría ninguna zona).
    var liveCenter by remember { mutableStateOf<Offset?>(null) }

    val renderX = if (dragging) dragOffsetX else snapX.value
    val renderY = if (dragging) dragOffsetY else snapY.value

    fun releaseAndSnapBack() {
        val fromX = dragOffsetX
        val fromY = dragOffsetY
        dragOffsetX = 0f
        dragOffsetY = 0f
        scope.launch {
            snapX.snapTo(fromX)
            snapY.snapTo(fromY)
            val spec = spring<Float>(dampingRatio = Spring.DampingRatioMediumBouncy)
            launch { snapX.animateTo(0f, animationSpec = spec) }
            launch { snapY.animateTo(0f, animationSpec = spec) }
        }
    }

    Box(
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier.pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                dragOffsetX = 0f
                                dragOffsetY = 0f
                                dragging = true
                            },
                            onDragEnd = {
                                dragging = false
                                val center = liveCenter
                                val hitZone = center?.let { c ->
                                    zones.entries.firstOrNull { (_, rect) -> rect.contains(c) }?.key
                                }
                                releaseAndSnapBack()
                                onDropped(hitZone)
                            },
                            onDragCancel = {
                                dragging = false
                                releaseAndSnapBack()
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffsetX += dragAmount.x
                                dragOffsetY += dragAmount.y
                            }
                        )
                    }
                } else Modifier
            )
            .offset {
                IntOffset(renderX.roundToInt(), renderY.roundToInt())
            }
            .onGloballyPositioned { coordinates ->
                liveCenter = coordinates.boundsInWindow().center
            }
            .zIndex(if (dragging) 4f else 0f)
    ) {
        AnswerTileVisual(label = label, kind = kind, tint = tint, selected = dragging, enabled = enabled)
    }
}
