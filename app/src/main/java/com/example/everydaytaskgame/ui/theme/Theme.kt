package com.example.everydaytaskgame.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary          = Green700,
    onPrimary        = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Green200,
    onPrimaryContainer = Green900,
    secondary        = Gold400,
    onSecondary      = androidx.compose.ui.graphics.Color.Black,
    secondaryContainer = Gold200,
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF3D2900),
    background       = Green50,
    onBackground     = OnSurface,
    surface          = Surface,
    onSurface        = OnSurface,
    error            = Red400,
    onError          = androidx.compose.ui.graphics.Color.White
)

@Composable
fun EverydayTaskGameTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = Typography,
        content     = content
    )
}
