package com.example.plango

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// Акцентные цвета для светлой темы
object LightAccentColors {
    val Red = Color(0xFFD32F2F)
    val Orange = Color(0xFFE65100)
    val Yellow = Color(0xFFFBC02D)
    val Green = Color(0xFF388E3C)
    val Cyan = Color(0xFF0097A7)
    val Blue = Color(0xFF1976D2)
    val Purple = Color(0xFF7B1FA2)
}

// Акцентные цвета для тёмной темы
object DarkAccentColors {
    val Red = Color(0xFFEF5350)
    val Orange = Color(0xFFFF9800)
    val Yellow = Color(0xFFFFEB3B)
    val Green = Color(0xFF4CAF50)
    val Cyan = Color(0xFF26C6DA)
    val Blue = Color(0xFF42A5F5)
    val Purple = Color(0xFFAB47BC)
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