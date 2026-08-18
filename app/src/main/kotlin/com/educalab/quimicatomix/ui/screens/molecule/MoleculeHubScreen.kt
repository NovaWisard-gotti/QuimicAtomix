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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
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
import com.educalab.quimicatomix.ui.components.IllustrationKind
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.components.QuimiMood
import com.educalab.quimicatomix.ui.components.QuimiSpeechBubble
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabCoral500
import com.educalab.quimicatomix.ui.theme.LabLime500
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun MoleculeHubScreen(
    onBack: () -> Unit,
    onOpenChallenge: (String) -> Unit,
    viewModel: MoleculeBuilderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.hubState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = LabWhite)
                }
                Text("Mesa de construcción molecular", style = MaterialTheme.typography.headlineMedium, color = LabWhite, fontWeight = FontWeight.ExtraBold)
            }
            QuimiSpeechBubble(
                text = "Elige una molécula y arrastra los átomos que necesites para construirla.",
                mood = QuimiMood.ENCOURAGING,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.padding(top = 8.dp))
            LazyColumn(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.challenges) { challenge ->
                    val unlocked = state.playerLevel >= challenge.unlockLevel
                    val built = challenge.id in state.builtIds
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(LabNavy800)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                enabled = unlocked,
                                onClick = { onOpenChallenge(challenge.id) }
                            )
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(56.dp).clip(CircleShape).background(LabNavy700),
                            contentAlignment = Alignment.Center
                        ) {
                            LabIllustration(kind = IllustrationKind.MOLECULE, primaryColor = if (unlocked) LabCoral500 else LabNavy700, sizeDp = 40)
                        }
                        Spacer(Modifier.padding(start = 12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${challenge.formula} · ${challenge.commonName}", style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
                            Text(
                                if (unlocked) challenge.description else "Se desbloquea en el nivel ${challenge.unlockLevel}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = LabWhite.copy(alpha = 0.7f),
                                maxLines = 2
                            )
                        }
                        when {
                            !unlocked -> Icon(Icons.Filled.Lock, contentDescription = "Bloqueada", tint = LabWhite.copy(alpha = 0.35f))
                            built -> Icon(Icons.Filled.CheckCircle, contentDescription = "Construida", tint = LabLime500)
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}
