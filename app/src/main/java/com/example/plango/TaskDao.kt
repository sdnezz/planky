package com.example.plango

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE date_of_task >= :startOfDay AND date_of_task <= :endOfDay")
    fun getTasksForDay(startOfDay: Long, endOfDay: Long): Flow<List<TaskEntity>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE date_of_task >= :startOfDay AND date_of_task <= :endOfDay")
    fun getTasksWithSubtasksForDay(startOfDay: Long, endOfDay: Long): Flow<List<TaskWithSubtasks>>

    @Query("SELECT COUNT(*) FROM tasks WHERE date_of_task >= :startOfDay AND date_of_task <= :endOfDay")
    suspend fun getTaskCountForDay(startOfDay: Long, endOfDay: Long): Int

    @Query("SELECT * FROM tasks WHERE date_of_task >= :startOfDay AND date_of_task <= :endOfDay ORDER BY position ASC")
    suspend fun getTasksForDaySync(startOfDay: Long, endOfDay: Long): List<TaskEntity>

    @Transaction
    suspend fun updateTasksPositions(tasks: List<TaskEntity>) {
        tasks.forEach { updateTask(it) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)
    @Update
    suspend fun updateSubTask(subtask: SubTaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    // Подзадачи
    @Insert
    suspend fun insertSubTask(subTask: SubTaskEntity)

    @Query("SELECT * FROM subtasks WHERE task_id = :taskId")
    suspend fun getSubTasksForTask(taskId: Int): List<SubTaskEntity>
}

data class TaskWithSubtasks(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val subtasks: List<SubTaskEntity>
)