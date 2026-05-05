package com.example.plango

import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun buildPrioritizationJson(
    tasks: List<TaskWithSubtasks>,
    chronotype: String?,
): String {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val now = LocalDateTime.now()

    val tasksArray = JSONArray()

    tasks.forEach { item ->
        val task = item.task
        val deadlineIso = task.deadline_date?.let {
            LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(it),
                ZoneId.systemDefault()
            ).format(formatter)
        }

        val obj = JSONObject()
            .put("id", task.id)
            .put("name", task.title)
            .put("deadline", deadlineIso)
            .put("important", task.is_important == true)
            .put("urgent", task.is_urgency == true)
            .put("complexity", task.difficulty)

        tasksArray.put(obj)
    }

    return JSONObject()
        .put("chronotype", chronotype ?: "intermediate")
        .put("current_time", now.format(formatter))
        .put("tasks", tasksArray)
        .toString()
}