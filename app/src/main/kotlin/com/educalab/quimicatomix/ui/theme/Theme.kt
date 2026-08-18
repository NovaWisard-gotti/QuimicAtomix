package com.educalab.quimicatomix.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val QuimicAtomixColorScheme = darkColorScheme(
    primary = LabTeal500,
    onPrimary = LabInk,
    secondary = LabBlue500,
    onSecondary = LabWhite,
    tertiary = LabViolet500,
    onTertiary = LabWhite,
    background = LabNavy900,
    onBackground = LabWhite,
    surface = LabNavy800,
    onSurface = LabWhite,
    surfaceVariant = LabNavy700,
    onSurfaceVariant = LabMuted,
    error = LabCoral500,
    onError = LabInk
)

/**
 * Tema visual único de QuimicAtomix: fondo "laboratorio nocturno" con acentos vibrantes.
 * Deliberadamente oscuro y dinámico -no pastel ni infantil- para encajar con un público
 * de 8 a 12 años que busca sentirse "científico moderno", no en una app de preescolar.
 */
@Composable
fun QuimicAtomixTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            it.statusBarColor = LabNavy900.toArgb()
            it.navigationBarColor = LabNavy900.toArgb()
            WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = QuimicAtomixColorScheme,
        typography = QuimicAtomixTypography,
        shapes = QuimicAtomixShapes,
        content = content
    )
}
