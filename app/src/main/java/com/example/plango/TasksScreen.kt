package com.example.plango

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.copy
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.forEach
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toLong
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle as DateTextStyle
import java.util.Locale


@Composable
fun TasksScreen() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val today = LocalDate.now()

    // Создаем CoroutineScope для управления прокруткой пейджера
    val coroutineScope = rememberCoroutineScope()

    // Параметры для "бесконечного" пейджера
    val initialPage = 5000
    val pagerState = rememberPagerState(initialPage = initialPage) { 10000 }

    // ВЫЧИСЛЯЕМ ОТОБРАЖАЕМЫЙ МЕСЯЦ на основе текущей страницы пейджера
    val displayedMonthText = remember(pagerState.currentPage) {
        val weekOffset = pagerState.currentPage - initialPage
        val mondayOfVisibleWeek = today
            .minusDays(today.dayOfWeek.value.toLong() - 1)
            .plusWeeks(weekOffset.toLong())

        mondayOfVisibleWeek.month.getDisplayName(DateTextStyle.FULL_STANDALONE, Locale("ru"))
            .replaceFirstChar { it.uppercase() } + " ${mondayOfVisibleWeek.year}"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayedMonthText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = { showDatePicker = true }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Календарь")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CalendarWeekPager(
            pagerState = pagerState,
            initialPage = initialPage,
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (selectedDate == today) "Задачи на сегодня"
            else "Задачи на ${selectedDate.dayOfMonth} ${selectedDate.month.getDisplayName(DateTextStyle.FULL, Locale("ru"))}",
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }

    if (showDatePicker) {
        CustomDatePickerDialog(
            initialDate = selectedDate,
            onDismiss = { showDatePicker = false },
            onConfirm = { pickedDate ->
                selectedDate = pickedDate
                showDatePicker = false

                // ВЫЧИСЛЯЕМ ЦЕЛЕВУЮ СТРАНИЦУ ДЛЯ ПЕЙДЖЕРА
                // 1. Находим разницу в неделях между "сегодня" и выбранной датой
                val todayMonday = today.minusDays(today.dayOfWeek.value.toLong() - 1)
                val pickedMonday = pickedDate.minusDays(pickedDate.dayOfWeek.value.toLong() - 1)

                val weeksBetween = java.time.temporal.ChronoUnit.WEEKS.between(todayMonday, pickedMonday)
                val targetPage = initialPage + weeksBetween.toInt()

                // Прокручиваем пейджер к этой неделе
                coroutineScope.launch {
                    pagerState.scrollToPage(targetPage)
                }
            }
        )
    }
}

@Composable
fun AddTaskSheet(onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var taskName by remember { mutableStateOf("") }
    var showGoalSelector by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf<String?>(null) }
    val goalText = selectedGoal ?: "Выбрать цель"
    val displayGoalText = if (goalText.length > 15) {    goalText.take(15).trim() + "..."
    } else {
        goalText
    }
    var subTasks by remember { mutableStateOf(listOf<SubTask>()) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var offsetY by remember { mutableFloatStateOf(0f) }
    val animatedOffset by animateFloatAsState(
        targetValue = offsetY,
        animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing),
        label = "drag_return"
    )
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var selectedPriority by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                // Оставляем здесь, чтобы все окно двигалось
                .graphicsLayer { translationY = animatedOffset.coerceAtLeast(0f) }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { showGoalSelector = false },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp) // Боковые отступы перенесем ниже, чтобы зона была во всю ширину
            ) {
                // ЗОНА ДЛЯ СМАХИВАНИЯ (ВЕРХНЯЯ ЧАСТЬ)
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp) // Высота чувствительной зоны
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta -> offsetY += delta },
                            onDragStopped = {
                                if (offsetY > 300) { // Чуть уменьшил порог для удобства
                                    keyboardController?.hide()
                                    onDismiss()
                                } else {
                                    offsetY = 0f
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Визуальный индикатор (серая полоска)
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .background(Color.LightGray, RoundedCornerShape(2.dp))
                    )
                }

                // ВНУТРЕННИЙ КОНТЕНТ (с боковыми отступами)
                Column(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        placeholder = { Text("Новая задача...", fontSize = 16.sp, color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,

                        // ЛЕВАЯ КНОПКА (Значок)
                        leadingIcon = {
                            Surface(
                                onClick = { /* Логика выбора иконки */ },
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(38.dp),
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Edit, // Иконка по умолчанию
                                        contentDescription = "Выбор значка",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },

                        // ПРАВАЯ КНОПКА (Добавить подзадачу)
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    val newNode = SubTask()
                                    subTasks = subTasks + newNode
                                    coroutineScope.launch {
                                        // Небольшая задержка, чтобы список успел обновиться перед скроллом
                                        delay(50)
                                        scrollState.animateScrollTo(scrollState.maxValue + 500)
                                    }
                                },
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Добавить подзадачу",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        },

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                        )
                    )
                    // 2. СПИСОК ПОДЗАДАЧ с плавной анимацией
                    if (subTasks.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Ограничиваем высоту, чтобы список не «съедал» весь экран
                        Box(modifier = Modifier.heightIn(max = 110.dp)) {
                            Column(modifier = Modifier.verticalScroll(scrollState)) {
                                subTasks.forEach { subTask ->
                                    // Ключ по ID критически важен для корректной анимации
                                    key(subTask.id) {
                                        // Локальное состояние видимости для запуска анимации
                                        var isVisible by remember { mutableStateOf(false) }
                                        LaunchedEffect(Unit) { isVisible = true }

                                        androidx.compose.animation.AnimatedVisibility(
                                            visible = isVisible,
                                            enter = androidx.compose.animation.expandVertically(
                                                animationSpec = androidx.compose.animation.core.tween(300)
                                            ) + androidx.compose.animation.fadeIn(),
                                            exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
                                        ) {
                                            SubTaskItem(
                                                subTask = subTask,
                                                onTextValueChange = { newValue ->
                                                    subTasks = subTasks.map {
                                                        if (it.id == subTask.id) it.copy(textValue = newValue) else it
                                                    }
                                                },
                                                onToggle = {
                                                    subTasks = subTasks.map {
                                                        if (it.id == subTask.id) {
                                                            it.copy(
                                                                isDone = !it.isDone,
                                                                textValue = it.textValue.copy()
                                                            )
                                                        } else it
                                                    }
                                                },
                                                onDelete = {
                                                    // Сначала запускаем исчезновение, затем удаляем из списка
                                                    isVisible = false
                                                    // Небольшая задержка перед физическим удалением для завершения анимации
                                                    coroutineScope.launch {
                                                        delay(200)
                                                        subTasks = subTasks.filter { it.id != subTask.id }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

// --- 2. БЛОК ВАЖНОСТИ И СРОЧНОСТИ (Матрица 2x2) ---
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PriorityButton("Важно | Срочно", 1, selectedPriority) { selectedPriority = it }
                            PriorityButton("Важно | Неср.", 2, selectedPriority) { selectedPriority = it }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PriorityButton("Нев. | Срочно", 3, selectedPriority) { selectedPriority = it }
                            PriorityButton("Нев. | Неср.", 4, selectedPriority) { selectedPriority = it }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

// --- 3. НИЖНИЙ БЛОК: ЦЕЛИ (слева) + НАПОМИНАНИЯ (справа) ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // ЛЕВАЯ ЧАСТЬ: ВЫБОР ЦЕЛИ
                        Column(modifier = Modifier.weight(0.45f)) {
                            val goalText = selectedGoal ?: "Выбрать цель"
                            val displayGoalText = if (goalText.length > 20) goalText.take(20).trim() + "..." else goalText

                            Surface(
                                onClick = {
                                    showGoalSelector = !showGoalSelector
                                    keyboardController?.hide()
                                },
                                modifier = Modifier
                                    .height(34.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(displayGoalText, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                                }
                            }

                            // Выпадающий список целей
                            androidx.compose.animation.AnimatedVisibility(visible = showGoalSelector) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
                                ) {
                                    Box(modifier = Modifier.heightIn(max = 110.dp)) {
                                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                            GoalItem("Без цели", selectedGoal == null) { selectedGoal = null; showGoalSelector = false }
                                            GoalItem("Работа", selectedGoal == "Работа") { selectedGoal = "Работа"; showGoalSelector = false }
                                            GoalItem("Личное", selectedGoal == "Личное") { selectedGoal = "Личное"; showGoalSelector = false }
                                        }
                                    }
                                }
                            }
                        }

                        // ПРАВАЯ ЧАСТЬ: КНОПКА НАПОМИНАНИЙ
                        Surface(
                            onClick = { /* Логика напоминаний */ },
                            modifier = Modifier
                                .weight(0.55f)
                                .height(34.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.TwoTone.DateRange, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Напоминание и сроки", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Кнопка "Запланировать" в самом низу
                    Button(
                        onClick = { if (taskName.isNotBlank()) onTaskAdded(taskName) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = taskName.isNotBlank()
                    ) {
                        Text("Запланировать", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

data class SubTask(
    val id: Long = System.currentTimeMillis() + (0..1000).random(),
    var textValue: TextFieldValue = TextFieldValue(""), // Используем TextFieldValue
    var isDone: Boolean = false
)

@Composable
fun SubTaskItem(
    subTask: SubTask,
    onTextValueChange: (TextFieldValue) -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val activeGreen = Color(0xFF4CAF50)
    val textColor = if (subTask.isDone) activeGreen.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
    val textDecoration = if (subTask.isDone) TextDecoration.LineThrough else TextDecoration.None

    val focusRequester = remember { FocusRequester() }

    // Храним последнее состояние, где было строго <= 2 строк
    var lastValidValue by remember { mutableStateOf(subTask.textValue) }

    LaunchedEffect(Unit) {
        if (subTask.textValue.text.isEmpty() && !subTask.isDone) {
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- ЛОГИКА ИКОНКИ ---
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = if (subTask.isDone) activeGreen.copy(alpha = 0.1f) else Color.Transparent,
                    shape = CircleShape
                )
                .border(1.5.dp, if (subTask.isDone) activeGreen else Color.LightGray, CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            if (subTask.isDone) {
                Icon(Icons.Default.Check, null, modifier = Modifier.size(14.dp), tint = activeGreen)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // --- ПОЛЕ ВВОДА ---
        BasicTextField(
            value = subTask.textValue,
            onValueChange = { newValue ->
                val newText = newValue.text
                val oldText = subTask.textValue.text
                val newEnters = newText.count { it == '\n' }

                if (newText.length < oldText.length) {
                    onTextValueChange(newValue)
                } else {
                    // Блокируем создание 3-й строки через Enter
                    if (newEnters > 1) return@BasicTextField

                    onTextValueChange(newValue)
                }
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            onTextLayout = { result ->
                // Если текст перескочил на 3-ю строку — откатываем
                if (result.lineCount > 2) {
                    onTextValueChange(lastValidValue)
                } else {
                    lastValidValue = subTask.textValue
                }
            },
            maxLines = 2,
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = textColor,
                textDecoration = textDecoration,
                // ДОБАВЛЯЕМ ПРАВИЛА ПЕРЕНОСА:
                lineBreak = LineBreak.Paragraph // Улучшает обработку пробелов и сложных переносов
            ),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (subTask.textValue.text.isEmpty()) {
                        Text(
                            text = "Название подзадачи...",
                            fontSize = 15.sp,
                            color = if (subTask.isDone) activeGreen.copy(alpha = 0.4f) else Color.LightGray,
                            textDecoration = textDecoration
                        )
                    }
                    innerTextField()
                }
            }
        )

        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp), tint = Color.Gray.copy(alpha = 0.5f))
        }
    }
}

// Вспомогательный компонент для элемента списка целей
@Composable
fun GoalItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val displaySize = 20
    val displayText = if (text.length > displaySize) {
        text.take(displaySize).trim() + "..."
    } else {
        text
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 1.dp)
    ) {
        Text(
            text = displayText,
            fontSize = 14.sp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 2
        )
    }
}

@Composable// Добавляем RowScope., чтобы weight стал доступен
fun RowScope.PriorityButton(
    label: String,
    id: Int,
    currentSelected: Int,
    onClick: (Int) -> Unit
) {
    val isSelected = id == currentSelected
    Surface(
        onClick = { onClick(if (isSelected) 0 else id) },
        modifier = Modifier
            .height(32.dp)
            .weight(1f), // Теперь ошибка исчезнет
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.2f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Composable
fun CalendarWeekPager(
    pagerState: PagerState,
    initialPage: Int,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        val weekOffset = page - initialPage
        val mondayOfDisplayedWeek = today
            .minusDays(today.dayOfWeek.value.toLong() - 1)
            .plusWeeks(weekOffset.toLong())

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            (0..6).forEach { dayIndex ->
                val date = mondayOfDisplayedWeek.plusDays(dayIndex.toLong())
                DayItem(
                    date = date,
                    isSelected = date == selectedDate,
                    isToday = date == today,
                    onClick = { onDateSelected(date) }
                )
            }
        }
    }
}

@Composable
fun DayItem(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val dayName = date.dayOfWeek.getDisplayName(DateTextStyle.SHORT, Locale("ru"))
    val dayNumber = date.dayOfMonth.toString()

    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        else -> Color.Transparent
    }

    val contentColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary
        isToday -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onBackground
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(45.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = dayName,
            fontSize = 12.sp,
            color = if (isSelected) contentColor else Color.Gray
        )
        Text(
            text = dayNumber,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(initialDate: LocalDate,
                           onDismiss: () -> Unit,
                           onConfirm: (LocalDate) -> Unit
) {
    var currentMonth by remember { mutableStateOf(initialDate.withDayOfMonth(1)) }
    var tempSelectedDate by remember { mutableStateOf(initialDate) }
    var isYearPickerMode by remember { mutableStateOf(false) }

    val today = LocalDate.now()

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(24.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.width(340.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // ВЕРХНЯЯ ЧАСТЬ: Выбранная дата
                val formatter = remember(tempSelectedDate) {
                    val day = tempSelectedDate.dayOfMonth
                    val month = tempSelectedDate.month.getDisplayName(DateTextStyle.SHORT, Locale("ru"))
                    val year = tempSelectedDate.year
                    "$day $month $year"
                }

                Text(
                    text = formatter.uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                TextButton(
                    onClick = { isYearPickerMode = !isYearPickerMode },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (isYearPickerMode) "Вернуться" else "${currentMonth.year} год")
                        Icon(if (isYearPickerMode) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isYearPickerMode) {
                    YearPickerWheel(
                        initialYear = currentMonth.year,
                        onYearSelected = { pickedYear ->
                            currentMonth = currentMonth.withYear(pickedYear)
                            tempSelectedDate = tempSelectedDate.withYear(pickedYear)
                            isYearPickerMode = false
                        }
                    )
                } else {
                    MonthPager(
                        currentMonth = currentMonth,
                        selectedDate = tempSelectedDate,
                        today = today,
                        onMonthChanged = { currentMonth = it },
                        onDateClick = { tempSelectedDate = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // НИЖНЯЯ ПАНЕЛЬ С КНОПКАМИ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Кнопка сброса слева, остальные справа
                    ) {
                        // Кнопка СБРОСИТЬ
                        TextButton(
                            onClick = {
                                val now = LocalDate.now()
                                tempSelectedDate = now
                                currentMonth = now.withDayOfMonth(1)
                            }
                        ) {
                            Text("СБРОСИТЬ", color = MaterialTheme.colorScheme.secondary)
                        }

                        Row {
                            TextButton(onClick = onDismiss) {
                                Text("ОТМЕНА")
                            }
                            TextButton(onClick = { onConfirm(tempSelectedDate) }) {
                                Text("ОК")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MonthPager(
    currentMonth: LocalDate,
    selectedDate: LocalDate,
    today: LocalDate,
    onMonthChanged: (LocalDate) -> Unit,    onDateClick: (LocalDate) -> Unit
) {
    val initialPage = 500
    val pagerState = rememberPagerState(initialPage = initialPage) { 1000 }

    // СИНХРОНИЗАЦИЯ: Если currentMonth изменился (например, через выбор года),
    // прокручиваем пейджер на нужную страницу
    LaunchedEffect(currentMonth.year, currentMonth.monthValue) {
        val monthDiff = java.time.temporal.ChronoUnit.MONTHS.between(
            LocalDate.now().withDayOfMonth(1),
            currentMonth.withDayOfMonth(1)
        ).toInt()

        val targetPage = initialPage + monthDiff
        if (pagerState.currentPage != targetPage) {
            pagerState.scrollToPage(targetPage)
        }
    }

    // Обратная синхронизация: когда свайпаем пальцем, уведомляем родителя о смене месяца
    LaunchedEffect(pagerState.currentPage) {
        val offset = pagerState.currentPage - initialPage
        val newMonth = LocalDate.now().withDayOfMonth(1).plusMonths(offset.toLong())
        if (newMonth.monthValue != currentMonth.monthValue || newMonth.year != currentMonth.year) {
            onMonthChanged(newMonth)
        }
    }

    Column {
        Text(
            text = currentMonth.month.getDisplayName(DateTextStyle.FULL, Locale("ru"))
                .replaceFirstChar { it.uppercase() } + " ${currentMonth.year}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            listOf("П", "В", "С", "Ч", "П", "С", "В").forEach {
                Text(it, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }

        HorizontalPager(state = pagerState) { page ->
            val monthOffset = page - initialPage
            val monthDate = LocalDate.now().withDayOfMonth(1).plusMonths(monthOffset.toLong())
            CalendarGrid(monthDate, selectedDate, today, onDateClick)
        }
    }
}

@Composable
fun CalendarGrid(
    monthDate: LocalDate,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = monthDate.withDayOfMonth(1)
    val lastDayOfMonth = monthDate.plusMonths(1).minusDays(1)

    // Пятнашки работают по системе: Пн=1, Вс=7.
    val firstDayDayOfWeek = firstDayOfMonth.dayOfWeek.value
    val daysInMonth = lastDayOfMonth.dayOfMonth

    Column(modifier = Modifier.fillMaxWidth()) {
        for (row in 0 until 6) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (col in 1..7) {
                    val cellIndex = row * 7 + col
                    val dayNumber = cellIndex - (firstDayDayOfWeek - 1)

                    if (dayNumber in 1..daysInMonth) {
                        val date = monthDate.withDayOfMonth(dayNumber)
                        val isSelected = date == selectedDate
                        val isToday = date == today

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = when {
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                        else -> Color.Transparent
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onDateClick(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayNumber.toString(),
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun YearPickerWheel(initialYear: Int, onYearSelected: (Int) -> Unit) {
    val haptic = LocalHapticFeedback.current // Достаем менеджер тактильной отдачи
    val years = remember { (2000..2100).toList() }
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = years.indexOf(initialYear)
    )

    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // Определяем центральный индекс
    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) years.indexOf(initialYear)
            else {
                val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItems.minByOrNull {
                    Math.abs((it.offset + it.size / 2) - viewportCenter)
                }?.index ?: 0
            }
        }
    }

    // ЛОГИКА ВИБРАЦИИ: Каждый раз, когда centerIndex меняется, происходит "щелчок"
    LaunchedEffect(centerIndex) {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    val currentYear = years.getOrElse(centerIndex) { initialYear }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(40.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            ) {}

            LazyColumn(
                state = listState,
                flingBehavior = snapFlingBehavior,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 80.dp)
            ) {
                items(years.size) { index ->
                    val year = years[index]

                    val animatedColor by animateColorAsState(
                        targetValue = if (index == centerIndex) MaterialTheme.colorScheme.primary else Color.Gray,
                        animationSpec = tween(150), label = ""
                    )

                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                            .graphicsLayer {
                                val firstIdx = listState.firstVisibleItemIndex
                                val firstOffset = listState.firstVisibleItemScrollOffset
                                val itemSizePx = 40.dp.toPx()
                                val viewportHeightPx = 200.dp.toPx()
                                val contentPaddingPx = 80.dp.toPx()

                                val itemTopInViewport =
                                    (index - firstIdx) * itemSizePx - firstOffset + contentPaddingPx
                                val itemCenterInViewport = itemTopInViewport + (itemSizePx / 2f)
                                val viewCenter = viewportHeightPx / 2f
                                val dist = Math.abs(viewCenter - itemCenterInViewport)
                                val maxDist = itemSizePx * 2.5f

                                if (dist < maxDist) {
                                    val progress = (dist / maxDist).coerceIn(0f, 1f)
                                    alpha = (1f - progress * 0.9f).coerceIn(0f, 1f)
                                    val scaleValue =
                                        (1.25f - progress * 0.55f).coerceIn(0.6f, 1.25f)
                                    scaleX = scaleValue
                                    scaleY = scaleValue
                                } else {
                                    alpha = 0f
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = year.toString(),
                            fontSize = 22.sp,
                            fontWeight = if (index == centerIndex) FontWeight.ExtraBold else FontWeight.Medium,
                            color = animatedColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onYearSelected(currentYear) },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ВЫБРАТЬ $currentYear")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    MaterialTheme {
        TasksScreen()
    }
}