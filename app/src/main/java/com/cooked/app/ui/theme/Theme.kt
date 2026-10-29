package com.cooked.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CookedDarkColorScheme = darkColorScheme(
    primary = CookedAccentOrange,
    onPrimary = CookedButtonText,
    primaryContainer = CookedAccentOrange,
    onPrimaryContainer = CookedButtonText,
    secondary = CookedSecondaryText,
    onSecondary = CookedBackground,
    background = CookedBackground,
    onBackground = CookedPrimaryText,
    surface = CookedSurface,
    onSurface = CookedPrimaryText
)

@Composable
fun CookedTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = CookedDarkColorScheme,
        typography = Typography,
        content = content
    )
}
