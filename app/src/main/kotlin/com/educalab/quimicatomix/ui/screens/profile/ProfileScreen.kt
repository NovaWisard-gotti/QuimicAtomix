package com.educalab.quimicatomix.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.educalab.quimicatomix.ui.components.AvatarIllustration
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val profile by viewModel.profile.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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
