package com.example.plango

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val db = AppDatabase.getDatabase(this)
        val settingsDao = db.settingsDao()

        val initialSettings = runBlocking {
            settingsDao.getSettings()
        }
        val settingsFlow = MutableStateFlow(initialSettings)

        lifecycleScope.launch {
            settingsDao.observeSettings().collect { updatedSettings ->
                settingsFlow.value = updatedSettings
            }
        }

        setContent {
            val settings by settingsFlow.collectAsState()

            val colorScheme = appTheme(settings)
            val isDark = isDarkTheme(settings)

            val view = LocalView.current
            if (!view.isInEditMode) {
                val window = (view.context as Activity).window
                SideEffect {
                    WindowInsetsControllerCompat(window, view).apply {
                        setAppearanceLightStatusBars(!isDark)
                    }
                }
            }

            MaterialTheme(colorScheme = colorScheme) {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedItem by remember { mutableIntStateOf(1) }
    val haptic = LocalHapticFeedback.current
    val items = listOf("Цели", "Задачи", "Настройки")
    val icons = listOf(ImageVector.vectorResource(R.drawable.stars), Icons.Filled.List, Icons.Filled.Settings)
    var hasInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(selectedItem) {
        if (!hasInitialized) {
            hasInitialized = true
            return@LaunchedEffect
        }
        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding))
        {
            when (selectedItem) {
                0 -> GoalsScreen()
                1 -> TasksScreen(
                    onOpenSettings = {selectedItem = 2}
                )
                2 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun isDarkTheme(appSettings: AppSettingsEntity?): Boolean {
    val isSystemDark = isSystemInDarkTheme()
    val themeType = appSettings?.themeType?.let { AppThemeType.valueOf(it) } ?: AppThemeType.SYSTEM
    return when (themeType) {
        AppThemeType.LIGHT -> false
        AppThemeType.DARK -> true
        AppThemeType.SYSTEM -> isSystemDark
    }
}

@Composable
fun appTheme(appSettings: AppSettingsEntity?): ColorScheme {
    val isDark = isDarkTheme(appSettings)
    val accentOption = appSettings?.accentColor?.let { AccentColorOption.valueOf(it) } ?: AccentColorOption.ORANGE
    val accentColor = accentOption.getColor(isDark)

    val baseScheme = if (isDark) darkColorScheme() else lightColorScheme()
    return baseScheme.copy(
        primary = accentColor,
        secondary = accentColor,
        tertiary = accentColor
    )
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    MaterialTheme {
        MainScreen()
    }
}