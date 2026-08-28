package com.dannyrodrygues.petlife.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.dannyrodrygues.petlife.core.tenant.config.PetLifeDefaultTenant
import com.dannyrodrygues.petlife.core.tenant.model.BrandConfig

private val PetLifeLightColorScheme = lightColorScheme(
    primary = PetLifePurple,
    onPrimary = Color.White,
    primaryContainer = PetLifePurpleLight,
    onPrimaryContainer = PetLifePurpleDark,

    secondary = PetLifeBlue,
    onSecondary = PetLifeTextPrimary,
    secondaryContainer = PetLifeBlueLight,
    onSecondaryContainer = PetLifeTextPrimary,

    tertiary = PetLifeBlueDark,

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

    secondary = PetLifeBlue,
    onSecondary = PetLifeTextPrimary,
    secondaryContainer = PetLifeBlueDark,
    onSecondaryContainer = Color.White,

    tertiary = PetLifeBlueDark,

    background = Color(0xFF17141D),
    onBackground = Color(0xFFF2EEF7),

    surface = Color(0xFF211D28),
    onSurface = Color(0xFFF2EEF7),
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF938F99),
)

@Composable
fun PetLifeTheme(
    brandConfig: BrandConfig = PetLifeDefaultTenant.config.brand,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val primaryColor = brandConfig.primaryColorHex.toComposeColor(
        fallback = PetLifePurple,
    )

    val secondaryColor = brandConfig.secondaryColorHex.toComposeColor(
        fallback = PetLifeBlue,
    )

    val tertiaryColor = brandConfig.tertiaryColorHex
        ?.toComposeColor(
            fallback = PetLifeBlueDark,
        )
        ?: PetLifeBlueDark

    val colorScheme = if (darkTheme) {

        /*
         * Nesta primeira etapa do Multi-Tenant,
         * mantemos as tonalidades estruturais do Dark Theme
         * para preservar contraste e acessibilidade.
         *
         * As cores de branding que já são seguras no tema
         * escuro continuam sendo aplicadas.
         */
        PetLifeDarkColorScheme.copy(
            secondary = secondaryColor,
            tertiary = tertiaryColor,
        )

    } else {

        /*
         * No Light Theme, as cores principais passam
         * a ser fornecidas pelo BrandConfig do Tenant.
         */
        PetLifeLightColorScheme.copy(
            primary = primaryColor,
            secondary = secondaryColor,
            tertiary = tertiaryColor,
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = PetLifeShapes,
        content = content,
    )
}

private fun String.toComposeColor(
    fallback: Color,
): Color {
    return try {

        Color(
            android.graphics.Color.parseColor(this),
        )

    } catch (_: IllegalArgumentException) {

        fallback
    }
}