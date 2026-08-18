package com.educalab.quimicatomix.ui.screens.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.WorkspacePremium
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
import com.educalab.quimicatomix.data.local.entity.MasteryState
import com.educalab.quimicatomix.ui.components.AvatarIllustration
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.components.IconCatalog
import com.educalab.quimicatomix.ui.components.IllustrationKind
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.components.ModuleCard
import com.educalab.quimicatomix.ui.components.QuimiMood
import com.educalab.quimicatomix.ui.components.QuimiSpeechBubble
import com.educalab.quimicatomix.ui.components.XpBar
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun HomeScreen(
    onOpenTopic: (String) -> Unit,
    onOpenMolecules: () -> Unit,
    onOpenSafety: () -> Unit,
    onOpenProgress: () -> Unit,
    onOpenProfile: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HomeHeader(
                    aliasText = state.profile?.alias ?: "Explorador",
                    avatarId = state.profile?.avatarId ?: 0,
                    level = state.profile?.level ?: 1,
                    onOpenProfile = onOpenProfile
                )
            }
            item {
                XpBar(level = state.profile?.level ?: 1, progressWithinLevel = state.progressWithinLevel)
            }
            item {
                val greeting = state.suggestedTopic?.let { "¿Seguimos con \"${it.topic.title}\"? " + it.topic.narrativeIntro }
                    ?: "¡Bienvenido de nuevo al laboratorio! Elige un módulo para empezar."
                QuimiSpeechBubble(text = greeting, mood = QuimiMood.HAPPY)
            }
            item {
                Text(
                    "Tu laboratorio",
                    style = MaterialTheme.typography.titleLarge,
                    color = LabWhite,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            items(state.topics) { topicProgress ->
                val topic = topicProgress.topic
                val progress = topicProgress.progress
                val fraction = if (progress != null && progress.experimentsTotal > 0) {
                    progress.experimentsCompleted.toFloat() / progress.experimentsTotal
                } else 0f
                ModuleCard(
                    title = topic.title,
                    subtitle = topic.shortDescription,
                    illustration = IconCatalog.resolve(topic.iconKey),
                    tint = IconCatalog.parseHexOrFallback(topic.colorHex, topic.id),
                    mastery = progress?.mastery ?: MasteryState.BLOQUEADO,
                    progressFraction = fraction,
                    onClick = { onOpenTopic(topic.id) }
                )
            }
            item {
                Text(
                    "Más actividades",
                    style = MaterialTheme.typography.titleLarge,
                    color = LabWhite,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    QuickAccessTile(
                        title = "Constructor",
                        icon = Icons.Filled.Science,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenMolecules
                    )
                    QuickAccessTile(
                        title = "Seguridad",
                        icon = Icons.Filled.Shield,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenSafety
                    )
                    QuickAccessTile(
                        title = "Progreso",
                        icon = Icons.Filled.WorkspacePremium,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenProgress
                    )
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun HomeHeader(aliasText: String, avatarId: Int, level: Int, onOpenProfile: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(LabNavy800)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onOpenProfile
                )
                .padding(4.dp)
        ) {
            AvatarIllustration(avatarId = avatarId, sizeDp = 56)
        }
        Spacer(Modifier.padding(start = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("¡Hola, $aliasText!", style = MaterialTheme.typography.headlineMedium, color = LabWhite, fontWeight = FontWeight.ExtraBold)
            Text("Nivel $level · Laboratorio QuimicAtomix", style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.7f))
        }
        IconButton(onClick = onOpenProfile) {
            Icon(Icons.Filled.Settings, contentDescription = "Ajustes de perfil", tint = LabWhite)
        }
    }
}

@Composable
private fun QuickAccessTile(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(LabNavy800)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = LabWhite, modifier = Modifier.size(28.dp))
        Spacer(Modifier.padding(start = 8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
    }
}
