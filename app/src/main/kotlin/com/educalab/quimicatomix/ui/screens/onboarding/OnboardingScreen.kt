package com.educalab.quimicatomix.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.educalab.quimicatomix.ui.components.AvatarIllustration
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.components.IllustrationKind
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.components.QuimiCharacter
import com.educalab.quimicatomix.ui.components.QuimiMood
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabNavy700
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabTeal300
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsState()

    if (state.finished) {
        onFinished()
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indicador de páginas
            Row(
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(viewModel.totalPages) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == state.pageIndex) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (index == state.pageIndex) LabTeal500 else LabNavy700)
                    )
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                AnimatedContent(targetState = state.pageIndex, label = "onboardingPage") { page ->
                    when (page) {
                        0 -> OnboardingPagePurpose()
                        1 -> OnboardingPageWorld()
                        2 -> OnboardingPageProgress()
                        else -> OnboardingPageProfile(
                            alias = state.alias,
                            avatarId = state.avatarId,
                            onAliasChange = viewModel::updateAlias,
                            onAvatarSelected = viewModel::selectAvatar
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = viewModel::previousPage, enabled = state.pageIndex > 0) {
                    Text("Atrás", color = if (state.pageIndex > 0) LabWhite else LabNavy700)
                }
                if (state.pageIndex < viewModel.totalPages - 1) {
                    Button(
                        onClick = viewModel::nextPage,
                        colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
                    ) { Text("Siguiente", fontWeight = FontWeight.Bold) }
                } else {
                    Button(
                        onClick = viewModel::finishOnboarding,
                        enabled = !state.isCreatingProfile,
                        colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
                    ) { Text("¡Entrar al laboratorio!", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
private fun OnboardingPagePurpose() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LabIllustration(kind = IllustrationKind.FLASK, primaryColor = LabTeal500, sizeDp = 140)
        Spacer(Modifier.height(20.dp))
        Text("Bienvenido a QuimicAtomix", style = MaterialTheme.typography.headlineMedium, color = LabWhite, textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(10.dp))
        Text(
            "Un laboratorio virtual y seguro donde vas a mezclar, separar, construir moléculas y descubrir cómo funciona la química.",
            style = MaterialTheme.typography.bodyLarge, color = LabWhite.copy(alpha = 0.85f), textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OnboardingPageWorld() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        QuimiCharacter(mood = QuimiMood.HAPPY, sizeDp = 150)
        Spacer(Modifier.height(20.dp))
        Text("Te presento a Quimi", style = MaterialTheme.typography.headlineMedium, color = LabWhite, textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(10.dp))
        Text(
            "Quimi es un átomo curioso que vive en el laboratorio. Te acompañará con misiones y pistas cortas, sin interrumpir tu exploración.",
            style = MaterialTheme.typography.bodyLarge, color = LabWhite.copy(alpha = 0.85f), textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OnboardingPageProgress() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LabIllustration(kind = IllustrationKind.MAP, primaryColor = LabTeal500, sizeDp = 140)
        Spacer(Modifier.height(20.dp))
        Text("Explora a tu ritmo", style = MaterialTheme.typography.headlineMedium, color = LabWhite, textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(10.dp))
        Text(
            "Gana XP y estrellas completando prácticas cortas, sube de nivel, desbloquea temas nuevos y colecciona insignias y equipamiento. Tu progreso se guarda solo, sin internet.",
            style = MaterialTheme.typography.bodyLarge, color = LabWhite.copy(alpha = 0.85f), textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OnboardingPageProfile(
    alias: String,
    avatarId: Int,
    onAliasChange: (String) -> Unit,
    onAvatarSelected: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Elige tu alias y tu avatar", style = MaterialTheme.typography.headlineMedium, color = LabWhite, textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(6.dp))
        Text(
            "No necesitas tu nombre real ni ningún dato personal: solo un apodo para tu perfil local.",
            style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.75f), textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = alias,
            onValueChange = onAliasChange,
            label = { Text("Tu alias") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.height(160.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(8) { index ->
                AvatarSelectable(index = index, selected = avatarId == index, onClick = { onAvatarSelected(index) })
            }
        }
    }
}

@Composable
private fun AvatarSelectable(index: Int, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) LabTeal300.copy(alpha = 0.28f) else LabNavy800)
            .then(
                Modifier.clickable(
                    interactionSource = androidx.compose.runtime.remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AvatarIllustration(avatarId = index, sizeDp = 52)
    }
}
