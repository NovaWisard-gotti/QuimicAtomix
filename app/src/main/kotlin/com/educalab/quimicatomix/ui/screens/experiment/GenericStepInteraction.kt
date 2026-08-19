package com.educalab.quimicatomix.ui.screens.experiment

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.data.local.entity.ExperimentStep
import com.educalab.quimicatomix.data.local.entity.InteractionType
import com.educalab.quimicatomix.ui.components.DraggableTile
import com.educalab.quimicatomix.ui.components.IconCatalog
import com.educalab.quimicatomix.ui.components.ObservationAnimation
import com.educalab.quimicatomix.ui.components.VisualAnswerTile
import com.educalab.quimicatomix.ui.components.registerDropZone
import com.educalab.quimicatomix.ui.components.rememberDropZoneRegistry
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabWhite

/**
 * Interacción genérica de un paso de experimento: cada [InteractionType] se traduce en una
 * interacción visual real (arrastrar y soltar imágenes, animación de observación, tarjetas
 * ilustradas) en lugar de texto plano, reutilizando [VisualAnswerTile]/[DraggableTile] para
 * que la mejora aplique automáticamente a los 55 experimentos semilla de todos los temas.
 */
@Composable
fun GenericStepInteraction(step: ExperimentStep, onSubmit: (String) -> Unit) {
    val options = remember(step.id) { step.optionsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() } }

    when (step.interactionType) {
        InteractionType.ORDENAR -> OrderInteraction(step.id, options, onSubmit)
        InteractionType.CLASIFICAR -> {
            val groupCount = remember(step.id) { step.correctAnswerCsv.split("|").size.coerceAtLeast(2) }
            ClassifyInteraction(step.id, options, groupCount, onSubmit)
        }
        InteractionType.CONECTAR -> ConnectInteraction(step.id, step.correctAnswerCsv, onSubmit)
        InteractionType.OBSERVAR -> ObserveInteraction(step, options, onSubmit)
        InteractionType.SELECCION_IMAGEN -> {
            // Algunos pasos SELECCION_IMAGEN en realidad piden clasificar cada opción en una
            // categoría (p.ej. hielo/agua_liquida/vapor -> solido/liquido/gaseoso): el
            // vocabulario de correctAnswerCsv no comparte NINGÚN valor con optionsCsv y tiene
            // más de un elemento. En ese caso el enunciado dice "arrastra" y se necesita un
            // arrastre real a zonas fijas; el resto sigue siendo selección simple por toque.
            val labels = remember(step.id) { step.correctAnswerCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() } }
            val isClassifyByLabel = remember(step.id) { labels.size > 1 && labels.none { it in options } }
            if (isClassifyByLabel) {
                ClassifyToLabelsInteraction(step.id, options, labels, onSubmit)
            } else {
                SelectInteraction(options, onSubmit)
            }
        }
        else -> SelectInteraction(options, onSubmit)
    }
}

/** Layout simple en filas de tarjetas, sin desplazamiento (evita conflictos de gesto con el arrastre). */
@Composable
private fun TileRows(items: List<String>, itemsPerRow: Int = 3, content: @Composable (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.chunked(itemsPerRow).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowItems.forEach { item -> content(item) }
            }
        }
    }
}

@Composable
private fun OrderInteraction(stepId: Long, options: List<String>, onSubmit: (String) -> Unit) {
    var tray by remember(stepId) { mutableStateOf(options) }
    var slots by remember(stepId) { mutableStateOf(List<String?>(options.size) { null }) }
    val zones = rememberDropZoneRegistry()

    Column {
        Text("Arrastra cada elemento a su lugar, EN ORDEN:", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 10.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(androidx.compose.foundation.rememberScrollState())
        ) {
            slots.forEachIndexed { index, filled ->
                Box(
                    modifier = Modifier
                        .width(92.dp)
                        .height(124.dp)
                        .registerDropZone("slot_$index", zones)
                        .clip(RoundedCornerShape(16.dp))
                        .background(LabNavy800)
                        .border(width = 1.dp, color = LabNavy700, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (filled != null) {
                        VisualAnswerTile(
                            label = filled,
                            kind = IconCatalog.resolve(filled),
                            tint = IconCatalog.colorFor(filled),
                            selected = true,
                            onClick = {
                                slots = slots.toMutableList().also { it[index] = null }
                                tray = tray + filled
                            }
                        )
                    } else {
                        Text("${index + 1}", color = LabWhite.copy(alpha = 0.35f), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(Modifier.padding(top = 16.dp))
        if (tray.isNotEmpty()) {
            Text("Opciones disponibles:", style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.7f))
            Spacer(Modifier.padding(top = 8.dp))
        }
        TileRows(items = tray) { item ->
            DraggableTile(
                label = item,
                kind = IconCatalog.resolve(item),
                tint = IconCatalog.colorFor(item),
                zones = zones,
                onDropped = { zoneId ->
                    val index = zoneId?.removePrefix("slot_")?.toIntOrNull()
                    if (index != null && slots.getOrNull(index) == null) {
                        slots = slots.toMutableList().also { it[index] = item }
                        tray = tray - item
                    }
                }
            )
        }
        Spacer(Modifier.padding(top = 16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = {
                slots = List(options.size) { null }
                tray = options
            }) { Text("Reiniciar") }
            Button(
                onClick = { onSubmit(slots.joinToString(",") { it.orEmpty() }) },
                enabled = slots.none { it == null },
                colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
            ) { Text("Comprobar", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun ClassifyInteraction(stepId: Long, options: List<String>, groupCount: Int, onSubmit: (String) -> Unit) {
    var tray by remember(stepId) { mutableStateOf(options) }
    var assignment by remember(stepId) { mutableStateOf(mapOf<String, Int>()) }
    val zones = rememberDropZoneRegistry()

    fun unassign(item: String) {
        assignment = assignment - item
        tray = tray + item
    }

    Column {
        Text("Arrastra cada elemento a su grupo:", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            (0 until groupCount).forEach { groupIndex ->
                val groupItems = assignment.filterValues { it == groupIndex }.keys.toList()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .registerDropZone("group_$groupIndex", zones)
                        .clip(RoundedCornerShape(16.dp))
                        .background(LabNavy800)
                        .border(width = 1.dp, color = LabNavy700, shape = RoundedCornerShape(16.dp))
                        .padding(10.dp)
                ) {
                    Text("Grupo ${groupIndex + 1}", style = MaterialTheme.typography.labelLarge, color = LabWhite.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.padding(top = 6.dp))
                    if (groupItems.isEmpty()) {
                        Text("Suelta aquí", color = LabWhite.copy(alpha = 0.3f), style = MaterialTheme.typography.bodyMedium)
                    } else {
                        TileRows(items = groupItems, itemsPerRow = 3) { item ->
                            VisualAnswerTile(
                                label = item,
                                kind = IconCatalog.resolve(item),
                                tint = IconCatalog.colorFor(item),
                                selected = true,
                                onClick = { unassign(item) }
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.padding(top = 16.dp))
        if (tray.isNotEmpty()) {
            Text("Opciones disponibles:", style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.7f))
            Spacer(Modifier.padding(top = 8.dp))
        }
        TileRows(items = tray) { item ->
            DraggableTile(
                label = item,
                kind = IconCatalog.resolve(item),
                tint = IconCatalog.colorFor(item),
                zones = zones,
                onDropped = { zoneId ->
                    val groupIndex = zoneId?.removePrefix("group_")?.toIntOrNull()
                    if (groupIndex != null) {
                        assignment = assignment + (item to groupIndex)
                        tray = tray - item
                    }
                }
            )
        }
        Spacer(Modifier.padding(top = 16.dp))
        Button(
            onClick = {
                val groups = (0 until groupCount).map { g -> assignment.filterValues { it == g }.keys.joinToString(",") }
                onSubmit(groups.joinToString("|"))
            },
            enabled = assignment.size == options.size,
            colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
        ) { Text("Comprobar", fontWeight = FontWeight.Bold) }
    }
}

/** Arrastra cada opción a una de las zonas fijas (una por cada valor esperado). */
@Composable
private fun ClassifyToLabelsInteraction(stepId: Long, options: List<String>, labels: List<String>, onSubmit: (String) -> Unit) {
    var tray by remember(stepId) { mutableStateOf(options) }
    var assignment by remember(stepId) { mutableStateOf(mapOf<String, String>()) } // item -> label
    val zones = rememberDropZoneRegistry()

    Column {
        Text("Arrastra cada imagen al lugar que le corresponde:", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            labels.forEachIndexed { index, label ->
                val assignedItem = assignment.entries.firstOrNull { it.value == label }?.key
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .registerDropZone("label_$index", zones)
                        .clip(RoundedCornerShape(16.dp))
                        .background(LabNavy800)
                        .border(width = 1.dp, color = LabNavy700, shape = RoundedCornerShape(16.dp))
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        label.replace('_', ' ').replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelLarge,
                        color = LabWhite.copy(alpha = 0.75f),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(Modifier.padding(top = 6.dp))
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (assignedItem != null) {
                            VisualAnswerTile(
                                label = assignedItem,
                                kind = IconCatalog.resolve(assignedItem),
                                tint = IconCatalog.colorFor(assignedItem),
                                selected = true,
                                onClick = {
                                    assignment = assignment - assignedItem
                                    tray = tray + assignedItem
                                }
                            )
                        } else {
                            Text("Suelta aquí", color = LabWhite.copy(alpha = 0.3f), style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.padding(top = 16.dp))
        if (tray.isNotEmpty()) {
            Text("Opciones disponibles:", style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.7f))
            Spacer(Modifier.padding(top = 8.dp))
        }
        TileRows(items = tray) { item ->
            DraggableTile(
                label = item,
                kind = IconCatalog.resolve(item),
                tint = IconCatalog.colorFor(item),
                zones = zones,
                onDropped = { zoneId ->
                    val index = zoneId?.removePrefix("label_")?.toIntOrNull()
                    if (index != null) {
                        val label = labels[index]
                        val alreadyUsed = assignment.containsValue(label)
                        if (!alreadyUsed) {
                            assignment = assignment + (item to label)
                            tray = tray - item
                        }
                    }
                }
            )
        }
        Spacer(Modifier.padding(top = 16.dp))
        Button(
            onClick = { onSubmit(assignment.values.joinToString(",")) },
            enabled = tray.isEmpty() && assignment.size == labels.size,
            colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
        ) { Text("Comprobar", fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun ConnectInteraction(stepId: Long, correctAnswerCsv: String, onSubmit: (String) -> Unit) {
    val pairs = remember(stepId) { correctAnswerCsv.split(",").map { it.split("-") } }
    val lefts = remember(stepId) { pairs.map { it[0] }.shuffled() }
    val rights = remember(stepId) { pairs.map { it.getOrElse(1) { "" } }.shuffled() }

    var selectedLeft by remember(stepId) { mutableStateOf<String?>(null) }
    var connections by remember(stepId) { mutableStateOf(mapOf<String, String>()) }
    val itemCenters = remember(stepId) { mutableStateMapOf<String, Offset>() }
    var containerOrigin by remember { mutableStateOf(Offset.Zero) }

    Column {
        Text("Toca un elemento izquierdo y luego su pareja correcta:", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { containerOrigin = it.boundsInWindow().topLeft }
        ) {
            Row {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    lefts.forEach { left ->
                        val connected = connections.containsKey(left)
                        VisualAnswerTile(
                            label = left,
                            kind = IconCatalog.resolve(left),
                            tint = IconCatalog.colorFor(left),
                            selected = selectedLeft == left || connected,
                            modifier = Modifier
                                .padding(4.dp)
                                .onGloballyPositioned { itemCenters["L_$left"] = it.boundsInWindow().center },
                            onClick = { if (!connected) selectedLeft = left }
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    rights.forEach { right ->
                        val used = connections.containsValue(right)
                        VisualAnswerTile(
                            label = right,
                            kind = IconCatalog.resolve(right),
                            tint = IconCatalog.colorFor(right),
                            selected = used,
                            modifier = Modifier
                                .padding(4.dp)
                                .onGloballyPositioned { itemCenters["R_$right"] = it.boundsInWindow().center },
                            onClick = {
                                val left = selectedLeft
                                if (left != null && !used) {
                                    connections = connections + (left to right)
                                    selectedLeft = null
                                }
                            }
                        )
                    }
                }
            }
            Canvas(modifier = Modifier.matchParentSize()) {
                connections.forEach { (left, right) ->
                    val a = itemCenters["L_$left"]
                    val b = itemCenters["R_$right"]
                    if (a != null && b != null) {
                        drawLine(
                            color = LabTeal500,
                            start = a - containerOrigin,
                            end = b - containerOrigin,
                            strokeWidth = 6f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }
        Spacer(Modifier.padding(top = 12.dp))
        Button(
            onClick = { onSubmit(connections.entries.joinToString(",") { "${it.key}-${it.value}" }) },
            enabled = connections.size == pairs.size,
            colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
        ) { Text("Comprobar", fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun ObserveInteraction(step: ExperimentStep, options: List<String>, onSubmit: (String) -> Unit) {
    Column {
        ObservationAnimation(
            instructionText = step.instruction,
            optionsCsv = step.optionsCsv,
            correctAnswerCsv = step.correctAnswerCsv,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.padding(top = 12.dp))
        SelectInteraction(options, onSubmit)
    }
}

@Composable
private fun SelectInteraction(options: List<String>, onSubmit: (String) -> Unit) {
    var selected by remember(options) { mutableStateOf(setOf<String>()) }
    Column {
        Text("Selecciona tu respuesta:", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 10.dp))
        TileRows(items = options) { option ->
            VisualAnswerTile(
                label = option,
                kind = IconCatalog.resolve(option),
                tint = IconCatalog.colorFor(option),
                selected = option in selected,
                onClick = {
                    selected = if (option in selected) selected - option else selected + option
                }
            )
        }
        Spacer(Modifier.padding(top = 16.dp))
        Button(
            onClick = { onSubmit(selected.joinToString(",")) },
            enabled = selected.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
        ) { Text("Comprobar", fontWeight = FontWeight.Bold) }
    }
}
