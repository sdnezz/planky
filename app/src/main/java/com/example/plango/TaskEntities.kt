package com.example.plango

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val date_of_task: Long,
    val position: Int = 0,
    val cover: Int? = null,
    val is_important: Boolean? = false,
    val is_urgency: Boolean? = false,
    val is_completed: Boolean? = false,
    val difficulty: Int,
    val goal_id: Int = 0,

    // обычные (одноразовые) даты
    val deadline_date: Long? = null,
    val remind_date: Long? = null,

    // повторение
    val repeat_mon: Boolean = false,
    val repeat_tue: Boolean = false,
    val repeat_wed: Boolean = false,
    val repeat_thu: Boolean = false,
    val repeat_fri: Boolean = false,
    val repeat_sat: Boolean = false,
    val repeat_sun: Boolean = false,

    // серия
    val repeat_until_date: Long? = null,
    val source_task_id: Int? = null,

    // для повторяющихся задач: время суток, а не абсолютный timestamp
    val deadline_time_minutes: Int? = null,
    val remind_before_minutes: Int? = null,
)

@Entity(
    tableName = "subtasks",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("task_id")]
)
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val task_id: Int,
    val subtask_title: String?,
    val is_completed: Boolean = false
)

object RecurrenceExceptionMode {
    const val ONLY_THIS_DAY = "ONLY_THIS_DAY"
    const val DELETED_ONLY_THIS_DAY = "DELETED_ONLY_THIS_DAY"
}

@Entity(
    tableName = "task_occurrence_exceptions",
    indices = [Index("base_task_id"), Index("occurrence_date")],
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["base_task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskOccurrenceExceptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val base_task_id: Int,
    val occurrence_date: Long,
    val mode: String
)