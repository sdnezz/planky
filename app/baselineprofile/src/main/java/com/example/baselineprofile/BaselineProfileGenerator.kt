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

            // Ждём загрузки UI и нажимаем "Задачи"
            val tasksTab = device.wait(Until.findObject(By.text("Задачи")), 5000)
            tasksTab?.click()
            device.waitForIdle()

            // Основной цикл открытия окна добавления
            repeat(3) {
                // Ищем FAB по contentDescription
                val fab = device.wait(Until.findObject(By.desc("Добавить")), 3000)
                fab?.click()

                // Ждём появления текстового поля с placeholder "Новая задача..."
                val inputField = device.wait(
                    Until.findObject(By.text("Новая задача...")),
                    3000
                )
                // Вводим текст
                inputField?.click()
                inputField?.text = "Тест $it"
                device.waitForIdle()

                // Нажимаем кнопку "Запланировать" (ищем по тексту)
                val addButton = device.wait(
                    Until.findObject(By.text("Запланировать")),
                    2000
                )
                addButton?.click()
                device.waitForIdle()
            }

            // Дополнительно: проверяем закрытие свайпом
            val fab = device.wait(Until.findObject(By.desc("Добавить")), 3000)
            fab?.click()
            device.wait(Until.findObject(By.text("Новая задача...")), 3000)

            // Свайп вниз от середины экрана
            val displayHeight = device.displayHeight
            val displayWidth = device.displayWidth
            device.swipe(
                displayWidth / 2, displayHeight / 3,
                displayWidth / 2, displayHeight,
                20
            )
            device.waitForIdle()

            // Прокрутка календаря
            repeat(2) {
                device.swipe(
                    (displayWidth * 0.8).toInt(), (displayHeight * 0.3).toInt(),
                    (displayWidth * 0.2).toInt(), (displayHeight * 0.3).toInt(),
                    15
                )
                device.waitForIdle()
            }
        }
    }
}