package com.ryall.bitcoinwallet.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = FlashLime,
    onPrimary = FlashDarkGreen,

    primaryContainer = FlashDarkGreen,
    onPrimaryContainer = FlashLime,

    secondary = Color(0xFF9AD5A0),
    onSecondary = FlashDarkBackground,

    background = FlashDarkBackground,
    onBackground = FlashDarkText,

    surface = FlashDarkSurface,
    onSurface = FlashDarkText,

    surfaceVariant = Color(0xFF294231),
    onSurfaceVariant = Color(0xFFB8CDBB),

    outline = Color(0xFF748C79),
    error = Color(0xFFFFB4AB)
)

private val LightColorScheme = lightColorScheme(
    primary = FlashDarkGreen,
    onPrimary = Color.White,

    primaryContainer = FlashLime,
    onPrimaryContainer = FlashDarkGreen,

    secondary = FlashGreen,
    onSecondary = Color.White,

    background = FlashLightBackground,
    onBackground = FlashLightText,

    surface = FlashLightSurface,
    onSurface = FlashLightText,

    surfaceVariant = Color(0xFFE5EDE4),
    onSurfaceVariant = Color(0xFF455A49),

    outline = FlashOutline,
    error = FlashError
)

@Composable
fun BitcoinWalletTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}