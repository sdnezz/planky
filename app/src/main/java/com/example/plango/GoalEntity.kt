package com.example.plango

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val tasks_to_achieve: Int? = null,
    val tasks_completed: Int = 0,
    val is_completed: Boolean = false,
    val position: Int = 0
)

data class GoalWithTasks(
    @Embedded val goal: GoalEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "goal_id"
    )
    val tasks: List<TaskEntity>
)