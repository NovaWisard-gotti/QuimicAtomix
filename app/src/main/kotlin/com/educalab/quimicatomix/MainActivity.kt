package com.educalab.quimicatomix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.quimicatomix.ui.components.IllustrationKind
import com.educalab.quimicatomix.ui.components.LabIllustration
import com.educalab.quimicatomix.ui.navigation.QuimicAtomixNavGraph
import com.educalab.quimicatomix.ui.navigation.Routes
import com.educalab.quimicatomix.ui.theme.LabNavy900
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabWhite
import com.educalab.quimicatomix.ui.theme.QuimicAtomixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuimicAtomixTheme {
                QuimicAtomixRoot()
            }
        }
    }
}

@Composable
private fun QuimicAtomixRoot() {
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as QuimicAtomixApp
    var isReady by remember { mutableStateOf(false) }
    var hasExistingProfile by remember { mutableStateOf(false) }
    var profileEpoch by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        hasExistingProfile = app.container.bootstrap()
        isReady = true
    }

    if (!isReady) {
        LoadingScreen()
        return
    }

    val start = if (hasExistingProfile) Routes.HOME else Routes.ONBOARDING
    androidx.compose.runtime.key(profileEpoch) {
        QuimicAtomixNavGraph(startDestination = start, onProfileChanged = { profileEpoch++ })
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize().background(LabNavy900), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LabIllustration(kind = IllustrationKind.ATOM, primaryColor = LabTeal500, sizeDp = 96)
            Text(
                "Encendiendo el laboratorio…",
                style = MaterialTheme.typography.titleMedium,
                color = LabWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
