package com.kylecorry.habits.persistence

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards")
    fun getAll(): LiveData<List<RewardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RewardEntity): Long

    @Update
    suspend fun update(item: RewardEntity)

    @Delete
    suspend fun delete(item: RewardEntity)
}