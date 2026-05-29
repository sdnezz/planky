package com.example.plango

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Animatable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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
    val icons = listOf(ImageVector.vectorResource(R.drawable.stars), ImageVector.vectorResource(R.drawable.calendar_check_solid),
        ImageVector.vectorResource(R.drawable.gear))
    var hasInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(selectedItem) {
        if (!hasInitialized) {
            hasInitialized = true
            return@LaunchedEffect
        }
        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
    }

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val panelPadding = 16.dp
    val realPanelWidth = screenWidthDp - panelPadding * 2
    val itemWidth = realPanelWidth / items.size
    val indicatorRadius = 18.dp
    val indicatorVerticalOffset = -indicatorRadius * 0.3f // выступает на 30%

    val density = LocalDensity.current

    // Позиция индикатора в пикселях
    val indicatorPositionPx = remember { Animatable(0f) }
    var previousSelectedItem by remember { mutableIntStateOf(selectedItem) }

    LaunchedEffect(selectedItem) {
        val targetPx = with(density) { (panelPadding + itemWidth * selectedItem + itemWidth / 2).toPx() }
        val initialVelocity = if (selectedItem > previousSelectedItem) 300f else if (selectedItem < previousSelectedItem) -300f else 0f

        indicatorPositionPx.animateTo(
            targetValue = targetPx,
            animationSpec = spring(
                dampingRatio = 0.6f,
                stiffness = Spring.StiffnessLow,
                visibilityThreshold = 0.01f // пиксели
            ),
            initialVelocity = initialVelocity
        )
        previousSelectedItem = selectedItem
    }

    // Преобразуем обратно в Dp для использования в offset
    val indicatorPosition by remember {
        derivedStateOf { with(density) { indicatorPositionPx.value.toDp() } }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Основной контент
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (selectedItem) {
                    0 -> GoalsScreen()
                    1 -> TasksScreen(
                        bottomPadding = 64.dp,
                        onOpenSettings = { selectedItem = 2 }
                    )

                    2 -> SettingsScreen()
                }
            }
        }

        // Нижняя панель
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(60.dp) // уменьшили высоту (примерно на 2%)
                .align(Alignment.BottomCenter)
                .offset(y = (-16).dp),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 6.dp), // уменьшили отступ снизу, чтобы контент был по центру
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = selectedItem == index
                    val icon = icons[index]

                    val iconAlpha by animateFloatAsState(
                        targetValue = if (isSelected) 0f else 1f,
                        animationSpec = tween(durationMillis = 200)
                    )
                    val iconScale by animateFloatAsState(
                        targetValue = if (isSelected) 0f else 1f,
                        animationSpec = tween(durationMillis = 200)
                    )

                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        animationSpec = tween(durationMillis = 200)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (selectedItem != index) {
                                    selectedItem = index
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = item,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(28.dp)
                                .alpha(iconAlpha)
                                .graphicsLayer {
                                    scaleX = iconScale
                                    scaleY = iconScale
                                }
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = items[index],
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Индикатор (отдельный слой)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(
                    x = indicatorPosition - screenWidthDp / 2,
                    y = indicatorVerticalOffset - 18.dp - 12.dp // скорректировали под новую высоту панели
                )
                .size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            // Внешний круг (цвет панели)
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        shape = CircleShape
                    )
            )
            // Внутренний круг (акцентный цвет)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Анимация горизонтального колебания (без изменений)
                val shakeOffset = remember { Animatable(0f) }
                LaunchedEffect(selectedItem) {
                    shakeOffset.snapTo(0f)
                    shakeOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = keyframes {
                            durationMillis = 800
                            0f at 0
                            22f at 200
                            -12f at 350
                            5f at 680
                            0f at 800
                        }
                    )
                }

                AnimatedContent(
                    targetState = selectedItem,
                    transitionSpec = {
                        (fadeIn(tween(200)) + scaleIn(tween(200))) togetherWith
                                (fadeOut(tween(100)) + scaleOut(tween(100)))
                    }
                ) { index ->
                    Icon(
                        imageVector = icons[index],
                        contentDescription = items[index],
                        tint = Color.White,
                        modifier = Modifier
                            .size(32.dp)
                            .graphicsLayer {
                                translationX = shakeOffset.value
                            }
                    )
                }
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