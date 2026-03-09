package com.example.plango

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    // Состояние для отслеживания выбранной вкладки
    var selectedItem by remember { mutableIntStateOf(1) }

    // Список вкладок (название, иконка)
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
        }
    ) { innerPadding ->
        // Контент экрана в зависимости от выбранной вкладки
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> GoalsScreen()
                1 -> TasksScreen()
                2 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun GoalsScreen() {
    Text(text = "Экран Целей (Чек-лист)")
}

@Composable
fun TasksScreen() {
    Text(text = "Главный экран Задач (Список на день)")
}

@Composable
fun SettingsScreen() {
    Text(text = "Настройки (Биоритмы)")
}