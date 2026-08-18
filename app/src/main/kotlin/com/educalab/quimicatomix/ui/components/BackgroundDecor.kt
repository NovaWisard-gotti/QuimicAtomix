package com.educalab.quimicatomix.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.educalab.quimicatomix.ui.theme.LabBlue500
import com.educalab.quimicatomix.ui.theme.LabTeal500
import com.educalab.quimicatomix.ui.theme.LabViolet500

/**
 * Decoración de fondo sutil (burbujas/moléculas flotantes) para que las pantallas de
 * laboratorio no se sientan vacías, sin distraer del contenido principal.
 */
@Composable
fun BackgroundDecor(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        drawCircle(LabTeal500.copy(alpha = 0.06f), radius = w * 0.35f, center = Offset(w * 0.1f, h * 0.06f))
        drawCircle(LabBlue500.copy(alpha = 0.05f), radius = w * 0.28f, center = Offset(w * 0.95f, h * 0.22f))
        drawCircle(LabViolet500.copy(alpha = 0.05f), radius = w * 0.3f, center = Offset(w * 0.85f, h * 0.85f))
        drawCircle(LabTeal500.copy(alpha = 0.04f), radius = w * 0.2f, center = Offset(w * 0.05f, h * 0.9f))
    }
}
