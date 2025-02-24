package com.example.spartacusgame.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val SpartacusRed = Color(0xFFFF0000)
val SpartacusBackground = Color(0xFF121212)

@Composable
fun SpartacusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = SpartacusRed,
            background = SpartacusBackground,
            surface = Color.White,
            onPrimary = Color.White,
            onBackground = Color.White,
            onSurface = Color.Black
        ),
        typography = Typography(),
        content = content
    )
}
