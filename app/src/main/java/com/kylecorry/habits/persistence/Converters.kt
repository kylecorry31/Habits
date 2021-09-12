package com.kylecorry.habits.persistence

import androidx.room.TypeConverter
import com.kylecorry.habits.domain.HabitType
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun toHabitType(value: Int): HabitType {
        return HabitType.values().firstOrNull { it.id == value } ?: HabitType.Daily
    }

    @TypeConverter
    fun fromHabitType(value: HabitType): Int {
        return value.id
    }

    @TypeConverter
    fun fromNullableLocalDate(value: LocalDate?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toNullableLocalDate(value: String?): LocalDate? {
        value ?: return null
        return LocalDate.parse(value)
    }
}