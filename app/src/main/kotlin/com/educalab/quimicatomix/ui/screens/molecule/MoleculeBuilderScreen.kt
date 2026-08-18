package com.educalab.quimicatomix.ui.screens.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.components.FeedbackKind
import com.educalab.quimicatomix.ui.components.FeedbackPanel
import com.educalab.quimicatomix.ui.components.IllustrationKind
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.components.QuimiMood
import com.educalab.quimicatomix.ui.components.QuimiSpeechBubble
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabCoral500
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun MoleculeBuilderScreen(
    onBack: () -> Unit,
    onFinished: () -> Unit,
    viewModel: MoleculeBuilderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.builderState.collectAsState()
    val challenge = state.challenge

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = LabWhite)
                }
                Text(
                    challenge?.let { "${it.formula} · ${it.commonName}" } ?: "Cargando...",
                    style = MaterialTheme.typography.titleLarge,
                    color = LabWhite,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            challenge?.let {
                QuimiSpeechBubble(text = it.description, mood = QuimiMood.THINKING)
            }
            Spacer(Modifier.padding(top = 12.dp))

            if (state.outcome?.isComplete == true) {
                FeedbackPanel(
                    kind = FeedbackKind.CORRECT,
                    explanation = "¡Construiste ${challenge?.commonName}! ${challenge?.funFact.orEmpty()}",
                    starsEarned = state.starsEarned,
                    onContinue = onFinished
                )
            } else {
                MoleculeBuilderBody(state = state, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun MoleculeBuilderBody(state: MoleculeBuilderUiState, viewModel: MoleculeBuilderViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
            Text("Zona de construcción", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(LabNavy800)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.builtAtoms.isEmpty()) {
                    Text("Toca átomos abajo para añadirlos aquí", color = LabWhite.copy(alpha = 0.5f), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp))
                }
                state.builtAtoms.entries.forEach { (symbol, count) ->
                    BuiltAtomChip(symbol = symbol, count = count, onRemove = { viewModel.removeAtom(symbol) })
                }
            }

            state.outcome?.let { outcome ->
                if (!outcome.isComplete) {
                    Spacer(Modifier.padding(top = 8.dp))
                    val explanation = buildString {
                        if (outcome.missingAtoms.isNotEmpty()) {
                            append("Te falta: " + outcome.missingAtoms.entries.joinToString(", ") { "${it.value}x ${it.key}" } + ". ")
                        }
                        if (outcome.extraAtoms.isNotEmpty()) {
                            append("Te sobra: " + outcome.extraAtoms.entries.joinToString(", ") { "${it.value}x ${it.key}" } + ".")
                        }
                    }
                    FeedbackPanel(
                        kind = FeedbackKind.INCORRECT,
                        explanation = explanation.ifBlank { "Revisa la composición e inténtalo de nuevo." },
                        onContinue = {},
                        onRetry = { viewModel.resetBuild() }
                    )
                }
            }

            Spacer(Modifier.padding(top = 16.dp))
            Text("Átomos disponibles", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
            Spacer(Modifier.padding(top = 8.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = Modifier.weight(1f)) {
                items(state.palette) { atom ->
                    AtomPaletteChip(symbol = atom.symbol, name = atom.name, colorHex = atom.colorHex, onClick = { viewModel.addAtom(atom.symbol) })
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedButton(onClick = viewModel::resetBuild, modifier = Modifier.weight(1f)) { Text("Reiniciar") }
                Button(
                    onClick = viewModel::checkMolecule,
                    modifier = Modifier.weight(1f),
                    enabled = state.builtAtoms.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
                ) { Text("Comprobar molécula", fontWeight = FontWeight.Bold) }
            }
    }
}

@Composable
private fun BuiltAtomChip(symbol: String, count: Int, onRemove: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(LabCoral500)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onRemove
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("$symbol×$count", color = LabWhite, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AtomPaletteChip(symbol: String, name: String, colorHex: String?, onClick: () -> Unit) {
    val tint = com.educalab.quimicatomix.ui.components.IconCatalog.parseHexOrFallback(colorHex, symbol)
    Column(
        modifier = Modifier
            .padding(6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(LabNavy700)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(tint),
            contentAlignment = Alignment.Center
        ) {
            Text(symbol, color = LabWhite, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.padding(top = 4.dp))
        Text(name, color = LabWhite.copy(alpha = 0.8f), style = MaterialTheme.typography.labelMedium)
    }
}
