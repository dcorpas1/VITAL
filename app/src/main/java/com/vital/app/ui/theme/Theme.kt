package com.vital.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VitalColorScheme = darkColorScheme(
    primary = VitalRed,
    onPrimary = VitalWhite,
    secondary = VitalRedDark,
    onSecondary = VitalWhite,
    background = VitalBlack,
    onBackground = VitalWhite,
    surface = VitalGray,
    onSurface = VitalWhite,
    surfaceVariant = VitalGrayMid,
    onSurfaceVariant = VitalTextSecondary,
    outline = VitalGrayLight,
    error = VitalRedLight
)

@Composable
fun VITALTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VitalColorScheme,
        typography = VitalTypography,
        content = content
    )
}