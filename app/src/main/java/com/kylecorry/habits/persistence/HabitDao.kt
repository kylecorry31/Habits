package com.kylecorry.habits.persistence

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits")
    fun getAll(): LiveData<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HabitEntity): Long

    @Update
    suspend fun update(item: HabitEntity)

    @Delete
    suspend fun delete(item: HabitEntity)
}