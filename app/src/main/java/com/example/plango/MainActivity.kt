package com.example.plango

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedItem by remember { mutableIntStateOf(1) }
    var showAddTaskSheet by remember { mutableStateOf(false) }

    val items = listOf("Цели", "Задачи", "Настройки")
    val icons = listOf(Icons.Filled.Star, Icons.Filled.List, Icons.Filled.Settings)

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedItem == 1) {
                FloatingActionButton(
                    onClick = { showAddTaskSheet = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить", tint = Color.White)
                }
            }
        }
    ) { innerPadding ->
        // Основной контент экранов
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedItem) {
                0 -> GoalsScreen()
                1 -> TasksScreen()
                2 -> SettingsScreen()
            }
        }
    }

    // ВЫНОСИМ ОКНО ЗА ПРЕДЕЛЫ SCAFFOLD (чтобы перекрыть NavigationBar и FAB)
    // Используем Box, который занимает ВЕСЬ экран поверх Scaffold
    if (showAddTaskSheet) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Затемнение фона
            var animateScrim by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { animateScrim = true }

            AnimatedVisibility(
                visible = animateScrim,
                enter = fadeIn(tween(500)),
                exit = fadeOut(tween(500))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            animateScrim = false
                            showAddTaskSheet = false
                        }
                )
            }

            // 2. Окно задачи
            AddTaskSheet(
                onDismiss = {
                    animateScrim = false
                    showAddTaskSheet = false
                },
                onTaskAdded = {
                    showAddTaskSheet = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    MaterialTheme {
        MainScreen()
    }
}