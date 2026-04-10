package com.example.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() {
        System.setProperty("androidx.benchmark.suppressDumpProfilesCheck", "true")

        baselineProfileRule.collect(
            packageName = "com.example.plango",
            includeInStartupProfile = true
        ) {
            pressHome()
            startActivityAndWait()

            // Ждём появления вкладки "Задачи" и кликаем
            val tasksTab = device.wait(Until.findObject(By.text("Задачи")), 5000)
            tasksTab?.click()
            device.waitForIdle()

            // Повторяем сценарий 3 раза для лучшего покрытия
            repeat(3) {
                // Открываем FAB
                val fab = device.wait(Until.findObject(By.desc("Добавить")), 3000)
                fab?.click()

                // Ждём появления окна (по placeholder поля ввода)
                val inputField = device.wait(
                    Until.findObject(By.text("Новая задача...")),
                    3000
                )
                inputField?.click()
                device.waitForIdle()  // даём клавиатуре открыться

                // Вводим текст
                inputField?.text = "Тест"
                device.waitForIdle()

                // Ждём, пока кнопка "Запланировать" станет доступной
                val addButton = device.wait(
                    Until.findObject(By.text("Запланировать").enabled(true)),
                    3000
                )
                addButton?.click()
                device.waitForIdle()
            }

            // Дополнительно: свайп для закрытия
            val fab = device.wait(Until.findObject(By.desc("Добавить")), 3000)
            fab?.click()
            device.wait(Until.findObject(By.text("Новая задача...")), 3000)

            val displayWidth = device.displayWidth
            val displayHeight = device.displayHeight
            device.swipe(
                displayWidth / 2, displayHeight / 3,
                displayWidth / 2, displayHeight,
                20
            )
            device.waitForIdle()
        }
    }
}