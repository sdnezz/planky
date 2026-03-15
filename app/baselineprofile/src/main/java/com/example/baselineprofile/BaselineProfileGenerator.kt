package com.example.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        packageName = "com.example.plango",
        includeInStartupProfile = true
    ) {
        pressHome()
        startActivityAndWait()

        // Переход на вкладку "Задачи"
        device.findObject(By.text("Задачи"))?.click()
        device.waitForIdle()

        // Несколько итераций открытия окна, ввода текста, закрытия
        repeat(5) {
            // Нажимаем FAB (ищем по contentDescription)
            val fab = device.findObject(By.desc("Добавить"))
            fab?.click()

            // Ждём появления окна – используем testTag (добавим в коде)
            device.wait(Until.hasObject(By.res("add_task_sheet")), 2000)

            // Фокусируемся на поле ввода (имитация клика)
            val inputField = device.findObject(By.text("Новая задача..."))
            inputField?.click()

            // Вводим текст – это вызывает работу IME и анимации
            inputField?.text = "Тестовая задача $it"

            // Закрываем окно (можно через кнопку "Назад")
            device.pressBack()
            device.waitForIdle()
        }

        // Прокрутка календаря
        val displayWidth = device.displayWidth
        val displayHeight = device.displayHeight

        // Свайпаем неделю туда-сюда для оптимизации Pager
        repeat(2) {
            device.swipe(
                (displayWidth * 0.8).toInt(), (displayHeight * 0.3).toInt(),
                (displayWidth * 0.2).toInt(), (displayHeight * 0.3).toInt(),
                15
            )
            device.waitForIdle()
        }


        device.waitForIdle()
    }
}