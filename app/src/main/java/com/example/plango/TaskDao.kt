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

    @Query("""
            UPDATE tasks 
            SET position = position - 1 
            WHERE date_of_task BETWEEN :start AND :end
            AND position > :fromPosition 
            AND position <= :toPosition
        """)
    suspend fun shiftUp(
        start: Long,
        end: Long,
        fromPosition: Int,
        toPosition: Int
    )

    @Query("""
        UPDATE tasks 
        SET position = position + 1 
        WHERE date_of_task BETWEEN :start AND :end
        AND position >= :toPosition 
        AND position < :fromPosition
    """)
    suspend fun shiftDown(
        start: Long,
        end: Long,
        fromPosition: Int,
        toPosition: Int
    )

    @Query("""
        UPDATE tasks 
        SET position = :newPosition 
        WHERE id = :taskId
    """)
    suspend fun updateTaskPosition(taskId: Int, newPosition: Int)

    @Transaction
    suspend fun updatePositionsOnReorder(
        oldItem: TaskEntity,
        newIndex: Int,
        tasks: List<TaskWithSubtasks>
    ) {
        val fromPosition = oldItem.position ?: return
        val toPosition = newIndex + 1

        if (fromPosition == toPosition) return

        val start = tasks.first().task.date_of_task
        val end = tasks.first().task.date_of_task

        if (fromPosition < toPosition) {
            // Перетаскивание вниз
            shiftUp(
                start = start,
                end = end,
                fromPosition = fromPosition,
                toPosition = toPosition
            )
        } else {
            // Перетаскивание вверх
            shiftDown(
                start = start,
                end = end,
                fromPosition = fromPosition,
                toPosition = toPosition
            )
        }

        updateTaskPosition(oldItem.id, toPosition)
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

    @Query("DELETE FROM subtasks WHERE task_id = :taskId")
    suspend fun deleteSubTasksForTask(taskId: Int)

    @Transaction
    suspend fun updateTaskWithSubtasks(
        task: TaskEntity,
        subtasks: List<SubTaskEntity>
    ) {
        updateTask(task)
        deleteSubTasksForTask(task.id)
        subtasks.forEach { insertSubTask(it) }
    }
}

data class TaskWithSubtasks(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val subtasks: List<SubTaskEntity>
)