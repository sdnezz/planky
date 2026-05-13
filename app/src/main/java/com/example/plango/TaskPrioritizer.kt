package com.example.plango

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.ln

data class PriorityTaskInput(
    val id: Int,
    val title: String,
    val important: Boolean,
    val urgent: Boolean,
    val complexity: Int,
    val deadline: LocalDateTime?
)

data class PriorityTaskResult(
    val id: Int,
    val title: String,
    val important: Boolean,
    val urgent: Boolean,
    val complexity: Int,
    val deadline: LocalDateTime?,
    val e: Double,
    val d: Double,
    val c: Double,
    val r: Double,
    val priority: Double,
    val chronotype: String,
    val currentTime: LocalDateTime,
    val productivityPeriod: String,
    val queuePosition: Int
)

data class PriorityRun(
    val chronotype: String,
    val currentTime: LocalDateTime,
    val productivityPeriod: String,
    val items: List<PriorityTaskResult>
) {
    val orderedIds: List<Int> get() = items.map { it.id }
}

object TaskPrioritizer {

    private fun productivityPeriod(chronotype: String, currentHour: Int): String {
        val c = chronotype.lowercase()
        return when (c) {
            "morning" -> when (currentHour) {
                in 6..11 -> "peak"
                in 12..16 -> "normal"
                else -> "low"
            }

            "evening" -> when (currentHour) {
                in 18..22 -> "peak"
                in 13..17 -> "normal"
                else -> "low"
            }

            else -> when (currentHour) {
                in 10..13 -> "peak"
                in 8..9, in 14..17 -> "normal"
                else -> "low"
            }
        }
    }

    private fun computeEisenhowerScore(important: Boolean, urgent: Boolean): Double {
        return when {
            important && urgent -> 1.00
            important && !urgent -> 0.75
            !important && urgent -> 0.50
            else -> 0.25
        }
    }

    private fun computeDeadlineScore(deadline: LocalDateTime?, currentTime: LocalDateTime): Double {
        if (deadline == null) return 0.10

        val hoursRemaining = Duration.between(currentTime, deadline).toMinutes() / 60.0
        if (hoursRemaining <= 0) return 1.0

        return 1.0 / (1.0 + ln(1.0 + hoursRemaining / 24.0))
    }

    private fun computeChronoComplexityScore(complexity: Int, period: String): Double {
        val complexityNorm = ((complexity - 1) / 4.0).coerceIn(0.1, 1.0)
        val peakWeight = when (period) {
            "peak" -> 1.0
            "normal" -> 0.5
            else -> 0.0
        }

        return peakWeight * complexityNorm + (1.0 - peakWeight) * (1.0 - complexityNorm)
    }

    private fun computeExecutionRisk(complexity: Int, deadlineScore: Double): Double {
        val complexityNorm = ((complexity - 1) / 4.0).coerceIn(0.1, 1.0)
        return complexityNorm * deadlineScore
    }

    fun prioritize(
        tasks: List<PriorityTaskInput>,
        chronotype: String,
        currentTime: LocalDateTime = LocalDateTime.now()
    ): PriorityRun {
        val productivityPeriod = productivityPeriod(chronotype, currentTime.hour)

        val scored = tasks.map { task ->
            val e = computeEisenhowerScore(task.important, task.urgent)
            val d = computeDeadlineScore(task.deadline, currentTime)
            val c = computeChronoComplexityScore(task.complexity, productivityPeriod)
            val r = computeExecutionRisk(task.complexity, d)

            val priority =
                0.30 * e +
                        0.40 * d +
                        0.15 * c +
                        0.15 * r

            task to PriorityTaskResult(
                id = task.id,
                title = task.title,
                important = task.important,
                urgent = task.urgent,
                complexity = task.complexity,
                deadline = task.deadline,
                e = e,
                d = d,
                c = c,
                r = r,
                priority = priority,
                chronotype = chronotype,
                currentTime = currentTime,
                productivityPeriod = productivityPeriod,
                queuePosition = 0
            )
        }

        val ordered = scored
            .sortedByDescending { it.second.priority }
            .mapIndexed { index, (_, item) ->
                item.copy(queuePosition = index + 1)
            }

        return PriorityRun(
            chronotype = chronotype,
            currentTime = currentTime,
            productivityPeriod = productivityPeriod,
            items = ordered
        )
    }

    fun TaskWithSubtasks.toPriorityInput(selectedDate: LocalDate): PriorityTaskInput {
        val displayTask = if (task.isRecurringSeries()) {
            task.toDisplayCopyForDate(selectedDate)
        } else {
            task
        }

        return PriorityTaskInput(
            id = displayTask.id,
            title = displayTask.title,
            important = displayTask.is_important == true,
            urgent = displayTask.is_urgency == true,
            complexity = displayTask.difficulty,
            deadline = displayTask.deadline_date?.let {
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
            }
        )
    }
}