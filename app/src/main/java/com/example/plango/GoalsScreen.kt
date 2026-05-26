package com.example.plango

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.graphics.graphicsLayer
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

    val lazyListState = rememberLazyListState()
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        orderedGoals = orderedGoals.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        haptic.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
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
                        val elevation by animateDpAsState(if (isDragging) 6.dp else 0.dp)

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
                            tonalElevation = 2.dp,
                            shadowElevation = elevation,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { editingGoal = goal }
                        ) {
                            GoalItemView(
                                goal = goal,
                                reorderScope = this,
                                onEdit = { editingGoal = goal },
                                onDelete = { deleteGoalCandidate = goal },
                                onDragStopped = {
                                    scope.launch {
                                        goalDao.updateGoalPositionsByOrder(
                                            orderedGoals.map { it.id }
                                        )
                                    }
                                }
                            )
                        }
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

    if (showAddGoalSheet) {
        GoalDialog(
            initialGoal = null,
            onDismiss = { showAddGoalSheet = false },
            onSave = { title, tasksToAchieve, isCompleted ->
                scope.launch {
                    val newGoal = GoalEntity(
                        title = title.trim(),
                        tasks_to_achieve = tasksToAchieve,
                        is_completed = isCompleted,
                        position = goalDao.getGoalCount() + 1
                    )
                    goalDao.insertGoal(newGoal)
                    showAddGoalSheet = false
                }
            }
        )
    }

    if (editingGoal != null) {
        GoalDialog(
            initialGoal = editingGoal,
            onDismiss = { editingGoal = null },
            onSave = { title, tasksToAchieve, isCompleted ->
                val current = editingGoal ?: return@GoalDialog
                scope.launch {
                    goalDao.updateGoal(
                        current.copy(
                            title = title.trim(),
                            tasks_to_achieve = tasksToAchieve,
                            is_completed = isCompleted
                        )
                    )
                    editingGoal = null
                }
            }
        )
    }

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
    reorderScope: ReorderableCollectionItemScope,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDragStopped: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = if (goal.is_completed) Color(0xFF4CAF50).copy(alpha = 0.1f) else Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    2.dp,
                    if (goal.is_completed) Color(0xFF4CAF50) else Color.LightGray,
                    CircleShape
                )
                .clickable {
                    onEdit()
                },
            contentAlignment = Alignment.Center
        ) {
            if (goal.is_completed) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF4CAF50)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = goal.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = if (goal.is_completed) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onBackground
            )
            if (goal.tasks_to_achieve != null) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Нужно задач: ${goal.tasks_to_achieve}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        IconButton(
            onClick = onDelete,
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
                Modifier.draggableHandle(onDragStopped = { onDragStopped() })
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun GoalDialog(
    initialGoal: GoalEntity?,
    onDismiss: () -> Unit,
    onSave: (title: String, tasksToAchieve: Int?, isCompleted: Boolean) -> Unit
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
    onSave: (title: String, tasksToAchieve: Int?, isCompleted: Boolean) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current

    val goal = initialGoal

    var title by remember(goal?.id) { mutableStateOf(goal?.title ?: "") }
    var tasksToAchieveText by remember(goal?.id) { mutableStateOf(goal?.tasks_to_achieve?.toString() ?: "") }
    var isCompleted by remember(goal?.id) { mutableStateOf(goal?.is_completed ?: false) }

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
                onSave(
                    title.trim(),
                    tasksToAchieveText.trim().takeIf { it.isNotBlank() }?.toIntOrNull(),
                    isCompleted
                )
            }
            onDismiss()
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { translationY = AnimatabletranslationY.value },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .draggable(
                        orientation = androidx.compose.foundation.gestures.Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            if (!isDismissing) {
                                coroutineScope.launch {
                                    AnimatabletranslationY.snapTo((AnimatabletranslationY.value + delta).coerceAtLeast(0f))
                                }
                            }
                        },
                        onDragStopped = { velocity ->
                            if (AnimatabletranslationY.value > 600f || velocity > 5000f) {
                                triggerDismiss(save = false)
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

                OutlinedTextField(
                    value = tasksToAchieveText,
                    onValueChange = { new ->
                        tasksToAchieveText = new.filter { it.isDigit() }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("Сколько задач нужно выполнить (необязательно)", fontSize = 14.sp, color = Color.Gray) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { isCompleted = it }
                    )
                    Text("Цель выполнена")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { triggerDismiss(save = false) }) {
                        Text("Отмена")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { triggerDismiss(save = title.isNotBlank()) },
                        enabled = title.isNotBlank(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}