package com.example.plango

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [TaskEntity::class, SubTaskEntity::class, TaskOccurrenceExceptionEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "plango_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE tasks ADD COLUMN position INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE tasks ADD COLUMN repeat_until_date INTEGER")
                db.execSQL("ALTER TABLE tasks ADD COLUMN source_task_id INTEGER")
                db.execSQL("ALTER TABLE tasks ADD COLUMN deadline_time_minutes INTEGER")
                db.execSQL("ALTER TABLE tasks ADD COLUMN remind_before_minutes INTEGER")

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS task_occurrence_exceptions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        base_task_id INTEGER NOT NULL,
                        occurrence_date INTEGER NOT NULL,
                        mode TEXT NOT NULL,
                        FOREIGN KEY(base_task_id) REFERENCES tasks(id) ON DELETE CASCADE
                    )
                """.trimIndent())

                db.execSQL("CREATE INDEX IF NOT EXISTS index_task_occurrence_exceptions_base_task_id ON task_occurrence_exceptions(base_task_id)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_task_occurrence_exceptions_occurrence_date ON task_occurrence_exceptions(occurrence_date)")
            }
        }
    }
}