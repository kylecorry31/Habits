package com.kylecorry.habits.domain

import java.time.LocalDate

data class Habit(
    val id: Long,
    val name: String,
    val reward: Int,
    val type: HabitType = HabitType.Daily,
    val lastCompletedOn: LocalDate? = null
)
