package com.kylecorry.habits.domain

import com.kylecorry.andromeda.core.specifications.Specification
import java.time.LocalDate

class DailyHabitIsCompleteSpecification(private val today: LocalDate = LocalDate.now()) :
    Specification<Habit>() {
    override fun isSatisfiedBy(value: Habit): Boolean {
        return value.type == HabitType.Daily && value.lastCompletedOn == today
    }
}