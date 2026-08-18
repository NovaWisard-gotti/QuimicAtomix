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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import com.educalab.quimicatomix.data.local.entity.SafetyCategory
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.components.IconCatalog
import com.educalab.quimicatomix.ui.components.IllustrationKind
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.components.QuimiMood
import com.educalab.quimicatomix.ui.components.QuimiSpeechBubble
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabLime500
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabWhite

private fun categoryLabel(category: SafetyCategory): String = when (category) {
    SafetyCategory.EN_CASA -> "En casa"
    SafetyCategory.EN_ESCUELA -> "En la escuela"
    SafetyCategory.EN_LABORATORIO_VIRTUAL -> "En el laboratorio"
    SafetyCategory.PRIMEROS_AUXILIOS_BASICOS -> "Primeros auxilios"
    SafetyCategory.ETIQUETAS_Y_SIMBOLOS -> "Etiquetas y símbolos"
}

@Composable
fun SafetyHubScreen(
    onBack: () -> Unit,
    onOpenScenario: (String) -> Unit,
    viewModel: SafetyViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.hubState.collectAsState()
    val grouped = remember(state.scenarios) { state.scenarios.groupBy { it.category } }

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = LabWhite)
                }
                Text("Guardianes de la Seguridad", style = MaterialTheme.typography.headlineMedium, color = LabWhite, fontWeight = FontWeight.ExtraBold)
            }
            QuimiSpeechBubble(
                text = "Aprende a reconocer situaciones seguras en casa, en la escuela y en el laboratorio.",
                mood = QuimiMood.ENCOURAGING,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            LazyColumn(contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)) {
                grouped.forEach { (category, scenarios) ->
                    item {
                        Text(
                            categoryLabel(category),
                            style = MaterialTheme.typography.titleMedium,
                            color = LabWhite,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
                        )
                    }
                    items(scenarios) { scenario ->
                        val done = scenario.id in state.completedIds
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(LabNavy800)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { onOpenScenario(scenario.id) }
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(44.dp).clip(CircleShape).background(LabNavy700),
                                contentAlignment = Alignment.Center
                            ) {
                                LabIllustration(kind = IconCatalog.resolve(scenario.iconKey), primaryColor = IconCatalog.colorFor(scenario.id), sizeDp = 32)
                            }
                            Spacer(Modifier.padding(start = 10.dp))
                            Text(scenario.title, color = LabWhite, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                            if (done) Icon(Icons.Filled.CheckCircle, contentDescription = "Superado", tint = LabLime500)
                        }
                    }
                }
            }
        }
    }
}
