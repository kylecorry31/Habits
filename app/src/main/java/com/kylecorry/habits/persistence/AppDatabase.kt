package com.kylecorry.habits.persistence

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * The Room database for this app
 */
@Database(
    entities = [HabitEntity::class, RewardEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun rewardDao(): RewardDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {

            val MIGRATION_1_2 = object: Migration(1, 2){
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.beginTransaction()
                    database.query("ALTER TABLE habits ADD COLUMN type INTEGER NOT NULL DEFAULT 1")
                    database.endTransaction()
                }

            }

            return Room.databaseBuilder(context, AppDatabase::class.java, "habits")
                .addMigrations(
                    MIGRATION_1_2
                )
                .build()
        }
    }
}