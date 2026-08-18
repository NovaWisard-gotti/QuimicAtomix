package com.educalab.quimicatomix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.theme.LabNavy800
import com.educalab.quimicatomix.ui.theme.LabTeal300
import com.educalab.quimicatomix.ui.theme.LabWhite

/**
 * Selector de alias + avatar reutilizado por el onboarding y por la creación de perfiles
 * adicionales, para no duplicar la cuadrícula de avatares en dos pantallas.
 */
@Composable
fun AliasAvatarPicker(
    alias: String,
    avatarId: Int,
    onAliasChange: (String) -> Unit,
    onAvatarSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Elige tu alias y tu avatar",
    subtitle: String = "No necesitas tu nombre real ni ningún dato personal: solo un apodo para tu perfil local."
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, style = MaterialTheme.typography.headlineMedium, color = LabWhite, textAlign = TextAlign.Center, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold)
        androidx.compose.foundation.layout.Spacer(Modifier.height(6.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = LabWhite.copy(alpha = 0.75f), textAlign = TextAlign.Center)
        androidx.compose.foundation.layout.Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = alias,
            onValueChange = onAliasChange,
            label = { Text("Tu alias") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        androidx.compose.foundation.layout.Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(AVATAR_SPECS.size) { index ->
                AvatarSelectableTile(index = index, selected = avatarId == index, onClick = { onAvatarSelected(index) })
            }
        }
    }
}

@Composable
private fun AvatarSelectableTile(index: Int, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) LabTeal300.copy(alpha = 0.28f) else LabNavy800)
            .then(
                Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AvatarIllustration(avatarId = index, sizeDp = 52)
    }
}
