package com.example.plango

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import androidx.core.i18n.DateTimeFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.time.format.TextStyle as DateTextStyle
import java.util.Locale
import kotlin.math.abs
import kotlin.text.isNotBlank
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.style.TextAlign
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun TasksScreen() {
    var showAddTaskSheet by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<TaskWithSubtasks?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val today = LocalDate.now()

    // Создаем CoroutineScope для управления прокруткой пейджера
    val coroutineScope = rememberCoroutineScope()

    // Параметры для "бесконечного" пейджера
    val initialPage = 5000
    val pagerState = rememberPagerState(initialPage = initialPage) { 10000 }
    var weekPagerProgrammaticScroll by remember { mutableStateOf(false) }
    var taskPagerProgrammaticScroll by remember { mutableStateOf(false) }
    var ignoreWeekPagerUpdate by remember { mutableStateOf(false) }

    // ВЫЧИСЛЯЕМ ОТОБРАЖАЕМЫЙ МЕСЯЦ на основе текущей страницы пейджера
    val displayedMonthText = remember(pagerState.currentPage) {
        val weekOffset = pagerState.currentPage - initialPage
        val mondayOfVisibleWeek = today
            .minusDays(today.dayOfWeek.value.toLong() - 1)
            .plusWeeks(weekOffset.toLong())

        mondayOfVisibleWeek.month.getDisplayName(DateTextStyle.FULL_STANDALONE, Locale("ru"))
            .replaceFirstChar { it.uppercase() } + " ${mondayOfVisibleWeek.year}"
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val taskDao = db.taskDao()

    // Получаем задачи для БД (в реальном приложении лучше через ViewModel)
    val startOfDay = selectedDate.atStartOfDay(java.time.ZoneOffset.UTC).toInstant().toEpochMilli()
    val endOfDay =
        selectedDate.atTime(java.time.LocalTime.MAX).atZone(java.time.ZoneOffset.UTC).toInstant()
            .toEpochMilli()

    // Подписываемся на поток данных из БД
    val tasksFlow = remember(selectedDate) { taskDao.getTasksWithSubtasksForDay(startOfDay, endOfDay) }
    val tasksList by tasksFlow.collectAsState(initial = emptyList())

    var orderedTasks by remember(selectedDate, tasksList) {
        mutableStateOf(tasksList.sortedBy { it.task.position })
    }
    val hapticFeedback = LocalHapticFeedback.current
    val lazyListState = rememberLazyListState()

    // Сбрасываем скролл при смене дня
    LaunchedEffect(selectedDate) {
        lazyListState.scrollToItem(0)
    }

    val taskPagerState = rememberPagerState(initialPage = initialPage) { 10000 }
    // Свайп task pager → обновляем selectedDate + прокручиваем календарь

    val todayMonday = today.minusDays(today.dayOfWeek.value.toLong() - 1)

    LaunchedEffect(selectedDate) {
        val selectedMonday = selectedDate.minusDays(selectedDate.dayOfWeek.value.toLong() - 1)
        val weeksBetween = ChronoUnit.WEEKS.between(todayMonday, selectedMonday)
        val targetWeekPage = initialPage + weeksBetween.toInt()

        if (pagerState.currentPage != targetWeekPage) {
            weekPagerProgrammaticScroll = true
            pagerState.scrollToPage(targetWeekPage)
            weekPagerProgrammaticScroll = false
        }
    }

    LaunchedEffect(selectedDate) {
        val dayOffset = ChronoUnit.DAYS.between(today, selectedDate).toInt()
        val targetDayPage = initialPage + dayOffset

        if (taskPagerState.currentPage != targetDayPage) {
            taskPagerProgrammaticScroll = true
            taskPagerState.scrollToPage(targetDayPage)
            taskPagerProgrammaticScroll = false
        }
    }

    LaunchedEffect(taskPagerState.settledPage) {
        if (taskPagerProgrammaticScroll) return@LaunchedEffect

        val newDate = today.plusDays((taskPagerState.settledPage - initialPage).toLong())
        if (newDate != selectedDate) {
            selectedDate = newDate
        }
    }

    LaunchedEffect(pagerState.settledPage) {
        if (weekPagerProgrammaticScroll) return@LaunchedEffect

        val weekOffset = pagerState.settledPage - initialPage
        val mondayOfVisibleWeek = todayMonday.plusWeeks(weekOffset.toLong())

        val selectedDayOffset = selectedDate.dayOfWeek.value.toLong() - 1
        val newDate = mondayOfVisibleWeek.plusDays(selectedDayOffset)

        if (newDate != selectedDate) {
            selectedDate = newDate
        }
    }
    // Смена selectedDate снаружи (тап по дню, date picker) → прокручиваем task pager
//    LaunchedEffect(selectedDate) {
//        val dayOffset = ChronoUnit.DAYS.between(today, selectedDate).toInt()
//        val targetPage = initialPage + dayOffset
//        if (taskPagerState.settledPage != targetPage) {
//            taskPagerState.scrollToPage(targetPage)
//        }
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
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
            else "Задачи на ${selectedDate.dayOfMonth} ${
                selectedDate.month.getDisplayName(
                    DateTextStyle.FULL,
                    Locale("ru")
                )
            }",
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )

        Box(modifier = Modifier.weight(1f)) {
            HorizontalPager(
                state = taskPagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1 // предзагружаем соседние дни
            ) { page ->
                val pageDate = today.plusDays((page - initialPage).toLong())
                TaskListPage(
                    date = pageDate,
                    taskDao = taskDao,
                    coroutineScope = coroutineScope,
                    onTaskClick = { taskWithSubtasks ->
                        editingTask = taskWithSubtasks
                    }
                )
            }
            FloatingActionButton(
                onClick = { showAddTaskSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(36.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить", tint = Color.White)
            }
        }
    }

    if (showAddTaskSheet) {
        AddTaskDialog(
            onDismiss = { showAddTaskSheet = false },
            onTaskAdded = {
                    title, important, urgent, diff, subTasksList, deadline, remind, weekdays ->
                coroutineScope.launch {
                    val startOfDay = selectedDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                    val endOfDay = selectedDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant().toEpochMilli()

                    // Получаем количество задач на выбранный день
                    val count = taskDao.getTaskCountForDay(startOfDay, endOfDay)
                    val position = count + 1
                    // 1. Создаем основную задачу
                    val newTask = TaskEntity(
                        title = title,
                        date_of_task = selectedDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
                        position = position,
                        is_important = important,
                        is_urgency = urgent,
                        difficulty = if (diff == 0) 1 else diff,
                        is_completed = false,
                        deadline_date = deadline?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
                        remind_date = remind?.toInstant(ZoneOffset.UTC)?.toEpochMilli(),
                        repeat_mon = weekdays.contains(DayOfWeek.MONDAY),
                        repeat_tue = weekdays.contains(DayOfWeek.TUESDAY),
                        repeat_wed = weekdays.contains(DayOfWeek.WEDNESDAY),
                        repeat_thu = weekdays.contains(DayOfWeek.THURSDAY),
                        repeat_fri = weekdays.contains(DayOfWeek.FRIDAY),
                        repeat_sat = weekdays.contains(DayOfWeek.SATURDAY),
                        repeat_sun = weekdays.contains(DayOfWeek.SUNDAY)
                    )

                    // 2. Вставляем задачу и получаем её ID
                    val taskId = taskDao.insertTask(newTask)

                    // 3. Сохраняем подзадачи, привязывая их к taskId
                    subTasksList.forEach { subTaskData ->
                        val subTaskText = subTaskData.textValue.text.trim()
                        // Проверяем на пустую строку (добавлен безопасный вызов ?. и импорт выше)
//                            if (subTaskData.textValue.text.isNotBlank()) {
                        taskDao.insertSubTask(
                            SubTaskEntity(
                                task_id = taskId.toInt(),
                                subtask_title = subTaskText,
                                is_completed = subTaskData.isDone
                            )
                        )
                    }

                    showAddTaskSheet = false
                }
            }
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

    if (editingTask != null) {
        EditTaskDialog(
            taskWithSubtasks = editingTask!!,
            onDismiss = { editingTask = null },
            onSave = { updatedTask, updatedSubtasks ->
                coroutineScope.launch {
                    taskDao.updateTaskWithSubtasks(updatedTask, updatedSubtasks)
                    editingTask = null
                }
            }
        )
    }
}
//}

fun getRemainingTimeText(deadlineMs: Long?, currentTimeMs: Long): String? {
    if (deadlineMs == null || deadlineMs == 0L) return null
    val diff = deadlineMs - currentTimeMs
    if (diff <= 0) return "просрочено"

    val days = diff / (1000 * 60 * 60 * 24)
    val hours = (diff / (1000 * 60 * 60)) % 24
    val minutes = (diff / (1000 * 60)) % 60

    return when {
        days > 0 -> "осталось $days дн $hours ч"
        hours > 0 -> "осталось $hours ч $minutes мин"
        else -> "осталось $minutes мин"
    }
}

@Composable
fun TaskListPage(
    date: LocalDate,
    taskDao: TaskDao,
    coroutineScope: CoroutineScope,
    onTaskClick: (TaskWithSubtasks) -> Unit
) {
    val startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    val endOfDay = date.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant().toEpochMilli()

    val tasksFlow = remember(date) { taskDao.getTasksWithSubtasksForDay(startOfDay, endOfDay) }
    val tasksList by tasksFlow.collectAsState(initial = emptyList())

    var orderedTasks by remember(date, tasksList) {
        mutableStateOf(tasksList.sortedBy { it.task.position })
    }

    val hapticFeedback = LocalHapticFeedback.current
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        orderedTasks = orderedTasks.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(orderedTasks, key = { it.task.id }) { item ->
            ReorderableItem(reorderableLazyListState, key = item.task.id) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 6.dp else 0.dp)
                Surface(shadowElevation = elevation) {
                    TaskItemView(
                        task = item.task,
                        subtasks = item.subtasks,
                        reorderScope = this,
                        onClick = { onTaskClick(item) },
                        onDragStopped = {
                            coroutineScope.launch {
                                orderedTasks.forEachIndexed { index, t ->
                                    taskDao.updateTask(t.task.copy(position = index + 1))
                                }
                            }
                        },
                        onToggleCompleted = { updatedTask, updatedSubtasks ->
                            coroutineScope.launch {
                                taskDao.updateTask(updatedTask)
                                updatedSubtasks.forEach { taskDao.updateSubTask(it) }
                            }
                        },
                        onSubtaskToggle = { updatedSubtask ->
                            coroutineScope.launch { taskDao.updateSubTask(updatedSubtask) }
                        },
                        onDeleteTask = { taskToDelete ->
                            coroutineScope.launch {
                                taskDao.deleteTask(taskToDelete)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItemView(
    task: TaskEntity,
    subtasks: List<SubTaskEntity>,
    reorderScope: ReorderableCollectionItemScope,
    onClick: () -> Unit,
    onToggleCompleted: (TaskEntity, List<SubTaskEntity>) -> Unit,
    onSubtaskToggle: (SubTaskEntity) -> Unit,
    onDragStopped: () -> Unit,
    onDeleteTask: (TaskEntity) -> Unit
) {
    var showDeleteDialog by remember(task.id) { mutableStateOf(false) }
    var currentTimeMs by remember { mutableStateOf(System.currentTimeMillis()) }
    val activeGreen = Color(0xFF4CAF50)

    if (showDeleteDialog) {
        Dialog(
            onDismissRequest = { showDeleteDialog = false }
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.wrapContentSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Удалить задачу?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            onClick = { showDeleteDialog = false },
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Отмена",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Surface(
                            onClick = {
                                showDeleteDialog = false
                                onDeleteTask(task)
                            },
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.error
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "ОК",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val now = System.currentTimeMillis()
            val seconds = (now / 1000) % 60
            val millisToNextMinute = ((60 - seconds) * 1000) - (now % 1000)
            delay(millisToNextMinute)
            currentTimeMs = System.currentTimeMillis()
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f), // фон задачи
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 8.dp, vertical = 8.dp)

        ) {
            // Ряд 1: Чекбокс + Название
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Кастомный чекбокс для задачи
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (task.is_completed == true) activeGreen.copy(alpha = 0.1f)
                            else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            2.dp,
                            if (task.is_completed == true) activeGreen else Color.LightGray,
                            CircleShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            val newCompleted = !(task.is_completed ?: false)
                            val updatedTask = task.copy(is_completed = newCompleted)
                            val updatedSubtasks = if (newCompleted) {
                                subtasks.map { it.copy(is_completed = true) }
                            } else {
                                subtasks
                            }
                            onToggleCompleted(updatedTask, updatedSubtasks)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (task.is_completed == true) {
                        Icon(
                            Icons.Default.Check,
                            null,
                            modifier = Modifier.size(16.dp),
                            tint = activeGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))


                Text(
                    text = task.title,
                    fontSize = 17.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (task.is_completed == true) TextDecoration.LineThrough else null,
                    color = if (task.is_completed == true) activeGreen else MaterialTheme.colorScheme.onBackground
                )

                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить задачу",
                        tint = Color(0xFFD32F2F)
                    )
                }

                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Переместить",
                    tint = Color.LightGray,
                    modifier = with(reorderScope) {
                        Modifier.draggableHandle(
                            onDragStopped = { onDragStopped() }
                        )
                    }
                )
            }


            // Ряд 2: Плашки и Дедлайн

            Row(
                modifier = Modifier
                    .padding(start = 36.dp, top = 4.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val tags = buildList {
                    if (task.is_important == true) add("важно" to Color.Red)
                    if (task.is_urgency == true) add("срочно" to Color(0xFFFFA500))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    tags.forEach { (text, color) ->
                        StatusTag(
                            text = text,
                            color = color
                        )
                    }
                }

                getRemainingTimeText(task.deadline_date, currentTimeMs)?.let {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        color = if (it == "просрочено") Color.Red else Color.Gray
                    )
                }
            }

            // Ряд 3+: Подзадачи
            subtasks.forEach { subtask ->
                Row(
                    modifier = Modifier
                        .padding(start = 36.dp, top = 6.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Кастомный чекбокс для подзадачи
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(
                                color = if (subtask.is_completed) activeGreen.copy(
                                    alpha = 0.1f
                                ) else Color.Transparent,
                                shape = CircleShape
                            )
                            .border(
                                1.5.dp,
                                if (subtask.is_completed) activeGreen else Color.LightGray,
                                CircleShape
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onSubtaskToggle(subtask.copy(is_completed = !subtask.is_completed))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (subtask.is_completed) {
                            Icon(
                                Icons.Default.Check,
                                null,
                                modifier = Modifier.size(12.dp),
                                tint = activeGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = subtask.subtask_title ?: "",
                        fontSize = 14.sp,
                        color = if (subtask.is_completed) activeGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = if (subtask.is_completed) TextDecoration.LineThrough else null
                    )
                }
            }
        }
    }
}

//шрифт sanfransicso
@Composable
fun StatusTag(
    text: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .border(
                width = 0.5.dp,
                color = color,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 6.dp, vertical = 1.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onTaskAdded: (
        title: String,
        important: Boolean,
        urgent: Boolean,
        difficulty: Int,
        subTasks: List<SubTask>,
        deadline: LocalDateTime?,   // Добавлено
        remind: LocalDateTime?,     // Добавлено
        weekdays: Set<DayOfWeek>    // Добавлено
    ) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AddTaskSheetContent(
                onDismiss = onDismiss,
                onTaskAdded = onTaskAdded
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTaskSheetContent(
    onDismiss: () -> Unit,
    onTaskAdded: (
        title: String,
        important: Boolean,
        urgent: Boolean,
        difficulty: Int,
        subTasks: List<SubTask>,
        deadline: LocalDateTime?,   // Добавлено
        remind: LocalDateTime?,     // Добавлено
        weekdays: Set<DayOfWeek>    // Добавлено
    ) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
//    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    var taskName by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    var showReminderDialog by remember { mutableStateOf(false) }
    //
    var deadlineDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var remindDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var selectedDays by remember { mutableStateOf<Set<DayOfWeek>>(emptySet()) }

    // Подзадачи
    var subTasks by remember { mutableStateOf(listOf<SubTask>()) }
    val scrollState = rememberScrollState()

    // Важность / Срочность
    var isImportant by remember { mutableStateOf(false) }
    var isUrgent by remember { mutableStateOf(false) }

    // Сложность (1..5, 0 – не выбрано)
    var difficulty by remember { mutableIntStateOf(1) }

    // Цель
    var selectedGoal by remember { mutableStateOf<String?>(null) }
    var showGoalSelector by remember { mutableStateOf(false) }

    // Для позиционирования Popup
    val density = LocalDensity.current
    var goalButtonBounds by remember { mutableStateOf<Rect?>(null) }

    // ── Появление sheet: ждём первого кадра с клавиатурой ────────────────────
    val imeBottom = WindowInsets.ime.getBottom(density)
    var hasKeyboardShown by remember { mutableStateOf(false) }
    if (imeBottom > 0) hasKeyboardShown = true

    var isDismissing by remember { mutableStateOf(false) }
    var AnimatabletranslationY = remember { Animatable(0f) }

    fun triggerDismiss(pendingTask: String? = null) {
        if (isDismissing) return
        isDismissing = true
        keyboardController?.hide()
        coroutineScope.launch {
            AnimatabletranslationY.animateTo(
                targetValue = 1200f,
                animationSpec = tween(durationMillis = 260, easing = FastOutLinearInEasing)
            )
            pendingTask?.let {
                onTaskAdded(it
                    , isImportant, isUrgent, difficulty, subTasks,
                    deadlineDateTime, remindDateTime, selectedDays
                )}
            onDismiss()
        }
    }

    val scrimAlpha by animateFloatAsState(
        targetValue = when {
            isDismissing -> 0f
            hasKeyboardShown -> 0.45f
            else -> 0f
        },
        animationSpec = tween(220),
        label = "scrim_alpha"
    )

    val sheetAlpha by animateFloatAsState(
        targetValue = if (hasKeyboardShown && !isDismissing) 1f else 0f,
        animationSpec = tween(180),
        label = "sheet_alpha"
    )

    LaunchedEffect(Unit) {
        delay(16)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    // Scrim
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(scrimAlpha)
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { triggerDismiss() }
    )

    // Основное окно
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(sheetAlpha)
            .graphicsLayer { translationY = AnimatabletranslationY.value }
            .semantics { testTag = "add_task_sheet" }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showGoalSelector = false
            },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Drag handle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            if (!isDismissing) {
                                coroutineScope.launch {
                                    AnimatabletranslationY.snapTo(
                                        (AnimatabletranslationY.value + delta).coerceAtLeast(0f)
                                    )
                                }
                            }
                        },
                        onDragStopped = { velocity ->
                            if (isDismissing) return@draggable
                            if (AnimatabletranslationY.value > 600f || velocity > 5000f) {
                                triggerDismiss()
                            } else {
                                coroutineScope.launch {
                                    AnimatabletranslationY.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .background(Color.LightGray, RoundedCornerShape(2.dp))
                )
            }

            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Spacer(modifier = Modifier.height(16.dp))

                // Поле ввода задачи
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    placeholder = { Text("Новая задача...", fontSize = 16.sp, color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .semantics { testTag = "task_name_input" },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    leadingIcon = {
                        Surface(
                            onClick = { /* TODO */ },
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(38.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Edit,
                                    null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                subTasks = subTasks + SubTask()
                                coroutineScope.launch {
                                    delay(50)
                                    scrollState.animateScrollTo(scrollState.maxValue + 500)
                                }
                            },
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                "Добавить подзадачу",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                    )
                )

                // Список подзадач
                if (subTasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.heightIn(max = 110.dp)) {
                        Column(modifier = Modifier.verticalScroll(scrollState)) {
                            subTasks.forEach { subTask ->
                                key(subTask.id) {
                                    var isVisible by remember { mutableStateOf(false) }
                                    LaunchedEffect(Unit) { isVisible = true }
                                    AnimatedVisibility(
                                        visible = isVisible,
                                        enter = expandVertically(tween(300)) + fadeIn(),
                                        exit = shrinkVertically() + fadeOut()
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
                                                    if (it.id == subTask.id) it.copy(isDone = !it.isDone) else it
                                                }
                                            },
                                            onDelete = {
                                                isVisible = false
                                                coroutineScope.launch {
                                                    delay(200)   // ждём завершения анимации исчезновения
                                                    subTasks = subTasks.filter { it.id != subTask.id }
                                                    if (imeBottom>0) {
                                                        // Возвращаем фокус основному полю, только если клавиатура была открыта
                                                        focusRequester.requestFocus()
                                                        keyboardController?.show()
                                                    }
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

                // Блок Важность / Срочность + Сложность и остальные элементы
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Левая колонка: кнопки "Важно" и "Срочно"
                    Column(
                        modifier = Modifier.width(80.dp),
                        verticalArrangement = Arrangement.Top,   // прижимаем к верху
                    ) {
                        FilterChip(
                            selected = isImportant,
                            onClick = { isImportant = !isImportant },
                            label = { Text("Важно", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().height(43.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFFA726)
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FilterChip(
                            selected = isUrgent,
                            onClick = { isUrgent = !isUrgent },
                            label = { Text("Срочно", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().height(43.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFEF5350)
                            )
                        )
                    }

                    // Правая колонка: всё остальное
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Top
                    ) {
                        // Первая строка: шкала сложности + кнопка цели
                        Row(
                            modifier = Modifier.fillMaxWidth().height(43.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Блок шкалы сложности (подписи над квадратиками)
                            Column(
                                modifier = Modifier.weight(1.5f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Легко", fontSize = 10.sp, color = Color.Gray)
                                    Text("Сложно", fontSize = 10.sp, color = Color.Gray)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    for (level in 1..5) {
                                        Box(
                                            modifier = Modifier
                                                .size(30.dp)   // квадратики побольше
                                                .background(
                                                    if (level <= difficulty) MaterialTheme.colorScheme.primary
                                                    else Color.LightGray.copy(alpha = 0.3f),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .clickable { difficulty = level }
                                        )
                                        if (level < 5) Spacer(modifier = Modifier.width(1.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Кнопка цели (занимает оставшееся место)
                            Box(modifier = Modifier.weight(1f)) {
                                val goalText = selectedGoal ?: "Цель"
                                val displayGoalText =
                                    if (goalText.length > 15) goalText.take(15) + "…" else goalText

                                Surface(
                                    onClick = { showGoalSelector = true },
                                    modifier = Modifier
                                        .height(43.dp)
                                        .fillMaxWidth()
                                        .onGloballyPositioned {
                                            goalButtonBounds = it.boundsInWindow()
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            null,
                                            modifier = Modifier.size(18.dp),
                                            tint = Color.Gray
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            displayGoalText,
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            maxLines = 1
                                        )
                                    }
                                }

                                if (showGoalSelector && goalButtonBounds != null) {
                                    Popup(
                                        alignment = Alignment.TopCenter,
                                        offset = IntOffset(10, -goalButtonBounds!!.height.toInt() - 8),
                                        onDismissRequest = { showGoalSelector = false },
                                        properties = PopupProperties(focusable = false)   // ← отключаем захват фокуса
                                    ) {
                                        // После открытия попапа возвращаем фокус основному полю,
                                        // но только если клавиатура была видна до этого
                                        LaunchedEffect(Unit) {
                                            if (imeBottom > 0) {
                                                focusRequester.requestFocus()
                                                keyboardController?.show()
                                            }
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.surface,
                                            shadowElevation = 8.dp,
                                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
                                        ) {
                                            Column(modifier = Modifier.width(300.dp).padding(3.dp)) {
                                                GoalItem("Без цели", selectedGoal == null) {
                                                    selectedGoal = null
                                                    showGoalSelector = false
                                                }
                                                GoalItem("Работа", selectedGoal == "Работа") {
                                                    selectedGoal = "Работа"
                                                    showGoalSelector = false
                                                }
                                                GoalItem("Личное", selectedGoal == "Личное") {
                                                    selectedGoal = "Личное"
                                                    showGoalSelector = false
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Вторая строка: напоминание (растянуто) + кнопка отправки
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Кнопка напоминаний (растягивается)
                            Surface(
                                onClick = { showReminderDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(43.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                )
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.TwoTone.DateRange,
                                        null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            // Кнопка "Запланировать" (фиксированный размер)
                            Surface(
                                onClick = { if (taskName.isNotBlank()) triggerDismiss(taskName) },
                                modifier = Modifier
                                    .size(43.dp)
                                    .semantics { testTag = "add_task_button" },
                                shape = RoundedCornerShape(12.dp),
                                color = if (taskName.isNotBlank())
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                enabled = taskName.isNotBlank()
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.ArrowForward,
                                        "Запланировать",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            if (showReminderDialog) {
                                ReminderPickerDialog(
                                    initialDate = LocalDate.now(), // или сохранённое значение
                                    initialTime = LocalTime.now().plusHours(1),
                                    onDismiss = { showReminderDialog = false },
                                    onSave = { date, time, remindValue, remindUnit, weekdays ->
                                        // 1. Сохраняем дедлайн (дата + время)
                                        val deadline = LocalDateTime.of(date, time)
                                        deadlineDateTime = deadline

                                        // 2. Сохраняем дни повтора
                                        selectedDays = weekdays

                                        // 3. Вычисляем время напоминания
                                        remindDateTime = when (remindUnit) {
                                            ReminderUnit.MINUTES -> deadline.minusMinutes(remindValue.toLong())
                                            ReminderUnit.HOURS -> deadline.minusHours(remindValue.toLong())
                                            ReminderUnit.DAYS -> deadline.minusDays(remindValue.toLong())
                                            else -> deadline
                                        }

                                        showReminderDialog = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditTaskDialog(
    taskWithSubtasks: TaskWithSubtasks,
    onDismiss: () -> Unit,
    onSave: (updatedTask: TaskEntity, updatedSubtasks: List<SubTaskEntity>) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            EditTaskSheetContent(
                taskWithSubtasks = taskWithSubtasks,
                onDismiss = onDismiss,
                onSave = onSave
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditTaskSheetContent(
    taskWithSubtasks: TaskWithSubtasks,
    onDismiss: () -> Unit,
    onSave: (updatedTask: TaskEntity, updatedSubtasks: List<SubTaskEntity>) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val density = LocalDensity.current

    val task = taskWithSubtasks.task

    var taskName by remember(task.id) { mutableStateOf(task.title) }
    var showReminderDialog by remember(task.id) { mutableStateOf(false) }

    var deadlineDateTime by remember(task.id) {
        mutableStateOf(task.deadline_date?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
        })
    }
    var remindDateTime by remember(task.id) {
        mutableStateOf(task.remind_date?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
        })
    }

    var selectedDays by remember(task.id) {
        mutableStateOf(
            buildSet {
                if (task.repeat_mon) add(DayOfWeek.MONDAY)
                if (task.repeat_tue) add(DayOfWeek.TUESDAY)
                if (task.repeat_wed) add(DayOfWeek.WEDNESDAY)
                if (task.repeat_thu) add(DayOfWeek.THURSDAY)
                if (task.repeat_fri) add(DayOfWeek.FRIDAY)
                if (task.repeat_sat) add(DayOfWeek.SATURDAY)
                if (task.repeat_sun) add(DayOfWeek.SUNDAY)
            }
        )
    }

    var subTasks by remember(task.id) {
        mutableStateOf(
            taskWithSubtasks.subtasks.map { it.toUiModel() }
                .ifEmpty { emptyList() }
        )
    }

    val scrollState = rememberScrollState()

    var isImportant by remember(task.id) { mutableStateOf(task.is_important == true) }
    var isUrgent by remember(task.id) { mutableStateOf(task.is_urgency == true) }
    var difficulty by remember(task.id) { mutableIntStateOf(task.difficulty ?: 0) }

    var selectedGoal by remember(task.id) { mutableStateOf<String?>(null) }
    var showGoalSelector by remember(task.id) { mutableStateOf(false) }
    var goalButtonBounds by remember(task.id) { mutableStateOf<Rect?>(null) }

    val imeBottom = WindowInsets.ime.getBottom(density)
    var hasKeyboardShown by remember { mutableStateOf(false) }
    if (imeBottom > 0) hasKeyboardShown = true

    var isDismissing by remember { mutableStateOf(false) }
    var AnimatabletranslationY = remember { Animatable(0f) }
    val canSave = taskName.trim().isNotEmpty()

    fun triggerDismiss(pendingTask: String? = null) {
        if (isDismissing) return
        isDismissing = true
        keyboardController?.hide()

        coroutineScope.launch {
            AnimatabletranslationY.animateTo(
                targetValue = 1200f,
                animationSpec = tween(durationMillis = 260, easing = FastOutLinearInEasing)
            )

            pendingTask?.let { title ->
                val updatedTask = task.copy(
                    title = title,
                    is_important = isImportant,
                    is_urgency = isUrgent,
                    difficulty = if (difficulty == 0) 1 else difficulty,
                    deadline_date = deadlineDateTime
                        ?.atZone(ZoneId.systemDefault())
                        ?.toInstant()
                        ?.toEpochMilli(),
                    remind_date = remindDateTime
                        ?.toInstant(ZoneOffset.UTC)
                        ?.toEpochMilli(),
                    repeat_mon = selectedDays.contains(DayOfWeek.MONDAY),
                    repeat_tue = selectedDays.contains(DayOfWeek.TUESDAY),
                    repeat_wed = selectedDays.contains(DayOfWeek.WEDNESDAY),
                    repeat_thu = selectedDays.contains(DayOfWeek.THURSDAY),
                    repeat_fri = selectedDays.contains(DayOfWeek.FRIDAY),
                    repeat_sat = selectedDays.contains(DayOfWeek.SATURDAY),
                    repeat_sun = selectedDays.contains(DayOfWeek.SUNDAY)
                )

                val updatedSubtasks = subTasks
                    .map { draft ->
                        SubTaskEntity(
                            task_id = task.id,
                            subtask_title = draft.textValue.text.trim(),
                            is_completed = draft.isDone
                        )
                    }

                onSave(updatedTask, updatedSubtasks)
            }

            onDismiss()
        }
    }

    val scrimAlpha by animateFloatAsState(
        targetValue = when {
            isDismissing -> 0f
            hasKeyboardShown -> 0.45f
            else -> 0f
        },
        animationSpec = tween(220),
        label = "scrim_alpha"
    )

    val sheetAlpha by animateFloatAsState(
        targetValue = if (hasKeyboardShown && !isDismissing) 1f else 0f,
        animationSpec = tween(180),
        label = "sheet_alpha"
    )

    LaunchedEffect(Unit) {
        delay(16)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(scrimAlpha)
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { triggerDismiss() }
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(sheetAlpha)
            .graphicsLayer { translationY = AnimatabletranslationY.value }
            .semantics { testTag = "edit_task_sheet" }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showGoalSelector = false
            },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            if (!isDismissing) {
                                coroutineScope.launch {
                                    AnimatabletranslationY.snapTo((AnimatabletranslationY.value + delta).coerceAtLeast(0f))
                                }
                            }
                        },
                        onDragStopped = { velocity ->
                            if (isDismissing) return@draggable
                            if (AnimatabletranslationY.value > 600f || velocity > 5000f) {
                                triggerDismiss()
                            } else {
                                coroutineScope.launch {
                                    AnimatabletranslationY.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .background(Color.LightGray, RoundedCornerShape(2.dp))
                )
            }

            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    placeholder = { Text("Название задачи...", fontSize = 16.sp, color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    leadingIcon = {
                        Surface(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(38.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Edit,
                                    null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                subTasks = subTasks + SubTask()
                                coroutineScope.launch {
                                    delay(50)
                                    scrollState.animateScrollTo(scrollState.maxValue + 500)
                                }
                            },
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                "Добавить подзадачу",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                    )
                )

                if (subTasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.heightIn(max = 110.dp)) {
                        Column(modifier = Modifier.verticalScroll(scrollState)) {
                            subTasks.forEach { subTask ->
                                key(subTask.id) {
                                    var isVisible by remember { mutableStateOf(true) }
                                    AnimatedVisibility(
                                        visible = isVisible,
                                        enter = expandVertically(tween(300)) + fadeIn(),
                                        exit = shrinkVertically() + fadeOut()
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
                                                    if (it.id == subTask.id) it.copy(isDone = !it.isDone) else it
                                                }
                                            },
                                            onDelete = {
                                                isVisible = false
                                                coroutineScope.launch {
                                                    delay(200)
                                                    subTasks = subTasks.filter { it.id != subTask.id }
                                                    if (imeBottom > 0) {
                                                        focusRequester.requestFocus()
                                                        keyboardController?.show()
                                                    }
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier.width(80.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        FilterChip(
                            selected = isImportant,
                            onClick = { isImportant = !isImportant },
                            label = { Text("Важно", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().height(43.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFFA726)
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FilterChip(
                            selected = isUrgent,
                            onClick = { isUrgent = !isUrgent },
                            label = { Text("Срочно", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().height(43.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFEF5350)
                            )
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(43.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1.5f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Легко", fontSize = 10.sp, color = Color.Gray)
                                    Text("Сложно", fontSize = 10.sp, color = Color.Gray)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    for (level in 1..5) {
                                        Box(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .background(
                                                    if (level <= difficulty) MaterialTheme.colorScheme.primary
                                                    else Color.LightGray.copy(alpha = 0.3f),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .clickable { difficulty = level }
                                        )
                                        if (level < 5) Spacer(modifier = Modifier.width(1.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(modifier = Modifier.weight(1f)) {
                                val goalText = selectedGoal ?: "Цель"
                                Surface(
                                    onClick = { showGoalSelector = true },
                                    modifier = Modifier
                                        .height(43.dp)
                                        .fillMaxWidth()
                                        .onGloballyPositioned {
                                            goalButtonBounds = it.boundsInWindow()
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            null,
                                            modifier = Modifier.size(18.dp),
                                            tint = Color.Gray
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            if (goalText.length > 15) goalText.take(15) + "…" else goalText,
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            maxLines = 1
                                        )
                                    }
                                }
                                if (showGoalSelector && goalButtonBounds != null) {
                                    Popup(
                                        alignment = Alignment.TopCenter,
                                        offset = IntOffset(10, -goalButtonBounds!!.height.toInt() - 8),
                                        onDismissRequest = { showGoalSelector = false },
                                        properties = PopupProperties(focusable = false)   // ← отключаем захват фокуса
                                    ) {
                                        // После открытия попапа возвращаем фокус основному полю,
                                        // но только если клавиатура была видна до этого
                                        LaunchedEffect(Unit) {
                                            if (imeBottom > 0) {
                                                focusRequester.requestFocus()
                                                keyboardController?.show()
                                            }
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.surface,
                                            shadowElevation = 8.dp,
                                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
                                        ) {
                                            Column(modifier = Modifier.width(300.dp).padding(3.dp)) {
                                                GoalItem("Без цели", selectedGoal == null) {
                                                    selectedGoal = null
                                                    showGoalSelector = false
                                                }
                                                GoalItem("Работа", selectedGoal == "Работа") {
                                                    selectedGoal = "Работа"
                                                    showGoalSelector = false
                                                }
                                                GoalItem("Личное", selectedGoal == "Личное") {
                                                    selectedGoal = "Личное"
                                                    showGoalSelector = false
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                onClick = { showReminderDialog = true },
                                modifier = Modifier.weight(1f).height(43.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                )
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.TwoTone.DateRange,
                                        null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Surface(
                                onClick = { if (canSave) triggerDismiss(taskName.trim()) },
                                enabled = canSave,
                                modifier = Modifier
                                    .size(43.dp)
                                    .semantics { testTag = "save_task_button" },
                                shape = RoundedCornerShape(12.dp),
                                color = if (canSave) MaterialTheme.colorScheme.primary else Color.Gray
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Check,
                                        "Сохранить",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showReminderDialog) {
        ReminderPickerDialog(
            initialDate = deadlineDateTime?.toLocalDate() ?: LocalDate.now(),
            initialTime = deadlineDateTime?.toLocalTime() ?: LocalTime.now().plusHours(1),
            onDismiss = { showReminderDialog = false },
            onSave = { date, time, remindValue, remindUnit, weekdays ->
                val deadline = LocalDateTime.of(date, time)
                deadlineDateTime = deadline
                selectedDays = weekdays
                remindDateTime = when (remindUnit) {
                    ReminderUnit.MINUTES -> deadline.minusMinutes(remindValue.toLong())
                    ReminderUnit.HOURS -> deadline.minusHours(remindValue.toLong())
                    ReminderUnit.DAYS -> deadline.minusDays(remindValue.toLong())
                    else -> deadline
                }
                showReminderDialog = false
            }
        )
    }
}

@Stable
data class SubTask(
    val id: Long = System.currentTimeMillis() + (0..1000).random(),
    val dbId: Int? = null,
    var textValue: TextFieldValue = TextFieldValue(""), // Используем TextFieldValue
    var isDone: Boolean = false
)

private fun SubTaskEntity.toUiModel(): SubTask {
    return SubTask(
        id = this.id.toLong(),      // если у entity есть id
        dbId = this.id,
        textValue = TextFieldValue(this.subtask_title ?: ""),
        isDone = this.is_completed
    )
}

private fun List<SubTask>.toEntities(taskId: Int): List<SubTaskEntity> {
    return map { draft ->
        SubTaskEntity(
            task_id = taskId,
            subtask_title = draft.textValue.text.trim(),
            is_completed = draft.isDone
        )
    }
}

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReminderPickerDialog(
    initialDate: LocalDate,
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onSave: (LocalDate, LocalTime, Int, ReminderUnit, Set<DayOfWeek>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentMonth by remember { mutableStateOf(initialDate.withDayOfMonth(1)) }
    var selectedTime by remember { mutableStateOf(LocalTime.now().plusHours(1)) }
    var remindBeforeValue by remember { mutableStateOf(5) }
    var remindBeforeUnit by remember { mutableStateOf(ReminderUnit.MINUTES) }
    var selectedWeekdays by remember { mutableStateOf<Set<DayOfWeek>>(emptySet()) }
    var resetTimeTrigger by remember { mutableIntStateOf(0) }


    var isContentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isContentVisible = true }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Содержимое (Column с календарём, колёсами, кнопками) без изменений
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
                    .animateContentSize(tween(200))
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                // === Строка с заголовком, сводкой и кнопкой "Сбросить" ===
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Выполнить до:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val formatted = remember(selectedDate, selectedTime) {
                            val day = selectedDate.dayOfMonth
                            val month = selectedDate.month.getDisplayName(
                                DateTextStyle.SHORT,
                                Locale("ru")
                            )
                            val year = selectedDate.year
                            val timeStr = String.format(
                                "%02d:%02d",
                                selectedTime.hour,
                                selectedTime.minute
                            )
                            "$timeStr $day $month $year"
                        }
                        Text(
                            text = formatted,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1
                        )
                    }
                }

                // === Время + Календарь ===
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Колонка с временем и кнопкой "Сбросить"
                    Column(
                        modifier = Modifier.weight(0.9f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TimeWheel(
                            initialHour = selectedTime.hour,
                            initialMinute = selectedTime.minute,
                            resetTrigger = resetTimeTrigger,
                            onTimeChanged = { hour, minute ->
                                selectedTime = LocalTime.of(hour, minute)
                            }
                        )

                        TextButton(
                            onClick = {
                                val now = LocalDate.now()
                                selectedDate = now
                                selectedTime = LocalTime.now().plusHours(1)
                                currentMonth = now.withDayOfMonth(1)
                                resetTimeTrigger++
                            },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text(
                                "Сбросить",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    // Календарь
                    Box(modifier = Modifier.weight(1.2f)) {
                        CompactDatePicker(
                            selectedDate = selectedDate,
                            currentMonth = currentMonth,
                            onMonthChanged = { currentMonth = it },
                            onDateSelected = { selectedDate = it }
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(top = 0.dp, bottom = 8.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
                )
                // === "Напомнить за:" ===
                Text(
                    "Напомнить за:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // === Два колеса и надпись "ДО" ===
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val numbers = remember { (0..60).toList() }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        WheelPicker(
                            items = numbers,
                            initialItem = remindBeforeValue,
                            itemHeight = 36.dp,
                            visibleItems = 5,
                            centerFontSize = 20.sp,
                            secondaryFontSize = 16.sp,
                            onItemSelected = { remindBeforeValue = it }
                        )
                    }

                    val units = remember { ReminderUnit.values().toList() }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        WheelPicker(
                            items = units,
                            initialItem = remindBeforeUnit,
                            itemHeight = 36.dp,
                            visibleItems = 5,
                            centerFontSize = 20.sp,
                            secondaryFontSize = 16.sp,
                            formatter = { it.displayName },
                            onItemSelected = { remindBeforeUnit = it }
                        )
                    }

                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            "ДО",
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                //                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(top = 6.dp, bottom = 8.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
                )
                // === "Повторять каждые:" ===
                Text(
                    "Повторять задачу каждый:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // === Дни недели ===
                WeekdaySelector(
                    selectedDays = selectedWeekdays,
                    onSelectionChanged = { selectedWeekdays = it }
                )

                Spacer(modifier = Modifier.height(15.dp))

                // === Кнопки "Отмена" и "Сохранить" ===
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Отмена", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = {
                            onSave(
                                selectedDate,
                                selectedTime,
                                remindBeforeValue,
                                remindBeforeUnit,
                                selectedWeekdays
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Сохранить", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TimeWheel(
    initialHour: Int,
    initialMinute: Int,
    resetTrigger: Int = 0,
    onTimeChanged: (hour: Int, minute: Int) -> Unit
) {
    val hours = remember { (0..23).toList() }
    val minutes = remember { (0..59).toList() }

    val hourState = rememberLazyListState(initialFirstVisibleItemIndex = hours.indexOf(initialHour))
    val minuteState = rememberLazyListState(initialFirstVisibleItemIndex = minutes.indexOf(initialMinute))

    // Прокрутка при сбросе
    LaunchedEffect(resetTrigger) {
        if (resetTrigger > 0) {
            hourState.animateScrollToItem(hours.indexOf(initialHour))
            minuteState.animateScrollToItem(minutes.indexOf(initialMinute))
        }
    }

    // Определяем центральные элементы
    val currentHour by remember {
        derivedStateOf {
            val layoutInfo = hourState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) initialHour
            else {
                val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItems.minByOrNull { abs((it.offset + it.size / 2) - viewportCenter) }
                    ?.let { hours[it.index] } ?: initialHour
            }
        }
    }
    val currentMinute by remember {
        derivedStateOf {
            val layoutInfo = minuteState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) initialMinute
            else {
                val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItems.minByOrNull { abs((it.offset + it.size / 2) - viewportCenter) }
                    ?.let { minutes[it.index] } ?: initialMinute
            }
        }
    }

    LaunchedEffect(currentHour, currentMinute) {
        onTimeChanged(currentHour, currentMinute)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        WheelColumn(
            items = hours,
            state = hourState,
            formatter = { it.toString().padStart(2, '0') },
            modifier = Modifier.weight(1f)
        )
        Text(":", fontSize = 20.sp, modifier = Modifier.padding(horizontal = 2.dp))
        WheelColumn(
            items = minutes,
            state = minuteState,
            formatter = { it.toString().padStart(2, '0') },
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> WheelColumn(
    items: List<T>,
    state: LazyListState,
    formatter: (T) -> String,
    modifier: Modifier = Modifier
) {
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = state)
    val density = LocalDensity.current
    val itemHeightPx = with(density) { 40.dp.toPx() }
    val viewportHeightPx = with(density) { 200.dp.toPx() }

    Box(modifier = modifier.height(200.dp), contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(40.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            shape = RoundedCornerShape(12.dp)
        ) {}

        LazyColumn(
            state = state,
            flingBehavior = snapFlingBehavior,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 80.dp)
        ) {
            items(items.size) { index ->
                val item = items[index]
                val isCenter = index == state.firstVisibleItemIndex
                val animatedColor by animateColorAsState(
                    targetValue = if (isCenter) MaterialTheme.colorScheme.primary else Color.Gray,
                    animationSpec = tween(150)
                )

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .graphicsLayer {
                            val firstIdx = state.firstVisibleItemIndex
                            val firstOffset = state.firstVisibleItemScrollOffset.toFloat()
                            val contentPaddingPx = 80.dp.toPx()
                            val itemTopInViewport = (index - firstIdx) * itemHeightPx - firstOffset + contentPaddingPx
                            val itemCenterInViewport = itemTopInViewport + (itemHeightPx / 2f)
                            val viewCenter = viewportHeightPx / 2f
                            val dist = abs(viewCenter - itemCenterInViewport)
                            val maxDist = itemHeightPx * 2.5f

                            if (dist < maxDist) {
                                val progress = (dist / maxDist).coerceIn(0f, 1f)
                                alpha = (1f - progress * 0.9f).coerceIn(0f, 1f)
                                val scale = (1.25f - progress * 0.55f).coerceIn(0.6f, 1.25f)
                                scaleX = scale
                                scaleY = scale
                            } else {
                                alpha = 0f
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formatter(item),
                        fontSize = if (isCenter) 20.sp else 16.sp,
                        fontWeight = if (isCenter) FontWeight.ExtraBold else FontWeight.Medium,
                        color = animatedColor
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// Компактный календарь (использует MonthPager, но без лишних отступов)
// ---------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CompactDatePicker(
    selectedDate: LocalDate,
    currentMonth: LocalDate,
    onMonthChanged: (LocalDate) -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    var isMonthYearPickerMode by remember { mutableStateOf(false) }
    val today = LocalDate.now()
    val interactionSource = remember { MutableInteractionSource() }

    Column {
        // Кнопка месяца/года с иконкой
        Button(
            onClick = { isMonthYearPickerMode = !isMonthYearPickerMode },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currentMonth.month.getDisplayName(DateTextStyle.FULL, Locale("ru"))
                        .replaceFirstChar { it.uppercase() } + " ${currentMonth.year}",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    if (isMonthYearPickerMode) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (isMonthYearPickerMode) {
            // Режим выбора месяца и года
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val months = remember {
                        (1..12).map { month ->
                            LocalDate.of(2000, month, 1).month.getDisplayName(DateTextStyle.FULL, Locale("ru"))
                                .replaceFirstChar { it.uppercase() }
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        WheelPicker(
                            items = months,
                            initialItem = months[currentMonth.monthValue - 1],
                            itemHeight = 36.dp,
                            visibleItems = 5,
                            centerFontSize = 16.sp,
                            secondaryFontSize = 12.sp,
                            onItemSelected = { monthName ->
                                val monthIndex = months.indexOf(monthName) + 1
                                onMonthChanged(currentMonth.withMonth(monthIndex))
                            }
                        )
                    }
                    val years = remember { (2000..2100).toList() }
                    Box(modifier = Modifier.weight(1f)) {
                        WheelPicker(
                            items = years,
                            initialItem = currentMonth.year,
                            itemHeight = 36.dp,
                            visibleItems = 5,
                            centerFontSize = 16.sp,
                            secondaryFontSize = 12.sp,
                            formatter = { it.toString() },
                            onItemSelected = { year ->
                                onMonthChanged(currentMonth.withYear(year))
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { isMonthYearPickerMode = false }) {
                        Text("Отмена", fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { isMonthYearPickerMode = false },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("OK", fontSize = 14.sp)
                    }
                }
            }
        } else {
            MonthPagerCompact(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                today = today,
                onMonthChanged = onMonthChanged,
                onDateClick = onDateSelected
            )
        }
    }
}

@Composable
fun MonthPagerCompact(
    currentMonth: LocalDate,
    selectedDate: LocalDate,
    today: LocalDate,
    onMonthChanged: (LocalDate) -> Unit,
    onDateClick: (LocalDate) -> Unit
) {
    val initialPage = 500
    val pagerState = rememberPagerState(initialPage = initialPage) { 1000 }

    LaunchedEffect(currentMonth.year, currentMonth.monthValue) {
        val monthDiff = ChronoUnit.MONTHS.between(
            today.withDayOfMonth(1),
            currentMonth.withDayOfMonth(1)
        ).toInt()
        val targetPage = initialPage + monthDiff
        if (pagerState.currentPage != targetPage) {
            pagerState.scrollToPage(targetPage)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        val offset = pagerState.currentPage - initialPage
        val newMonth = today.withDayOfMonth(1).plusMonths(offset.toLong())
        if (newMonth.monthValue != currentMonth.monthValue || newMonth.year != currentMonth.year) {
            onMonthChanged(newMonth)
        }
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(it, fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
        HorizontalPager(state = pagerState) { page ->
            val monthOffset = page - initialPage
            val monthDate = today.withDayOfMonth(1).plusMonths(monthOffset.toLong())
            CalendarGridCompact(monthDate, selectedDate, today, onDateClick)
        }
    }
}

@Composable
fun CalendarGridCompact(
    monthDate: LocalDate,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = monthDate.withDayOfMonth(1)
    val lastDayOfMonth = monthDate.plusMonths(1).minusDays(1)
    val daysInMonth = lastDayOfMonth.dayOfMonth
    val firstDayWeekValue = firstDayOfMonth.dayOfWeek.value // 1=Пн, 7=Вс

    Column(modifier = Modifier.fillMaxWidth()) {
        for (row in 0 until 6) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 1..7) {
                    val dayNumber = row * 7 + col - (firstDayWeekValue - 1)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayNumber in 1..daysInMonth) {
                            val date = monthDate.withDayOfMonth(dayNumber)
                            val isSelected = date == selectedDate
                            val isToday = date == today
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                            else -> Color.Transparent
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onDateClick(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 11.sp
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

// ---------------------------------------------------------------------
// Колесо единиц измерения (минуты, часы, дни, недели, месяцы)
// ---------------------------------------------------------------------
enum class ReminderUnit(val displayName: String) {
    MINUTES("минут"),
    HOURS("часов"),
    DAYS("дней"),
    WEEKS("недель"),
    MONTHS("месяцев")
}

// ---------------------------------------------------------------------
// Выбор дней недели (круглые кнопки)
// ---------------------------------------------------------------------
@Composable
fun WeekdaySelector(
    selectedDays: Set<DayOfWeek>,
    onSelectionChanged: (Set<DayOfWeek>) -> Unit
) {
    val days = listOf(
        DayOfWeek.MONDAY to "ПН",
        DayOfWeek.TUESDAY to "ВТ",
        DayOfWeek.WEDNESDAY to "СР",
        DayOfWeek.THURSDAY to "ЧТ",
        DayOfWeek.FRIDAY to "ПТ",
        DayOfWeek.SATURDAY to "СБ",
        DayOfWeek.SUNDAY to "ВС"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        days.forEach { (day, label) ->
            val isSelected = day in selectedDays
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null)
                    {
                        onSelectionChanged(
                            if (isSelected) selectedDays - day else selectedDays + day
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else Color.Gray
                )
            }
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
        modifier = Modifier.fillMaxWidth(),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> WheelPicker(
    items: List<T>,
    initialItem: T,
    itemHeight: Dp = 40.dp,
    visibleItems: Int = 5,
    formatter: (T) -> String = { it.toString() },
    centerFontSize: androidx.compose.ui.unit.TextUnit = 20.sp,    // новый параметр
    secondaryFontSize: androidx.compose.ui.unit.TextUnit = 16.sp, // новый параметр
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }
    val viewportHeight = itemHeight * visibleItems

    val initialIndex = items.indexOf(initialItem).coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(initialItem) {
        val index = items.indexOf(initialItem)
        if (index >= 0) {
            listState.animateScrollToItem(index)
        }
    }

    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) initialIndex
            else {
                val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItemsInfo.minByOrNull { info ->
                    abs((info.offset + info.size / 2) - viewportCenter)
                }?.index ?: initialIndex
            }
        }
    }

    LaunchedEffect(centerIndex) {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    val currentItem = items.getOrElse(centerIndex) { initialItem }
    LaunchedEffect(currentItem) {
        onItemSelected(currentItem)
    }

    Box(
        modifier = modifier.height(viewportHeight),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(itemHeight),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            shape = RoundedCornerShape(12.dp)
        ) {}

        LazyColumn(
            state = listState,
            flingBehavior = snapFlingBehavior,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = (viewportHeight - itemHeight) / 2)
        ) {
            items(items.size) { index ->
                val item = items[index]
                val isCenter = index == centerIndex
                val animatedColor by animateColorAsState(
                    targetValue = if (isCenter) MaterialTheme.colorScheme.primary else Color.Gray,
                    animationSpec = tween(150)
                )

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer {
                            val firstIdx = listState.firstVisibleItemIndex
                            val firstOffset = listState.firstVisibleItemScrollOffset.toFloat()
                            val contentPaddingPx = ((viewportHeight - itemHeight) / 2).toPx()
                            val itemTopInViewport = (index - firstIdx) * itemHeightPx - firstOffset + contentPaddingPx
                            val itemCenterInViewport = itemTopInViewport + (itemHeightPx / 2f)
                            val viewCenter = viewportHeight.toPx() / 2f
                            val dist = abs(viewCenter - itemCenterInViewport)
                            val maxDist = itemHeightPx * 2.5f

                            if (dist < maxDist) {
                                val progress = (dist / maxDist).coerceIn(0f, 1f)
                                alpha = (1f - progress * 0.9f).coerceIn(0f, 1f)
                                val scale = (1.25f - progress * 0.55f).coerceIn(0.6f, 1.25f)
                                scaleX = scale
                                scaleY = scale
                            } else {
                                alpha = 0f
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formatter(item),
                        fontSize = if (isCenter) centerFontSize else secondaryFontSize,
                        fontWeight = if (isCenter) FontWeight.ExtraBold else FontWeight.Medium,
                        color = animatedColor
                    )
                }
            }
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