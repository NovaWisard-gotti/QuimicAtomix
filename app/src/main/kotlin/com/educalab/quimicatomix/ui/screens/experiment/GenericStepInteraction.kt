package com.educalab.quimicatomix.ui.screens.experiment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.data.local.entity.ExperimentStep
import com.educalab.quimicatomix.data.local.entity.InteractionType
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabWhite

/**
 * Interacción genérica de un paso de experimento. Traduce cada [InteractionType] en un
 * gesto de TOQUE real (seleccionar, ordenar por toques sucesivos, clasificar en grupos,
 * conectar pares) -sustituyendo el arrastre físico por una interacción táctil equivalente
 * y fiable en cualquier tamaño de pantalla-. Se documenta como simplificación deliberada
 * en docs/MANUAL_TECNICO.md.
 */
@Composable
fun GenericStepInteraction(step: ExperimentStep, onSubmit: (String) -> Unit) {
    val options = remember(step.id) { step.optionsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() } }

    when (step.interactionType) {
        InteractionType.ORDENAR -> OrderInteraction(options, onSubmit)
        InteractionType.CLASIFICAR -> {
            val groupCount = remember(step.id) { step.correctAnswerCsv.split("|").size.coerceAtLeast(2) }
            ClassifyInteraction(options, groupCount, onSubmit)
        }
        InteractionType.CONECTAR -> ConnectInteraction(step.correctAnswerCsv, onSubmit)
        else -> SelectInteraction(options, step.interactionType, onSubmit)
    }
}

@Composable
private fun OrderInteraction(options: List<String>, onSubmit: (String) -> Unit) {
    var sequence by remember { mutableStateOf(listOf<String>()) }
    Column {
        Text("Toca las opciones EN ORDEN:", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 8.dp))
        ChipFlow(items = options, disabledItems = sequence.toSet()) { picked ->
            sequence = sequence + picked
        }
        Spacer(Modifier.padding(top = 12.dp))
        Text("Tu secuencia: " + sequence.joinToString(" → ").ifEmpty { "(vacío)" }, color = LabWhite.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.padding(top = 12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = { sequence = emptyList() }) { Text("Reiniciar") }
            Button(
                onClick = { onSubmit(sequence.joinToString(",")) },
                enabled = sequence.size == options.size,
                colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
            ) { Text("Comprobar", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun ClassifyInteraction(options: List<String>, groupCount: Int, onSubmit: (String) -> Unit) {
    // -1 = sin asignar; 0..groupCount-1 = grupo asignado
    var assignment by remember { mutableStateOf(options.associateWith { -1 }) }
    val groupLabels = remember(groupCount) { (1..groupCount).map { "Grupo $it" } }

    Column {
        Text("Toca cada elemento para asignarlo a un grupo:", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 8.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.wrapContentWidth()) {
            items(options) { option ->
                val group = assignment[option] ?: -1
                val label = if (group == -1) option else "$option → ${groupLabels[group]}"
                Chip(text = label, selected = group != -1, modifier = Modifier.padding(4.dp)) {
                    assignment = assignment.toMutableMap().apply { this[option] = if (group + 1 >= groupCount) -1 else group + 1 }
                }
            }
        }
        Spacer(Modifier.padding(top = 12.dp))
        val allAssigned = assignment.values.none { it == -1 }
        Button(
            onClick = {
                val groups = (0 until groupCount).map { g ->
                    assignment.filterValues { it == g }.keys.joinToString(",")
                }
                onSubmit(groups.joinToString("|"))
            },
            enabled = allAssigned,
            colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
        ) { Text("Comprobar", fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun ConnectInteraction(correctAnswerCsv: String, onSubmit: (String) -> Unit) {
    val pairs = remember(correctAnswerCsv) { correctAnswerCsv.split(",").map { it.split("-") } }
    val lefts = remember(pairs) { pairs.map { it[0] }.shuffled() }
    val rights = remember(pairs) { pairs.map { it.getOrElse(1) { "" } }.shuffled() }

    var selectedLeft by remember { mutableStateOf<String?>(null) }
    var connections by remember { mutableStateOf(mapOf<String, String>()) }

    Column {
        Text("Toca un elemento izquierdo y luego su pareja correcta:", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 8.dp))
        Row {
            Column(modifier = Modifier.weight(1f)) {
                lefts.forEach { left ->
                    val connected = connections.containsKey(left)
                    Chip(text = left, selected = selectedLeft == left || connected, modifier = Modifier.padding(4.dp).fillMaxWidth()) {
                        if (!connected) selectedLeft = left
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                rights.forEach { right ->
                    val used = connections.containsValue(right)
                    Chip(text = right, selected = used, modifier = Modifier.padding(4.dp).fillMaxWidth()) {
                        val left = selectedLeft
                        if (left != null && !used) {
                            connections = connections + (left to right)
                            selectedLeft = null
                        }
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
private fun SelectInteraction(options: List<String>, type: InteractionType, onSubmit: (String) -> Unit) {
    var selected by remember { mutableStateOf(setOf<String>()) }
    Column {
        Text("Selecciona tu respuesta:", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 8.dp))
        ChipFlow(items = options, selectedItems = selected) { picked ->
            selected = if (selected.contains(picked)) selected - picked else selected + picked
        }
        Spacer(Modifier.padding(top = 12.dp))
        Button(
            onClick = { onSubmit(selected.joinToString(",")) },
            enabled = selected.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
        ) { Text("Comprobar", fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun ChipFlow(
    items: List<String>,
    disabledItems: Set<String> = emptySet(),
    selectedItems: Set<String> = emptySet(),
    onPick: (String) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 120.dp)) {
        items(items) { item ->
            val disabled = item in disabledItems
            Chip(
                text = item,
                selected = item in selectedItems,
                enabled = !disabled,
                modifier = Modifier.padding(4.dp)
            ) { onPick(item) }
        }
    }
}

@Composable
private fun Chip(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) LabTeal500 else LabNavy700)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text.replace('_', ' '),
            color = if (selected) LabInk else LabWhite.copy(alpha = if (enabled) 1f else 0.4f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
