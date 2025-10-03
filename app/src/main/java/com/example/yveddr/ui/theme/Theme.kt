package com.example.yveddr.ui.theme

import android.app.Activity
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
    primary = HelloKittyPink,
    secondary = LavenderPink,
    tertiary = SoftPink,
    background = Color(0xFFF5E6EE),
    surface = Color(0xFFFFF0F5),
    onPrimary = Color.White,
    onSecondary = Color(0xFF5D4E56),
    onTertiary = Color.White,
    onBackground = Color(0xFF3D2D35),
    onSurface = Color(0xFF3D2D35),
    surfaceVariant = VeryLightPink,
    onSurfaceVariant = Color(0xFF6D5D65)
)

private val LightColorScheme = lightColorScheme(
    primary = HelloKittyPink,
    secondary = LavenderPink,
    tertiary = SoftPink,
    background = Color(0xFFFFFBFC),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color(0xFF5D4E56),
    onTertiary = Color.White,
    onBackground = Color(0xFF1E1B1E),
    onSurface = Color(0xFF1E1B1E),
    surfaceVariant = VeryLightPink,
    onSurfaceVariant = Color(0xFF4F4449)
)

@Composable
fun YvedDrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme.copy(
        background = Color(0xFFFFFBFC),
        surface = Color(0xFFFFFAFC),
        surfaceVariant = Color(0xFFFFF5F8)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}