package com.kylecorry.habits.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kylecorry.habits.domain.Habit
import com.kylecorry.habits.domain.HabitType
import java.time.LocalDate

@Entity(tableName = "habits")
data class HabitEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "reward") val reward: Int,
    @ColumnInfo(name = "type", defaultValue = "1") val habitType: HabitType = HabitType.Daily,
    @ColumnInfo(name = "lastCompletedOn") val lastCompletedOn: LocalDate? = null,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    fun toHabit(): Habit {
        return Habit(id, name, reward, habitType, lastCompletedOn)
    }

    companion object {
        fun fromHabit(habit: Habit): HabitEntity {
            return HabitEntity(habit.name, habit.reward, habit.type, habit.lastCompletedOn).also {
                it.id = habit.id
            }
        }
    }

}