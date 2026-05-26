package com.example.plango

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun GoalsScreen() {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val goalDao = db.goalDao()
    val taskDao = db.taskDao()
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val goalsFlow = remember { goalDao.observeGoals() }
    val goals by goalsFlow.collectAsState(initial = emptyList())

    var orderedGoals by remember(goals) {
        mutableStateOf(goals.sortedBy { it.position })
    }

    var showAddGoalSheet by remember { mutableStateOf(false) }
    var editingGoal by remember { mutableStateOf<GoalEntity?>(null) }
    var deleteGoalCandidate by remember { mutableStateOf<GoalEntity?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val neworderedGoals = orderedGoals.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        orderedGoals = neworderedGoals
        haptic.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)

        coroutineScope.launch {
            goalDao.updateGoalPositionsByOrder(
                neworderedGoals.map { it.id }
            )
        }
    }

    // Сбрасываем позиции при изменении порядка
    LaunchedEffect(orderedGoals) {
        // Опционально: сохранять позиции в БД при каждом изменении
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Цели",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(18.dp))

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(orderedGoals, key = { it.id }) { goal ->
                    ReorderableItem(reorderableState, key = goal.id) { isDragging ->
                        GoalItemView(
                            goal = goal,
                            isDragging = isDragging,
                            reorderScope = this,
                            onToggleCompleted = { updatedGoal ->
                                scope.launch {
                                    goalDao.updateGoal(updatedGoal)
                                }
                            },
                            onEdit = { editingGoal = goal },
                            onDelete = { deleteGoalCandidate = goal },
                            onDragStopped = {}
                        )
                    }
                }
            }

            Surface(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    showAddGoalSheet = true
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Добавить цель",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // Добавление цели
    if (showAddGoalSheet) {
        GoalDialog(
            initialGoal = null,
            onDismiss = { showAddGoalSheet = false },
            onSave = { title, tasksToAchieve ->
                scope.launch {
                    val newGoal = GoalEntity(
                        title = title.trim(),
                        tasks_to_achieve = tasksToAchieve,
                        tasks_completed = 0,
                        is_completed = false,
                        position = goalDao.getGoalCount() + 1
                    )
                    goalDao.insertGoal(newGoal)
                    showAddGoalSheet = false
                }
            }
        )
    }

    // Редактирование цели
    if (editingGoal != null) {
        GoalDialog(
            initialGoal = editingGoal,
            onDismiss = { editingGoal = null },
            onSave = { title, tasksToAchieve ->
                val current = editingGoal ?: return@GoalDialog
                scope.launch {
                    goalDao.updateGoal(
                        current.copy(
                            title = title.trim(),
                            tasks_to_achieve = tasksToAchieve
                        )
                    )
                    editingGoal = null
                }
            }
        )
    }

    // Диалог удаления цели
    if (deleteGoalCandidate != null) {
        val goal = deleteGoalCandidate!!
        Dialog(
            onDismissRequest = { deleteGoalCandidate = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Удалить цель?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Surface(
                            onClick = { deleteGoalCandidate = null },
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)) {
                                Text("Отмена", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Surface(
                            onClick = {
                                scope.launch {
                                    taskDao.clearGoalFromTasks(goal.id)
                                    goalDao.deleteGoalSafely(goal)
                                    deleteGoalCandidate = null
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.error
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)) {
                                Text("ОК", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalItemView(
    goal: GoalEntity,
    isDragging: Boolean,
    reorderScope: ReorderableCollectionItemScope,
    onToggleCompleted: (GoalEntity) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDragStopped: () -> Unit
) {
    val activeGreen = Color(0xFF4CAF50)
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isDragging) 1.03f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(if (isDragging) 1f else 0f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                clip = false              // отключаем обрезку при перетаскивании
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onEdit() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            // Верхняя строка: чекбокс, название, удаление, перетаскивание
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (goal.is_completed) activeGreen.copy(alpha = 0.1f) else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            2.dp,
                            if (goal.is_completed) activeGreen else Color.LightGray,
                            CircleShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            onToggleCompleted(goal.copy(is_completed = !goal.is_completed))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (goal.is_completed) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = activeGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = goal.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    color = if (goal.is_completed) activeGreen else MaterialTheme.colorScheme.onBackground,
                    textDecoration = if (goal.is_completed) TextDecoration.LineThrough else null
                )

                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        onDelete()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить цель",
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

            if (goal.tasks_to_achieve != null && goal.tasks_to_achieve > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .background(
                                color = activeGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(3.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(
                                    (goal.tasks_completed.toFloat() / goal.tasks_to_achieve).coerceIn(0f, 1f)
                                )
                                .background(
                                    color = activeGreen,
                                    shape = RoundedCornerShape(3.dp)
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${goal.tasks_completed}/${goal.tasks_to_achieve}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun GoalDialog(
    initialGoal: GoalEntity?,
    onDismiss: () -> Unit,
    onSave: (title: String, tasksToAchieve: Int?) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            GoalSheetContent(
                initialGoal = initialGoal,
                onDismiss = onDismiss,
                onSave = onSave
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun GoalSheetContent(
    initialGoal: GoalEntity?,
    onDismiss: () -> Unit,
    onSave: (title: String, tasksToAchieve: Int?) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current

    val goal = initialGoal
    var title by remember(goal?.id) { mutableStateOf(goal?.title ?: "") }
    var tasksToAchieveText by remember(goal?.id) { mutableStateOf(goal?.tasks_to_achieve?.toString() ?: "") }
    val isEditMode = goal != null

    val imeBottom = WindowInsets.ime.getBottom(density)
    var hasKeyboardShown by remember { mutableStateOf(false) }
    if (imeBottom > 0) hasKeyboardShown = true

    var isDismissing by remember { mutableStateOf(false) }
    var AnimatabletranslationY = remember { Animatable(0f) }

    fun triggerDismiss(save: Boolean = false) {
        if (isDismissing) return
        isDismissing = true
        keyboardController?.hide()
        coroutineScope.launch {
            AnimatabletranslationY.animateTo(
                targetValue = 1200f,
                animationSpec = tween(durationMillis = 260, easing = FastOutLinearInEasing)
            )
            if (save && title.isNotBlank()) {
                val tasksToAchieve = tasksToAchieveText.trim().takeIf { it.isNotBlank() }?.toIntOrNull()
                onSave(title.trim(), tasksToAchieve)
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
        animationSpec = tween(220)
    )

    val sheetAlpha by animateFloatAsState(
        targetValue = if (hasKeyboardShown && !isDismissing) 1f else 0f,
        animationSpec = tween(180)
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
            ) { triggerDismiss(false) }
    )

    // Основное окно
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(sheetAlpha)
            .graphicsLayer { translationY = AnimatabletranslationY.value }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /* блокируем клик */ },
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
                                    AnimatabletranslationY.snapTo((AnimatabletranslationY.value + delta).coerceAtLeast(0f))
                                }
                            }
                        },
                        onDragStopped = { velocity ->
                            if (isDismissing) return@draggable
                            if (AnimatabletranslationY.value > 600f || velocity > 5000f) {
                                triggerDismiss(false)
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
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    placeholder = { Text("Название цели...", fontSize = 16.sp, color = Color.Gray) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Задач для достижения:",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    val lineColor = if (tasksToAchieveText.isNotEmpty())
                        MaterialTheme.colorScheme.primary
                    else
                        Color.LightGray.copy(alpha = 0.5f)

                    Spacer(modifier = Modifier.width(16.dp))

                    // Поле ввода количества с подчеркиванием
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(32.dp)
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = lineColor,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            },
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        BasicTextField(
                            value = tasksToAchieveText,
                            onValueChange = { new -> tasksToAchieveText = new.filter { it.isDigit() } },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            decorationBox = { innerTextField ->
                                Box(contentAlignment = Alignment.Center,modifier = Modifier.padding(bottom = 2.dp)) {
                                    if (tasksToAchieveText.isEmpty()) {
                                        Text(
                                            text = "количество",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    val canSave = title.isNotBlank()
                    Surface(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            if (canSave) triggerDismiss(save = true)
                        },
                        modifier = Modifier
                            .size(43.dp)
                            .semantics { testTag = "save_goal_button" },
                        shape = RoundedCornerShape(12.dp),
                        color = if (canSave) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        enabled = canSave
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isEditMode) Icons.Default.Check else Icons.Default.ArrowForward,
                                contentDescription = "Сохранить",
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