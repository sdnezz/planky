package com.example.plango

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Акцентные цвета для светлой темы
object LightAccentColors {
    val Red = Color(0xFFFF0000)
    val Orange = Color(0xFFFFAA00)
    val Yellow = Color(0xFFFFC800)
    val Green = Color(0xFF4CAF50)
    val Cyan = Color(0xFF00E4FF)
    val Blue = Color(0xFF27A1FF)
    val Purple = Color(0xFF8000FF)
}

// Акцентные цвета для тёмной темы
object DarkAccentColors {
    val Red = Color(0xFFFF0300)
    val Orange = Color(0xFFFFAA00)
    val Yellow = Color(0xFFFFDD00)
    val Green = Color(0xFF4CAF50)
    val Cyan = Color(0xFF00E3FF)
    val Blue = Color(0xFF42B0FF)
    val Purple = Color(0xFFA550FF)
}

enum class AccentColorOption(val lightColor: Color, val darkColor: Color) {
    RED(LightAccentColors.Red, DarkAccentColors.Red),
    ORANGE(LightAccentColors.Orange, DarkAccentColors.Orange),
    YELLOW(LightAccentColors.Yellow, DarkAccentColors.Yellow),
    GREEN(LightAccentColors.Green, DarkAccentColors.Green),
    CYAN(LightAccentColors.Cyan, DarkAccentColors.Cyan),
    BLUE(LightAccentColors.Blue, DarkAccentColors.Blue),
    PURPLE(LightAccentColors.Purple, DarkAccentColors.Purple)
}

fun AccentColorOption.getColor(isDark: Boolean): Color {
    return if (isDark) darkColor else lightColor
}

enum class AppThemeType {
    LIGHT, DARK, SYSTEM
}

val LocalAccentColor = compositionLocalOf { AccentColorOption.BLUE }