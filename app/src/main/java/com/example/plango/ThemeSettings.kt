package com.example.plango

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Акцентные цвета для светлой темы
object LightAccentColors {
    val Gray = Color(0xFF565656)
    val Red = Color(0xFFFF0000)
    val Orange = Color(0xFFFFAA00)
    val Green = Color(0xFF5CD562)
    val Turquoise = Color(0xFF00EF99)
    val Cyan = Color(0xFF00E4FF)
    val Blue = Color(0xFF1BA8FF)
    val Purple = Color(0xFF8800FF)
    val Pink = Color(0xFFFF2DC2)
}

// Акцентные цвета для тёмной темы
object DarkAccentColors {
    val Gray = Color(0xFFD5D5D5)
    val Red = Color(0xFFC50300)
    val Orange = Color(0xFFDE9700)
    val Green = Color(0xFF56CE5B)
    val Turquoise = Color(0xFF00FFB7)
    val Cyan = Color(0xFF00E3FF)
    val Blue = Color(0xFF44ACFF)
    val Purple = Color(0xFF9325FF)
    val Pink = Color(0xFFE50089)
}

enum class AccentColorOption(val lightColor: Color, val darkColor: Color) {
    GRAY(LightAccentColors.Gray, DarkAccentColors.Gray),
    RED(LightAccentColors.Red, DarkAccentColors.Red),
    ORANGE(LightAccentColors.Orange, DarkAccentColors.Orange),
    GREEN(LightAccentColors.Green, DarkAccentColors.Green),
    TURQOUISE(LightAccentColors.Turquoise, DarkAccentColors.Turquoise),
    CYAN(LightAccentColors.Cyan, DarkAccentColors.Cyan),
    BLUE(LightAccentColors.Blue, DarkAccentColors.Blue),
    PURPLE(LightAccentColors.Purple, DarkAccentColors.Purple),
    PINK(LightAccentColors.Pink, DarkAccentColors.Pink)
}

fun AccentColorOption.getColor(isDark: Boolean): Color {
    return if (isDark) darkColor else lightColor
}

enum class AppThemeType {
    LIGHT, DARK, SYSTEM
}

val LocalAccentColor = compositionLocalOf { AccentColorOption.BLUE }