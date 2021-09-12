package com.kylecorry.habits.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kylecorry.andromeda.alerts.Alerts
import com.kylecorry.andromeda.alerts.toast
import com.kylecorry.andromeda.core.system.Resources
import com.kylecorry.andromeda.forms.Forms
import com.kylecorry.andromeda.forms.Forms.add
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.andromeda.list.ListView
import com.kylecorry.andromeda.pickers.Pickers
import com.kylecorry.andromeda.preferences.Preferences
import com.kylecorry.habits.R
import com.kylecorry.habits.databinding.FragmentHabitsBinding
import com.kylecorry.habits.databinding.ListItemHabitBinding
import com.kylecorry.habits.domain.DailyHabitIsCompleteSpecification
import com.kylecorry.habits.domain.Habit
import com.kylecorry.habits.domain.HabitIsCompleteSpecification
import com.kylecorry.habits.domain.HabitType
import com.kylecorry.habits.persistence.HabitRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HabitsFragment : BoundFragment<FragmentHabitsBinding>() {

    private val habitRepo by lazy { HabitRepo(requireContext()) }
    private lateinit var habitList: ListView<Habit>
    private val preferences by lazy { Preferences(requireContext()) }
    private val habitTypeMapper by lazy { HabitTypeMapper(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        habitList = ListView(binding.habitList, R.layout.list_item_habit) { itemView, habit ->
            val itemBinding = ListItemHabitBinding.bind(itemView)
            itemBinding.name.text = habit.name
            itemBinding.reward.text = "x${habit.reward}"
            itemBinding.type.text = habitTypeMapper.map(habit.type)

            val isComplete = HabitIsCompleteSpecification()

            itemBinding.root.setBackgroundColor(
                if (isComplete.isSatisfiedBy(habit)) Resources.color(
                    requireContext(),
                    R.color.colorPrimary
                ) else Resources.androidBackgroundColorPrimary(requireContext())
            )


            itemBinding.delete.setOnClickListener {
                Alerts.dialog(
                    requireContext(),
                    getString(R.string.delete_habit),
                    habit.name
                ) { cancelled ->
                    if (!cancelled) {
                        runInBackground {
                            withContext(Dispatchers.IO) {
                                habitRepo.deleteHabit(habit)
                            }
                        }
                    }
                }
            }

            itemBinding.root.setOnClickListener {
                Alerts.dialog(
                    requireContext(),
                    getString(R.string.complete_habit),
                    habit.name
                ) { cancelled ->
                    if (!cancelled) {
                        runInBackground {
                            withContext(Dispatchers.IO) {
                                if (!isComplete.isSatisfiedBy(habit)) {
                                    val lastReward =
                                        preferences.getLong(getString(R.string.pref_rewards)) ?: 0L
                                    preferences.putLong(
                                        getString(R.string.pref_rewards),
                                        lastReward + habit.reward
                                    )
                                    withContext(Dispatchers.Main) {
                                        toast(getString(R.string.points_earned, habit.reward))
                                    }
                                }
                                habitRepo.addHabit(habit.copy(lastCompletedOn = LocalDate.now()))
                            }
                        }
                    }
                }
            }
        }

        habitList.addLineSeparator()

        habitRepo.getHabits().observe(viewLifecycleOwner, {
            habitList.setData(it.filter { it.lastCompletedOn == null || it.lastCompletedOn == LocalDate.now() }
                .sortedBy { it.type })
        })

        binding.createBtn.setOnClickListener {

            var name: String? = null
            var points: Int? = null
            var type = HabitType.Daily

            val view = FrameLayout(requireContext())
            val form = Forms.Section(requireContext()) {
                text("name", label = getString(R.string.name)) { _, value ->
                    name = value
                }
                number(
                    "points",
                    label = getString(R.string.points),
                    allowNegative = false,
                    allowDecimals = false
                ) { _, value ->
                    points = value?.toInt()
                }
                spinner(
                    "type",
                    HabitType.values().map { habitTypeMapper.map(it) },
                    defaultIndex = 0,
                    useDialog = false,
                    label = getString(
                        R.string.type
                    )
                ) { _, value ->
                    type = HabitType.values()[(value ?: 0)]
                }
            }

            view.add(form)

            Alerts.dialog(
                requireContext(),
                getString(R.string.habit),
                contentView = view
            ) { cancelled ->
                if (!cancelled && name != null && points != null) {
                    runInBackground {
                        withContext(Dispatchers.IO) {
                            habitRepo.addHabit(Habit(0, name!!, points!!, type))
                        }
                    }
                }

            }
        }
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHabitsBinding {
        return FragmentHabitsBinding.inflate(layoutInflater, container, false)
    }
}