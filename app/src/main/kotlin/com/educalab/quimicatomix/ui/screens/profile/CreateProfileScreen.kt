package com.educalab.quimicatomix.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.educalab.quimicatomix.ui.components.AliasAvatarPicker
import com.educalab.quimicatomix.ui.components.BackgroundDecor
import com.educalab.quimicatomix.ui.navigation.AppViewModelProvider
import com.educalab.quimicatomix.ui.theme.LabInk
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabWhite

@Composable
fun CreateProfileScreen(
    onBack: () -> Unit,
    onCreated: () -> Unit,
    viewModel: CreateProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsState()

    if (state.created) {
        onCreated()
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(LabNavy900)) {
        BackgroundDecor()
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = LabWhite)
                }
                Text("Nuevo perfil", style = MaterialTheme.typography.headlineMedium, color = LabWhite, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.padding(top = 16.dp))
            AliasAvatarPicker(
                alias = state.alias,
                avatarId = state.avatarId,
                onAliasChange = viewModel::updateAlias,
                onAvatarSelected = viewModel::selectAvatar,
                title = "Crea otro perfil",
                subtitle = "Cada perfil guarda su propio progreso, por separado."
            )
            Spacer(Modifier.padding(top = 20.dp))
            Button(
                onClick = viewModel::createProfile,
                enabled = !state.isCreating,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = LabTeal500, contentColor = LabInk)
            ) { Text("Crear perfil", fontWeight = FontWeight.Bold) }
        }
    }
}
