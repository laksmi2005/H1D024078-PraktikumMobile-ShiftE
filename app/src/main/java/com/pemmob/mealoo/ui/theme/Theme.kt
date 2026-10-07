package com.pemmob.mealoo.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ZestyLime,
    onPrimary = NearBlack,
    primaryContainer = ZestyLimeContainer,
    onPrimaryContainer = NearBlack,
    secondary = LilacPink,
    onSecondary = NearBlack,
    secondaryContainer = LilacPinkContainer,
    onSecondaryContainer = NearBlack,
    tertiary = SkyBlue,
    onTertiary = NearBlack,
    tertiaryContainer = SkyBlueContainer,
    onTertiaryContainer = NearBlack,
    background = WarmCanvas,
    onBackground = NearBlack,
    surface = CardWhite,
    onSurface = NearBlack,
    surfaceVariant = WarmCanvas,
    onSurfaceVariant = SlateGray,
    outline = SlateLight,
    outlineVariant = BorderSubtle
)

private val DarkColorScheme = darkColorScheme(
    primary = ZestyLime,
    onPrimary = NearBlack,
    primaryContainer = Color(0xFF2C3E08),
    onPrimaryContainer = ZestyLime,
    secondary = LilacPink,
    onSecondary = NearBlack,
    tertiary = SkyBlue,
    onTertiary = NearBlack,
    background = Color(0xFF141412),
    onBackground = Color(0xFFEDEDE6),
    surface = Color(0xFF1E1E1B),
    onSurface = Color(0xFFEDEDE6),
    surfaceVariant = Color(0xFF282824),
    onSurfaceVariant = Color(0xFFA0A3A8),
    outline = Color(0xFF4A4B4E)
)

@Composable
fun MealooTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}