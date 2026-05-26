package com.example.plango

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals ORDER BY position ASC")
    fun observeGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE id = :goalId LIMIT 1")
    suspend fun getGoalById(goalId: Int): GoalEntity?

    @Query("SELECT COUNT(*) FROM goals")
    suspend fun getGoalCount(): Int

    @Query("SELECT position FROM goals WHERE id = :goalId LIMIT 1")
    suspend fun getGoalPosition(goalId: Int): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity): Long

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("UPDATE goals SET position = :newPosition WHERE id = :goalId")
    suspend fun updateGoalPosition(goalId: Int, newPosition: Int)

    @Transaction
    suspend fun updateGoalPositionsByOrder(orderedIds: List<Int>) {
        if (orderedIds.isEmpty()) return
        orderedIds.forEachIndexed { index, id ->
            updateGoalPosition(id, index + 1)
        }
    }

    @Query("UPDATE tasks SET goal_id = NULL WHERE goal_id = :goalId")
    suspend fun clearGoalFromTasks(goalId: Int)

    @Transaction
    suspend fun deleteGoalSafely(goal: GoalEntity) {
        clearGoalFromTasks(goal.id)
        deleteGoal(goal)
    }

    @Query("""
        UPDATE goals SET 
            tasks_completed = (SELECT COUNT(*) FROM tasks WHERE goal_id = :goalId AND is_completed = 1),
            is_completed = CASE 
                WHEN tasks_to_achieve IS NOT NULL AND 
                     (SELECT COUNT(*) FROM tasks WHERE goal_id = :goalId AND is_completed = 1) >= tasks_to_achieve 
                THEN 1 
                ELSE is_completed 
            END
        WHERE id = :goalId
    """)
    suspend fun updateCompletedCountForGoal(goalId: Int?)
}