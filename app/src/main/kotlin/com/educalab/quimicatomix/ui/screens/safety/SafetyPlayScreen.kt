package com.educalab.quimicatomix.ui.screens.safety

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.components.FeedbackKind
import com.educalab.quimicatomix.ui.components.FeedbackPanel
import com.educalab.quimicatomix.ui.components.IconCatalog
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.components.QuimiMood
import com.educalab.quimicatomix.ui.components.QuimiSpeechBubble
import com.educalab.quimicatomix.ui.components.VisualAnswerTile
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun SafetyPlayScreen(
    onBack: () -> Unit,
    onFinished: () -> Unit,
    viewModel: SafetyViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.playState.collectAsState()
    val scenario = state.scenario

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = LabWhite)
                }
                Text(scenario?.title ?: "Cargando...", style = MaterialTheme.typography.titleLarge, color = LabWhite, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.padding(top = 8.dp))
            scenario?.let {
                LabIllustration(kind = IconCatalog.resolve(it.iconKey), primaryColor = IconCatalog.colorFor(it.id), sizeDp = 88)
                Spacer(Modifier.padding(top = 8.dp))
                QuimiSpeechBubble(text = it.situationText, mood = QuimiMood.THINKING)
            }
            Spacer(Modifier.padding(top = 16.dp))

            if (state.answered) {
                FeedbackPanel(
                    kind = if (state.wasCorrect) FeedbackKind.CORRECT else FeedbackKind.INCORRECT,
                    explanation = scenario?.explanation ?: "",
                    starsEarned = if (state.wasCorrect) 3 else null,
                    onContinue = { if (state.wasCorrect) onFinished() else viewModel.retry() },
                    onRetry = if (!state.wasCorrect) viewModel::retry else null
                )
            } else {
                Text("¿Qué harías en esta situación?", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
                Spacer(Modifier.padding(top = 12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.options.chunked(3).forEach { rowOptions ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            rowOptions.forEach { option ->
                                VisualAnswerTile(
                                    label = option,
                                    kind = IconCatalog.resolve(option),
                                    tint = IconCatalog.colorFor(option),
                                    selected = false,
                                    onClick = { viewModel.answer(option) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
