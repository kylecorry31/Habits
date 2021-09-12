package com.kylecorry.habits.domain

import com.kylecorry.andromeda.core.specifications.Specification
import java.time.Duration
import java.time.LocalDate

class WeeklyHabitIsCompleteSpecification(private val today: LocalDate = LocalDate.now()) :
    Specification<Habit>() {
    override fun isSatisfiedBy(value: Habit): Boolean {
        return value.type == HabitType.Weekly && value.lastCompletedOn != null && inSameWeek(
            value.lastCompletedOn,
            today
        )
    }

    private fun inSameWeek(first: LocalDate, second: LocalDate): Boolean {
        val daysBetween = Duration.between(first.atStartOfDay(), second.atStartOfDay())
        if (daysBetween >= Duration.ofDays(7)) {
            return false
        }

        return first.dayOfWeek <= second.dayOfWeek
    }
}
