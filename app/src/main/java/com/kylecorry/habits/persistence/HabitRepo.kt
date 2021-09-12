package com.kylecorry.habits.persistence

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kylecorry.habits.domain.Habit

class HabitRepo(context: Context) {

    private val habitDao = AppDatabase.getInstance(context).habitDao()

    fun getHabits(): LiveData<List<Habit>> {
        return Transformations.map(habitDao.getAll()) {
            it.map { h -> h.toHabit() }
        }
    }

    suspend fun deleteHabit(habit: Habit){
        habitDao.delete(HabitEntity.fromHabit(habit))
    }

    suspend fun addHabit(habit: Habit): Long {
        return if (habit.id == 0L) {
            habitDao.insert(HabitEntity.fromHabit(habit))
        } else {
            habitDao.update(HabitEntity.fromHabit(habit))
            habit.id
        }
    }

}