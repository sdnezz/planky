package com.example.plango

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE date_of_task >= :startOfDay AND date_of_task <= :endOfDay")
    fun getTasksForDay(startOfDay: Long, endOfDay: Long): Flow<List<TaskEntity>>

    @Transaction
    @Query("""
        SELECT * FROM tasks t
        WHERE
            (
                t.repeat_mon = 0 AND t.repeat_tue = 0 AND t.repeat_wed = 0
                AND t.repeat_thu = 0 AND t.repeat_fri = 0
                AND t.repeat_sat = 0 AND t.repeat_sun = 0
                AND t.date_of_task BETWEEN :startOfDay AND :endOfDay
            )
            OR
            (
                :isMon = 1 AND t.repeat_mon = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isTue = 1 AND t.repeat_tue = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isWed = 1 AND t.repeat_wed = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isThu = 1 AND t.repeat_thu = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isFri = 1 AND t.repeat_fri = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isSat = 1 AND t.repeat_sat = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isSun = 1 AND t.repeat_sun = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
    """)
    fun getTasksWithSubtasksForDay(
        startOfDay: Long,
        endOfDay: Long,
        isMon: Int,
        isTue: Int,
        isWed: Int,
        isThu: Int,
        isFri: Int,
        isSat: Int,
        isSun: Int
    ): Flow<List<TaskWithSubtasks>>

    @Query("""
        SELECT COUNT(*) FROM tasks t
        WHERE
            (
                t.repeat_mon = 0 AND t.repeat_tue = 0 AND t.repeat_wed = 0
                AND t.repeat_thu = 0 AND t.repeat_fri = 0
                AND t.repeat_sat = 0 AND t.repeat_sun = 0
                AND t.date_of_task BETWEEN :startOfDay AND :endOfDay
            )
            OR
            (
                :isMon = 1 AND t.repeat_mon = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isTue = 1 AND t.repeat_tue = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isWed = 1 AND t.repeat_wed = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isThu = 1 AND t.repeat_thu = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isFri = 1 AND t.repeat_fri = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isSat = 1 AND t.repeat_sat = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
            OR
            (
                :isSun = 1 AND t.repeat_sun = 1
                AND t.date_of_task <= :endOfDay
                AND (t.repeat_until_date IS NULL OR t.repeat_until_date >= :startOfDay)
                AND NOT EXISTS (
                    SELECT 1 FROM task_occurrence_exceptions e
                    WHERE e.base_task_id = t.id
                      AND e.occurrence_date = :startOfDay
                      AND e.mode IN ('ONLY_THIS_DAY', 'DELETED_ONLY_THIS_DAY')
                )
            )
    """)
    suspend fun getVisibleTaskCountForDay(
        startOfDay: Long,
        endOfDay: Long,
        isMon: Int,
        isTue: Int,
        isWed: Int,
        isThu: Int,
        isFri: Int,
        isSat: Int,
        isSun: Int
    ): Int

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
            shiftUp(start = start, end = end, fromPosition = fromPosition, toPosition = toPosition)
        } else {
            shiftDown(start = start, end = end, fromPosition = fromPosition, toPosition = toPosition)
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

    @Insert
    suspend fun insertSubTask(subTask: SubTaskEntity)

    @Query("SELECT * FROM subtasks WHERE task_id = :taskId")
    suspend fun getSubTasksForTask(taskId: Int): List<SubTaskEntity>

    @Query("DELETE FROM subtasks WHERE task_id = :taskId")
    suspend fun deleteSubTasksForTask(taskId: Int)

    @Query("DELETE FROM subtasks WHERE task_id = :taskId")
    suspend fun deleteSubTasksForTaskOnly(taskId: Int)

    @Query("SELECT * FROM tasks WHERE source_task_id = :baseTaskId AND date_of_task = :occurrenceDate LIMIT 1")
    suspend fun getOccurrenceCopy(baseTaskId: Int, occurrenceDate: Long): TaskEntity?

    @Query("DELETE FROM tasks WHERE source_task_id = :baseTaskId AND date_of_task = :occurrenceDate")
    suspend fun deleteOccurrenceCopy(baseTaskId: Int, occurrenceDate: Long)

    @Query("DELETE FROM task_occurrence_exceptions WHERE base_task_id = :baseTaskId AND occurrence_date = :occurrenceDate")
    suspend fun deleteOccurrenceExceptionForDay(baseTaskId: Int, occurrenceDate: Long)

    @Insert
    suspend fun insertOccurrenceException(exception: TaskOccurrenceExceptionEntity): Long

    @Query("UPDATE tasks SET repeat_until_date = :untilDate WHERE id = :taskId")
    suspend fun endRecurringSeries(taskId: Int, untilDate: Long)

    @Transaction
    suspend fun createOneDayCopy(
        baseTask: TaskEntity,
        occurrenceDate: Long,
        updatedTask: TaskEntity,
        subtasks: List<SubTaskEntity>
    ): Long {
        deleteOccurrenceCopy(baseTask.id, occurrenceDate)
        deleteOccurrenceExceptionForDay(baseTask.id, occurrenceDate)

        val newTaskId = insertTask(
            updatedTask.copy(
                id = 0,
                date_of_task = occurrenceDate,
                source_task_id = baseTask.id,
                repeat_mon = false,
                repeat_tue = false,
                repeat_wed = false,
                repeat_thu = false,
                repeat_fri = false,
                repeat_sat = false,
                repeat_sun = false,
                repeat_until_date = null
            )
        ).toInt()

        subtasks.forEach { st ->
            insertSubTask(
                st.copy(
                    id = 0,
                    task_id = newTaskId
                )
            )
        }

        insertOccurrenceException(
            TaskOccurrenceExceptionEntity(
                base_task_id = baseTask.id,
                occurrence_date = occurrenceDate,
                mode = RecurrenceExceptionMode.ONLY_THIS_DAY
            )
        )

        return newTaskId.toLong()
    }

    @Transaction
    suspend fun saveRecurringOccurrenceForDate(
        baseTask: TaskEntity,
        occurrenceDate: Long,
        updatedTask: TaskEntity,
        subtasks: List<SubTaskEntity>
    ) {
        val existingCopy = getOccurrenceCopy(baseTask.id, occurrenceDate)
        if (existingCopy == null) {
            createOneDayCopy(baseTask, occurrenceDate, updatedTask, subtasks)
        } else {
            updateTaskWithSubtasks(
                updatedTask.copy(
                    id = existingCopy.id,
                    date_of_task = occurrenceDate,
                    source_task_id = baseTask.id,
                    repeat_mon = false,
                    repeat_tue = false,
                    repeat_wed = false,
                    repeat_thu = false,
                    repeat_fri = false,
                    repeat_sat = false,
                    repeat_sun = false,
                    repeat_until_date = null
                ),
                subtasks.map { it.copy(id = 0, task_id = existingCopy.id) }
            )
        }
    }

    @Transaction
    suspend fun deleteOccurrenceForDate(baseTask: TaskEntity, occurrenceDate: Long) {
        val existingCopy = getOccurrenceCopy(baseTask.id, occurrenceDate)
        if (existingCopy != null) {
            deleteTask(existingCopy)
        }
        insertOccurrenceException(
            TaskOccurrenceExceptionEntity(
                base_task_id = baseTask.id,
                occurrence_date = occurrenceDate,
                mode = RecurrenceExceptionMode.DELETED_ONLY_THIS_DAY
            )
        )
    }

    @Transaction
    suspend fun splitRecurringSeries(
        originalTask: TaskEntity,
        newTask: TaskEntity,
        subtasks: List<SubTaskEntity>,
        effectiveDate: Long
    ): Long {
        endRecurringSeries(originalTask.id, effectiveDate - 86_400_000L)

        val newTaskId = insertTask(
            newTask.copy(
                id = 0,
                date_of_task = effectiveDate,
                source_task_id = null,
                repeat_until_date = null
            )
        ).toInt()

        subtasks.forEach { st ->
            insertSubTask(
                st.copy(
                    id = 0,
                    task_id = newTaskId
                )
            )
        }

        return newTaskId.toLong()
    }

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