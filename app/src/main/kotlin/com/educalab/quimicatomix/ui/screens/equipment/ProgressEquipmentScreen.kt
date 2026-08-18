package com.educalab.quimicatomix.ui.screens.equipment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.educalab.quimicatomix.data.local.entity.Badge
import com.educalab.quimicatomix.data.local.entity.LabEquipment
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.components.IconCatalog
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.components.MasteryChip
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabGold500
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun ProgressEquipmentScreen(
    onBack: () -> Unit,
    viewModel: ProgressEquipmentViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = LabWhite)
                }
                Text("Progreso y colección", style = MaterialTheme.typography.headlineMedium, color = LabWhite, fontWeight = FontWeight.ExtraBold)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatPill(label = "Estrellas", value = state.totalStars.toString())
                StatPill(label = "Prácticas completadas", value = state.totalExperimentsCompleted.toString())
                StatPill(label = "Insignias", value = "${state.earnedBadgeIds.size}/${state.badges.size}")
            }
            Spacer(Modifier.padding(top = 12.dp))
            Text("Insignias", style = MaterialTheme.typography.titleLarge, color = LabWhite, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 16.dp))
            BadgeGrid(badges = state.badges, earnedIds = state.earnedBadgeIds)
            Spacer(Modifier.padding(top = 8.dp))
            Text("Equipamiento de laboratorio", style = MaterialTheme.typography.titleLarge, color = LabWhite, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 16.dp))
            EquipmentGrid(equipment = state.equipment, unlockedIds = state.unlockedEquipmentIds)
        }
    }
}

@Composable
private fun StatPill(label: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(LabNavy800)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = LabGold500, fontWeight = FontWeight.ExtraBold)
        Text(label, style = MaterialTheme.typography.labelMedium, color = LabWhite.copy(alpha = 0.7f))
    }
}

@Composable
private fun BadgeGrid(badges: List<Badge>, earnedIds: Set<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(horizontal = 12.dp).size((((badges.size + 2) / 3) * 120).dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
    ) {
        items(badges) { badge ->
            val earned = badge.id in earnedIds
            Column(
                modifier = Modifier.padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (earned) LabNavy800 else LabNavy700.copy(alpha = 0.5f))
                        .padding(10.dp)
                ) {
                    LabIllustration(
                        kind = IconCatalog.resolve(badge.iconKey),
                        primaryColor = if (earned) IconCatalog.colorFor(badge.id) else LabNavy700,
                        sizeDp = 48
                    )
                }
                Text(
                    badge.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (earned) LabWhite else LabWhite.copy(alpha = 0.4f),
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun EquipmentGrid(equipment: List<LabEquipment>, unlockedIds: Set<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(horizontal = 12.dp).size((((equipment.size + 2) / 3) * 120).dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
    ) {
        items(equipment) { item ->
            val unlocked = item.id in unlockedIds
            Column(
                modifier = Modifier.padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (unlocked) LabNavy800 else LabNavy700.copy(alpha = 0.5f))
                        .padding(10.dp)
                ) {
                    LabIllustration(
                        kind = IconCatalog.resolve(item.iconKey),
                        primaryColor = if (unlocked) IconCatalog.colorFor(item.id) else LabNavy700,
                        sizeDp = 48
                    )
                }
                Text(
                    item.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (unlocked) LabWhite else LabWhite.copy(alpha = 0.4f),
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
