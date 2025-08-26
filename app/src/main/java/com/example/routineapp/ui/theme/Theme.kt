package com.example.routineapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OliveLight = lightColorScheme(
    primary = Color(0xFF6B7D57),
    secondary = Color(0xFFC9BBA7),
    background = Color(0xFFF7F7F5),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onBackground = Color(0xFF111111),
    onSurface = Color(0xFF111111)
)

private val OliveDark = darkColorScheme(
    primary = Color(0xFF6B7D57),
    secondary = Color(0xFF8B9199),
    background = Color(0xFF121212),
    surface = Color(0xFF1A1A1A),
    onPrimary = Color.White,
    onBackground = Color(0xFFEFEFEF),
    onSurface = Color(0xFFEFEFEF)
)

@Composable
fun RoutineTheme(dark: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (dark) OliveDark else OliveLight, content = content)
}
