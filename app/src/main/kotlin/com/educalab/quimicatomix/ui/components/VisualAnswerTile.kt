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
import androidx.compose.ui.geometry.Size
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
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    var restingOrigin by remember { mutableStateOf(Offset.Zero) }
    var tileSize by remember { mutableStateOf(Size.Zero) }
    var dragging by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInWindow()
                tileSize = bounds.size
                restingOrigin = Offset(bounds.left - offsetX.value, bounds.top - offsetY.value)
            }
            .then(
                if (enabled) {
                    Modifier.pointerInput(zones.size) {
                        detectDragGestures(
                            onDragStart = { dragging = true },
                            onDragEnd = {
                                dragging = false
                                val center = Offset(
                                    restingOrigin.x + offsetX.value + tileSize.width / 2f,
                                    restingOrigin.y + offsetY.value + tileSize.height / 2f
                                )
                                val hitZone = zones.entries.firstOrNull { (_, rect) -> rect.contains(center) }?.key
                                val snapSpec = spring<Float>(dampingRatio = Spring.DampingRatioMediumBouncy)
                                scope.launch { offsetX.animateTo(0f, animationSpec = snapSpec) }
                                scope.launch { offsetY.animateTo(0f, animationSpec = snapSpec) }
                                onDropped(hitZone)
                            },
                            onDragCancel = {
                                dragging = false
                                scope.launch { offsetX.snapTo(0f) }
                                scope.launch { offsetY.snapTo(0f) }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                scope.launch { offsetX.snapTo(offsetX.value + dragAmount.x) }
                                scope.launch { offsetY.snapTo(offsetY.value + dragAmount.y) }
                            }
                        )
                    }
                } else Modifier
            )
            .offset {
                IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt())
            }
            .zIndex(if (dragging) 4f else 0f)
    ) {
        AnswerTileVisual(label = label, kind = kind, tint = tint, selected = dragging, enabled = enabled)
    }
}
