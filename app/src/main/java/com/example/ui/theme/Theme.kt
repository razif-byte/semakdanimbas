package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalBigTextMode = compositionLocalOf { false }
val LocalHighContrastMode = compositionLocalOf { false }

private val HighContrastColorScheme = darkColorScheme(
    primary = HighContrastYellow,
    onPrimary = HighContrastBlack,
    primaryContainer = HighContrastYellow,
    onPrimaryContainer = HighContrastBlack,
    secondary = HighContrastCyan,
    onSecondary = HighContrastBlack,
    secondaryContainer = HighContrastCyan,
    onSecondaryContainer = HighContrastBlack,
    tertiary = HighContrastGreen,
    onTertiary = HighContrastBlack,
    background = HighContrastBlack,
    onBackground = HighContrastWhite,
    surface = HighContrastSurface,
    onSurface = HighContrastWhite,
    surfaceVariant = HighContrastSurface,
    onSurfaceVariant = HighContrastYellow,
    outline = HighContrastYellow,
    error = HighContrastRed,
    onError = HighContrastWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = KioskBlueLight,
    onPrimary = Color(0xFF003258),
    primaryContainer = KioskBlueDark,
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = KioskTeal,
    onSecondary = Color(0xFF003735),
    secondaryContainer = Color(0xFF004F4C),
    onSecondaryContainer = Color(0xFF80F7EC),
    tertiary = KioskAmber,
    onTertiary = Color(0xFF452B00),
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = Color(0xFF4B5563),
    error = KioskRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = KioskBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary = KioskTeal,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCE8E5),
    onSecondaryContainer = Color(0xFF00201F),
    tertiary = KioskAmber,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = Color(0xFFCBD5E1),
    error = KioskRed,
    onError = Color.White
)

@Composable
fun KioskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    bigText: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = when {
        highContrast -> HighContrastColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalBigTextMode provides bigText,
        LocalHighContrastMode provides highContrast
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
