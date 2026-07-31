package com.dannyrodrygues.petlife.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PetLifeLightColorScheme = lightColorScheme(
    primary = PetLifePurple,
    onPrimary = Color.White,
    primaryContainer = PetLifePurpleLight,
    onPrimaryContainer = PetLifePurpleDark,

    secondary = PetLifeTiffanyDark,
    onSecondary = Color.White,
    secondaryContainer = PetLifeTiffanyLight,
    onSecondaryContainer = PetLifeTextPrimary,

    background = PetLifeBackground,
    onBackground = PetLifeTextPrimary,

    surface = PetLifeSurface,
    onSurface = PetLifeTextPrimary,
    onSurfaceVariant = PetLifeTextSecondary,

    outline = PetLifeTextSecondary,
)

private val PetLifeDarkColorScheme = darkColorScheme(
    primary = PetLifePurpleLight,
    onPrimary = PetLifePurpleDark,
    primaryContainer = PetLifePurpleDark,
    onPrimaryContainer = PetLifePurpleLight,

    secondary = PetLifeTiffany,
    onSecondary = PetLifeTextPrimary,
    secondaryContainer = PetLifeTiffanyDark,
    onSecondaryContainer = Color.White,

    background = Color(0xFF17141D),
    onBackground = Color(0xFFF2EEF7),

    surface = Color(0xFF211D28),
    onSurface = Color(0xFFF2EEF7),
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF938F99),
)

@Composable
fun PetLifeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        PetLifeDarkColorScheme
    } else {
        PetLifeLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = PetLifeShapes,
        content = content,
    )
}