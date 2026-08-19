package com.educalab.quimicatomix.ui.screens.safety

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import com.educalab.quimicatomix.ui.components.IconCatalog
import com.educalab.quimicatomix.ui.components.IllustrationKind
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.components.QuimiMood
import com.educalab.quimicatomix.ui.components.QuimiSpeechBubble
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabNavy800
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
                // Las opciones de seguridad son frases completas (no una palabra corta como en
                // otros ejercicios), así que usan una tarjeta ancha de texto sin límite de
                // líneas en vez de la cuadrícula compacta de íconos: con la cuadrícula el
                // texto se cortaba y no se podía leer la opción completa.
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.options.forEach { option ->
                        WideOptionCard(
                            text = option,
                            kind = IconCatalog.resolve(option),
                            tint = IconCatalog.colorFor(option),
                            onClick = { viewModel.answer(option) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WideOptionCard(text: String, kind: IllustrationKind, tint: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(LabNavy800)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LabIllustration(kind = kind, primaryColor = tint, sizeDp = 40)
        Spacer(Modifier.padding(start = 12.dp))
        Text(
            text,
            color = LabWhite,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
    }
}
