package com.educalab.quimicatomix.ui.screens.experiment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.educalab.quimicatomix.domain.model.SeparationTechnique
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.components.FeedbackKind
import com.educalab.quimicatomix.ui.components.FeedbackPanel
import com.educalab.quimicatomix.ui.components.IconCatalog
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.components.QuimiMood
import com.educalab.quimicatomix.ui.components.QuimiSpeechBubble
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun ExperimentPlayScreen(
    onBack: () -> Unit,
    onFinished: () -> Unit,
    viewModel: ExperimentPlayViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = LabWhite)
                }
                Column {
                    Text(
                        state.experiment?.title ?: "Cargando...",
                        style = MaterialTheme.typography.titleLarge,
                        color = LabWhite,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Spacer(Modifier.padding(top = 8.dp))
            state.experiment?.let {
                QuimiSpeechBubble(text = it.narrativeHook, mood = QuimiMood.THINKING)
            }
            Spacer(Modifier.padding(top = 16.dp))

            when (state.phase) {
                PlayPhase.LOADING -> { /* breve, sin contenido adicional */ }
                PlayPhase.MEZCLA_PREDICT -> MezclaPredictSection(
                    substanceAName = state.substanceA?.name ?: "",
                    substanceBName = state.substanceB?.name ?: "",
                    substanceAColorHex = state.substanceA?.colorHex,
                    substanceBColorHex = state.substanceB?.colorHex,
                    onPredict = viewModel::submitMixturePrediction
                )
                PlayPhase.SEPARACION_CHOOSE -> SeparacionChooseSection(
                    options = state.separationOptions,
                    onChoose = viewModel::submitSeparationChoice
                )
                PlayPhase.GENERIC_STEP -> state.currentStep?.let { step ->
                    Column {
                        Text(step.instruction, style = MaterialTheme.typography.bodyLarge, color = LabWhite)
                        Spacer(Modifier.padding(top = 12.dp))
                        com.educalab.quimicatomix.ui.screens.experiment.GenericStepInteraction(step = step) { answer ->
                            viewModel.submitGenericAnswer(answer)
                        }
                    }
                }
                PlayPhase.FEEDBACK -> {
                    val kind = when (state.feedbackTone) {
                        FeedbackTone.CORRECT -> FeedbackKind.CORRECT
                        FeedbackTone.PARTIAL -> FeedbackKind.PARTIAL
                        else -> FeedbackKind.INCORRECT
                    }
                    FeedbackPanel(
                        kind = kind,
                        explanation = state.feedbackExplanation,
                        starsEarned = if (kind == FeedbackKind.CORRECT) state.starsEarned else null,
                        onContinue = { if (kind == FeedbackKind.CORRECT) onFinished() else viewModel.retry() },
                        onRetry = if (kind != FeedbackKind.CORRECT) viewModel::retry else null
                    )
                }
                PlayPhase.FINISHED -> {}
            }
        }
    }
}

@Composable
private fun MezclaPredictSection(
    substanceAName: String,
    substanceBName: String,
    substanceAColorHex: String?,
    substanceBColorHex: String?,
    onPredict: (Boolean) -> Unit
) {
    Column {
        Text("¿Qué crees que pasará al combinarlas?", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            SubstanceChip(substanceAName, substanceAColorHex, Modifier.weight(1f))
            SubstanceChip(substanceBName, substanceBColorHex, Modifier.weight(1f))
        }
        Spacer(Modifier.padding(top = 20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onPredict(true) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
            ) { Text("Mezcla homogénea", fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center) }
            Button(
                onClick = { onPredict(false) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = LabNavy800, contentColor = LabWhite)
            ) { Text("Mezcla heterogénea", fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center) }
        }
    }
}

@Composable
private fun SubstanceChip(name: String, colorHex: String?, modifier: Modifier = Modifier) {
    val tint = IconCatalog.parseHexOrFallback(colorHex, name)
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(LabNavy800)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LabIllustration(kind = IconCatalog.resolve(name), primaryColor = tint, sizeDp = 56)
        Spacer(Modifier.padding(top = 8.dp))
        Text(name, color = LabWhite, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SeparacionChooseSection(options: List<SeparationTechnique>, onChoose: (SeparationTechnique) -> Unit) {
    Column {
        Text("¿Qué técnica usarías para separar esta mezcla?", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(top = 14.dp))
        options.forEach { technique ->
            val label = when (technique) {
                SeparationTechnique.FILTRACION -> "Filtración"
                SeparationTechnique.DECANTACION -> "Decantación"
                SeparationTechnique.EVAPORACION -> "Evaporación"
                SeparationTechnique.NINGUNA -> "Ninguna técnica"
            }
            Button(
                onClick = { onChoose(technique) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LabNavy800, contentColor = LabWhite)
            ) { Text(label, fontWeight = FontWeight.Bold) }
        }
    }
}
