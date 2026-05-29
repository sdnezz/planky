package com.example.plango

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlin.math.roundToInt

private enum class Chronotype(val storageValue: String, val title: String) {
    MORNING("morning", "Утренний"),
    EVENING("evening", "Вечерний"),
    INTERMEDIATE("intermediate", "Умеренный");

    companion object {
        fun fromStorage(value: String?): Chronotype? {
            return values().firstOrNull { it.storageValue == value }
        }
    }
}

private fun chronotypeDisplayText(value: String?): String {
    return Chronotype.fromStorage(value)?.title ?: "Выбрать"
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val settingsDao = db.settingsDao()
    val scope = rememberCoroutineScope()
    val initialSettings = remember { runBlocking { settingsDao.getSettings() } }
    val settings by settingsDao.observeSettings().collectAsState(initial = initialSettings)

    var showChronotypeSelector by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    var chronotypeButtonBounds by remember { mutableStateOf<Rect?>(null) }

    val chronotype = settings?.chronotype
    val displayText = chronotypeDisplayText(chronotype)

    val currentThemeType = settings?.themeType?.let { AppThemeType.valueOf(it) } ?: AppThemeType.SYSTEM
    val currentAccent = settings?.accentColor?.let { AccentColorOption.valueOf(it) } ?: AccentColorOption.ORANGE

    val haptic = LocalHapticFeedback.current

    fun saveSettings(
        themeType: AppThemeType? = null,
        accent: AccentColorOption? = null,
        chronotypeValue: String? = null
    ) {
        scope.launch {
            settingsDao.upsertSettings(
                AppSettingsEntity(
                    id = 1,
                    chronotype = chronotypeValue ?: settings?.chronotype,
                    themeType = (themeType ?: currentThemeType).name,
                    accentColor = (accent ?: currentAccent).name
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Настройки",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Ваш хронотип (для приоритизации)",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentWidth()
                        ) {
                            Surface(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                    showChronotypeSelector = true },
                                modifier = Modifier
                                    .onGloballyPositioned {
                                        chronotypeButtonBounds = it.boundsInWindow()
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .width(112.dp)
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = displayText,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            if (showChronotypeSelector && chronotypeButtonBounds != null) {
                                val anchor = chronotypeButtonBounds!!

                                Popup(
                                    alignment = Alignment.TopStart,
                                    offset = IntOffset(
                                        x = 0,
                                        y = anchor.height.roundToInt() + with(density) { 8.dp.roundToPx() }
                                    ),
                                    onDismissRequest = { showChronotypeSelector = false },
                                    properties = PopupProperties(focusable = false)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surface,
                                        shadowElevation = 8.dp,
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                        ),
                                        modifier = Modifier.width(with(density) { anchor.width.toDp() })
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(4.dp)
                                        ) {
                                            ChronotypeItem(
                                                text = "Утренний",
                                                selected = chronotype == Chronotype.MORNING.storageValue,
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                                    saveSettings(chronotypeValue = Chronotype.MORNING.storageValue)
                                                    showChronotypeSelector = false
                                                }
                                            )

                                            ChronotypeItem(
                                                text = "Вечерний",
                                                selected = chronotype == Chronotype.EVENING.storageValue,
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                                    saveSettings(chronotypeValue = Chronotype.EVENING.storageValue)
                                                    showChronotypeSelector = false
                                                }
                                            )

                                            ChronotypeItem(
                                                text = "Умеренный",
                                                selected = chronotype == Chronotype.INTERMEDIATE.storageValue,
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                                    saveSettings(chronotypeValue = Chronotype.INTERMEDIATE.storageValue)
                                                    showChronotypeSelector = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Surface(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                val url = "https://psytests.org/trait/meq-run.html" // замените на реальный URL
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                val chooser = android.content.Intent.createChooser(intent, "Выберите браузер")
                                context.startActivity(chooser)
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier
                                .wrapContentWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Пройти тест на хронотип",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                tonalElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Тема", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ThemeOption(Color.White, "Светлая", currentThemeType == AppThemeType.LIGHT) {
                            saveSettings(AppThemeType.LIGHT)
                        }
                        ThemeOption(Color.Black, "Тёмная", currentThemeType == AppThemeType.DARK) {
                            saveSettings(AppThemeType.DARK)
                        }
                        ThemeOption(Color.Gray, "Система", currentThemeType == AppThemeType.SYSTEM) {
                            saveSettings(AppThemeType.SYSTEM)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ----- Блок Акцентный цвет -----
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                tonalElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Акцентный цвет", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AccentColorOption.values().forEach { option ->
                            val color = option.getColor(
                                when (currentThemeType) {
                                    AppThemeType.LIGHT -> false
                                    AppThemeType.DARK -> true
                                    else -> isSystemInDarkTheme()
                                }
                            )
                            AccentColorDot(color, option == currentAccent) {
                                saveSettings(accent = option)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChronotypeItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ThemeOption(color: Color, label: String, selected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            modifier = Modifier.size(40.dp),
            color = color,
            border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
        ) {}
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 12.sp)
    }
}

@Composable
private fun AccentColorDot(color: Color, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier.size(30.dp),
        color = color,
        border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant) else null
    ) {}
}