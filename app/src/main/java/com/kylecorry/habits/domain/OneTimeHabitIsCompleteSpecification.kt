package com.kylecorry.habits.domain

import com.kylecorry.andromeda.core.specifications.Specification
import java.time.LocalDate

class OneTimeHabitIsCompleteSpecification : Specification<Habit>() {
    override fun isSatisfiedBy(value: Habit): Boolean {
        return value.type == HabitType.OneTime && value.lastCompletedOn != null
    }
}