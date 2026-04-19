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
    val cover: Int? = null,
    val is_important: Boolean? = false,
    val is_urgency: Boolean? = false,
    val is_completed: Boolean? = false,
    val difficulty: Int, // 1..5
    val goal_id: Int = 0,
    val deadline_date: Long? = null,
    val remind_date: Long? = null,
    val repeat_mon: Boolean = false,
    val repeat_tue: Boolean = false,
    val repeat_wed: Boolean = false,
    val repeat_thu: Boolean = false,
    val repeat_fri: Boolean = false,
    val repeat_sat: Boolean = false,
    val repeat_sun: Boolean = false
)

@Entity(
    tableName = "subtasks",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE // Если удалим задачу, удалятся и подзадачи
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