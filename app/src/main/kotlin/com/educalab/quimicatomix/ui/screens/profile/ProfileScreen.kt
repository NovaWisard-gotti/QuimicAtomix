package com.educalab.quimicatomix.ui.screens.profile

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.educalab.quimicatomix.data.local.entity.UserProfile
import com.educalab.quimicatomix.ui.components.AvatarIllustration
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabTeal300
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onCreateProfile: () -> Unit,
    onProfileChanged: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val profile by viewModel.profile.collectAsState()
    val profiles by viewModel.profiles.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = LabWhite)
                }
                Text("Tu perfil", style = MaterialTheme.typography.headlineMedium, color = LabWhite, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.padding(top = 16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(LabNavy800)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarIllustration(avatarId = profile?.avatarId ?: 0, sizeDp = 64)
                Spacer(Modifier.padding(start = 14.dp))
                Column {
                    Text(profile?.alias ?: "", style = MaterialTheme.typography.titleLarge, color = LabWhite, fontWeight = FontWeight.Bold)
                    Text("Nivel ${profile?.level ?: 1} · ${profile?.totalXp ?: 0} XP", style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.7f))
                }
            }
            Spacer(Modifier.padding(top = 20.dp))
            Text("Perfiles", style = MaterialTheme.typography.titleLarge, color = LabWhite, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.padding(top = 8.dp))
            Text(
                "Crea un perfil por cada jugador: cada uno guarda su propio progreso.",
                style = MaterialTheme.typography.bodyMedium,
                color = LabWhite.copy(alpha = 0.7f)
            )
            Spacer(Modifier.padding(top = 10.dp))
            profiles.forEach { p ->
                ProfileRow(
                    profile = p,
                    isActive = p.id == viewModel.activeProfileId,
                    onClick = { viewModel.switchProfile(p.id, onProfileChanged) }
                )
            }
            Spacer(Modifier.padding(top = 4.dp))
            OutlinedButton(onClick = onCreateProfile, modifier = Modifier.fillMaxWidth()) {
                Text("+ Crear nuevo perfil", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.padding(top = 20.dp))
            Text("Preferencias", style = MaterialTheme.typography.titleLarge, color = LabWhite, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.padding(top = 8.dp))
            SettingRow(
                title = "Sonido",
                subtitle = "Efectos de acierto, error y desbloqueo",
                checked = profile?.soundEnabled ?: true,
                onCheckedChange = viewModel::setSoundEnabled
            )
            SettingRow(
                title = "Vibración",
                subtitle = "Vibración breve al seleccionar o acertar",
                checked = profile?.hapticsEnabled ?: true,
                onCheckedChange = viewModel::setHapticsEnabled
            )
            Spacer(Modifier.padding(top = 20.dp))
            Text(
                "QuimicAtomix guarda todo tu progreso solo en este dispositivo. No se piden datos personales ni conexión a internet.",
                style = MaterialTheme.typography.bodyMedium,
                color = LabWhite.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun ProfileRow(profile: UserProfile, isActive: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isActive) LabTeal300.copy(alpha = 0.18f) else LabNavy800)
            .then(
                if (isActive) Modifier else Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarIllustration(avatarId = profile.avatarId, sizeDp = 44)
        Spacer(Modifier.padding(start = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(profile.alias, style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
            Text("Nivel ${profile.level}", style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.6f))
        }
        if (isActive) {
            Text("Activo", style = MaterialTheme.typography.labelLarge, color = LabTeal300, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SettingRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LabNavy800)
            .padding(14.dp)
            .padding(top = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = LabWhite, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.7f))
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = LabTeal500)
        )
    }
    Spacer(Modifier.padding(top = 10.dp))
}
