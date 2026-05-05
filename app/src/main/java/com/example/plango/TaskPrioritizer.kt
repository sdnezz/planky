package com.example.plango

import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min

object TaskPrioritizer {

    private val CHRONOTYPE_SCHEDULES = mapOf(
        "morning" to mapOf(
            "peak" to (6..11),
            "normal" to (12..16),
            "low" to ((17..23) + (0..5))
        ),
        "evening" to mapOf(
            "peak" to (18..22),
            "normal" to (13..17),
            "low" to ((6..12) + (23..23) + (0..5))
        ),
        "intermediate" to mapOf(
            "peak" to (10..13),
            "normal" to ((8..9) + (14..17)),
            "low" to ((0..7) + (18..23))
        )
    )

    private fun getPeriod(chronotype: String, hour: Int): String {
        val schedule = CHRONOTYPE_SCHEDULES[chronotype]
            ?: CHRONOTYPE_SCHEDULES["intermediate"]!!

        return schedule.entries.firstOrNull { hour in it.value }?.key ?: "low"
    }

    private fun eisenhower(important: Boolean, urgent: Boolean): Double =
        when {
            important && urgent -> 1.0
            important -> 0.75
            urgent -> 0.5
            else -> 0.25
        }

    private fun deadlineScore(deadline: String?, now: LocalDateTime): Double {
        if (deadline == null) return 0.1

        val d = LocalDateTime.parse(deadline)
        val hours = java.time.Duration.between(now, d).toMinutes() / 60.0

        if (hours <= 0) return 1.0

        return 1.0 / (1.0 + ln(1 + hours / 24.0))
    }

    private fun chronoScore(complexity: Int, period: String): Double {
        val norm = (complexity / 5.0).coerceIn(0.0, 1.0)
        val w = when (period) {
            "peak" -> 1.0
            "normal" -> 0.5
            else -> 0.0
        }
        return w * norm + (1 - w) * (1 - norm)
    }

    private fun risk(complexity: Int, deadlineScore: Double): Double {
        val norm = (complexity / 5.0).coerceIn(0.0, 1.0)
        return norm * deadlineScore
    }

    fun prioritize(json: String): List<Int> {
        val input = JSONObject(json)

        val chronotype = input.optString("chronotype", "intermediate")
        val now = LocalDateTime.parse(
            input.optString(
                "current_time",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
        )

        val tasks = input.getJSONArray("tasks")
        val period = getPeriod(chronotype, now.hour)

        val scored = mutableListOf<Pair<Int, Double>>()

        for (i in 0 until tasks.length()) {
            val t = tasks.getJSONObject(i)

            val id = t.getInt("id")
            val important = t.optBoolean("important")
            val urgent = t.optBoolean("urgent")
            val complexity = t.optInt("complexity", 5)
            val deadline = t.optString("deadline", null)

            val e = eisenhower(important, urgent)
            val d = deadlineScore(deadline, now)
            val c = chronoScore(complexity, period)
            val r = risk(complexity, d)

            val priority = 0.30 * e + 0.40 * d + 0.15 * c + 0.15 * r

            scored.add(id to priority)
        }

        return scored
            .sortedByDescending { it.second }
            .map { it.first }
    }
}