package com.burelo.appmedicinal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary                = DarkPrimary,
    onPrimary              = DarkOnPrimary,
    primaryContainer       = DarkPrimaryContainer,
    onPrimaryContainer     = DarkOnPrimaryContainer,
    inversePrimary         = DarkInversePrimary,
    secondary              = DarkSecondary,
    onSecondary            = DarkOnSecondary,
    secondaryContainer     = DarkSecondaryContainer,
    onSecondaryContainer   = DarkOnSecondaryContainer,
    tertiary               = DarkTertiary,
    onTertiary             = DarkOnTertiary,
    tertiaryContainer      = DarkTertiaryContainer,
    onTertiaryContainer    = DarkOnTertiaryContainer,
    background             = DarkBackground,
    onBackground           = DarkOnSurface,
    surface                = DarkSurface,
    onSurface              = DarkOnSurface,
    surfaceVariant         = DarkSurfaceVariant,
    onSurfaceVariant       = DarkOnSurfaceVariant,
    surfaceDim             = DarkSurfaceDim,
    surfaceBright          = DarkSurfaceBright,
    surfaceContainerLowest = DarkSurfaceLowest,
    surfaceContainerLow    = DarkSurfaceLow,
    surfaceContainer       = DarkSurfaceContainer,
    surfaceContainerHigh   = DarkSurfaceHigh,
    surfaceContainerHighest = DarkSurfaceHighest,
    inverseSurface         = DarkInverseSurface,
    inverseOnSurface       = DarkInverseOnSurface,
    outline                = DarkOutline,
    outlineVariant         = DarkOutlineVariant,
    error                  = DarkError,
    onError                = DarkOnError,
    errorContainer         = DarkErrorContainer,
    onErrorContainer       = DarkOnErrorContainer,
    surfaceTint            = DarkSurfaceTint,
)

private val LightColorScheme = lightColorScheme(
    primary                = LightPrimary,
    onPrimary              = LightOnPrimary,
    primaryContainer       = LightPrimaryContainer,
    onPrimaryContainer     = LightOnPrimaryContainer,
    inversePrimary         = LightInversePrimary,
    secondary              = LightSecondary,
    onSecondary            = LightOnSecondary,
    secondaryContainer     = LightSecondaryContainer,
    onSecondaryContainer   = LightOnSecondaryContainer,
    tertiary               = LightTertiary,
    onTertiary             = LightOnTertiary,
    tertiaryContainer      = LightTertiaryContainer,
    onTertiaryContainer    = LightOnTertiaryContainer,
    background             = LightBackground,
    onBackground           = LightOnSurface,
    surface                = LightSurface,
    onSurface              = LightOnSurface,
    surfaceVariant         = LightSurfaceVariant,
    onSurfaceVariant       = LightOnSurfaceVariant,
    surfaceDim             = LightSurfaceDim,
    surfaceBright          = LightSurfaceBright,
    surfaceContainerLowest = LightSurfaceLowest,
    surfaceContainerLow    = LightSurfaceLow,
    surfaceContainer       = LightSurfaceContainer,
    surfaceContainerHigh   = LightSurfaceHigh,
    surfaceContainerHighest = LightSurfaceHighest,
    inverseSurface         = LightInverseSurface,
    inverseOnSurface       = LightInverseOnSurface,
    outline                = LightOutline,
    outlineVariant         = LightOutlineVariant,
    error                  = LightError,
    onError                = LightOnError,
    errorContainer         = LightErrorContainer,
    onErrorContainer       = LightOnErrorContainer,
    surfaceTint            = LightSurfaceTint,
)

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),   // radius-sm
    small      = RoundedCornerShape(16.dp),  // radius-base
    medium     = RoundedCornerShape(24.dp),  // radius-md — cards, inputs
    large      = RoundedCornerShape(32.dp),  // radius-lg
    extraLarge = RoundedCornerShape(48.dp),  // radius-xl
)

@Composable
fun NaturaMedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NaturaMedTypography,
        shapes = AppShapes,
        content = content,
    )
}
