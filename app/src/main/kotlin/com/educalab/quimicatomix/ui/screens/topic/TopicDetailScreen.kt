package com.educalab.quimicatomix.ui.screens.topic

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
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.components.IconCatalog
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.components.StarsRow
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabLime500
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun TopicDetailScreen(
    onBack: () -> Unit,
    onOpenExperiment: (String) -> Unit,
    viewModel: TopicDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = LabWhite)
                }
                Column {
                    Text(
                        state.topic?.title ?: "",
                        style = MaterialTheme.typography.headlineMedium,
                        color = LabWhite,
                        fontWeight = FontWeight.ExtraBold
                    )
                    state.topic?.narrativeIntro?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.7f))
                    }
                }
            }
            LazyColumn(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.experiments) { item ->
                    ExperimentRow(item = item, onClick = { onOpenExperiment(item.experiment.id) })
                }
            }
        }
    }
}

@Composable
private fun ExperimentRow(item: ExperimentWithStatus, onClick: () -> Unit) {
    val exp = item.experiment
    val tint = IconCatalog.colorFor(exp.id)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(LabNavy800)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) {
            LabIllustration(kind = IconCatalog.resolve(exp.iconKey), primaryColor = tint, sizeDp = 40)
        }
        Spacer(Modifier.padding(start = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(exp.title, style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
            Text(exp.narrativeHook, style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.7f), maxLines = 2)
            Spacer(Modifier.padding(top = 4.dp))
            StarsRow(starsEarned = item.bestStars, starSizeDp = 18)
        }
        if (item.isCompleted) {
            Icon(Icons.Filled.CheckCircle, contentDescription = "Completado", tint = LabLime500)
        }
    }
}
