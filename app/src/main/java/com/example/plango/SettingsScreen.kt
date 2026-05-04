package com.example.plango

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.launch
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
    return Chronotype.fromStorage(value)?.title ?: "указать хронотип"
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val settingsDao = db.settingsDao()
    val scope = rememberCoroutineScope()

    val chronotype by settingsDao.observeChronotype().collectAsState(initial = null)

    var showChronotypeSelector by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    var chronotypeButtonBounds by remember { mutableStateOf<Rect?>(null) }

    val displayText = chronotypeDisplayText(chronotype)

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
                                onClick = { showChronotypeSelector = true },
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
                                                    scope.launch {
                                                        settingsDao.upsertSettings(
                                                            AppSettingsEntity(
                                                                id = 1,
                                                                chronotype = Chronotype.MORNING.storageValue
                                                            )
                                                        )
                                                    }
                                                    showChronotypeSelector = false
                                                }
                                            )

                                            ChronotypeItem(
                                                text = "Вечерний",
                                                selected = chronotype == Chronotype.EVENING.storageValue,
                                                onClick = {
                                                    scope.launch {
                                                        settingsDao.upsertSettings(
                                                            AppSettingsEntity(
                                                                id = 1,
                                                                chronotype = Chronotype.EVENING.storageValue
                                                            )
                                                        )
                                                    }
                                                    showChronotypeSelector = false
                                                }
                                            )

                                            ChronotypeItem(
                                                text = "Умеренный",
                                                selected = chronotype == Chronotype.INTERMEDIATE.storageValue,
                                                onClick = {
                                                    scope.launch {
                                                        settingsDao.upsertSettings(
                                                            AppSettingsEntity(
                                                                id = 1,
                                                                chronotype = Chronotype.INTERMEDIATE.storageValue
                                                            )
                                                        )
                                                    }
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
                                // пока мнимая кнопка
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