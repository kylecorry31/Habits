package com.kylecorry.habits.domain

import com.kylecorry.andromeda.core.specifications.Specification
import java.time.LocalDate

class HabitIsCompleteSpecification(private val today: LocalDate = LocalDate.now()) :
    Specification<Habit>() {
    override fun isSatisfiedBy(value: Habit): Boolean {
        return DailyHabitIsCompleteSpecification(today)
            .or(WeeklyHabitIsCompleteSpecification(today))
            .or(MonthlyHabitIsCompleteSpecification(today))
            .or(OneTimeHabitIsCompleteSpecification())
            .isSatisfiedBy(value)
    }
}