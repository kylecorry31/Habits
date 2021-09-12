package com.kylecorry.habits.ui

import android.content.Context
import com.kylecorry.habits.R
import com.kylecorry.habits.domain.HabitType

class HabitTypeMapper(private val context: Context) {
    
    fun map(type: HabitType): String {
        return when(type){
            HabitType.Daily -> context.getString(R.string.daily)
            HabitType.Weekly -> context.getString(R.string.weekly)
            HabitType.Monthly -> context.getString(R.string.monthly)
            HabitType.OneTime -> context.getString(R.string.one_time)
        }
    }
    
}