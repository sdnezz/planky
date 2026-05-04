package com.example.plango

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val chronotype: String? = null
)

@Dao
interface SettingsDao {
    @Query("SELECT chronotype FROM app_settings WHERE id = 1 LIMIT 1")
    fun observeChronotype(): Flow<String?>

    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): AppSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSettings(settings: AppSettingsEntity)

    @Query("UPDATE app_settings SET chronotype = :chronotype WHERE id = 1")
    suspend fun updateChronotype(chronotype: String)
}