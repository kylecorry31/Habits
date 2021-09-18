package com.kylecorry.habits.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kylecorry.andromeda.alerts.Alerts
import com.kylecorry.andromeda.alerts.toast
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.andromeda.list.ListView
import com.kylecorry.andromeda.pickers.Pickers
import com.kylecorry.andromeda.preferences.Preferences
import com.kylecorry.habits.R
import com.kylecorry.habits.databinding.FragmentRewardsBinding
import com.kylecorry.habits.databinding.ListItemPlainBinding
import com.kylecorry.habits.domain.Reward
import com.kylecorry.habits.persistence.RewardRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RewardsFragment : BoundFragment<FragmentRewardsBinding>() {

    private val rewardRepo by lazy { RewardRepo(requireContext()) }
    private lateinit var rewardList: ListView<Reward>
    private val preferences by lazy { Preferences(requireContext()) }

    private var rewards: List<Reward> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rewardList = ListView(binding.rewardList, R.layout.list_item_plain) { itemView, reward ->
            val itemBinding = ListItemPlainBinding.bind(itemView)
            itemBinding.title.text = reward.name
            itemBinding.description.text = reward.cost.toString()
            itemBinding.root.setOnLongClickListener {
                runInBackground {
                    withContext(Dispatchers.IO) {
                        rewardRepo.deleteReward(reward)
                    }
                }
                true
            }
            itemBinding.root.setOnClickListener {
                val points = getPoints()
                if (points >= reward.cost) {
                    Alerts.dialog(
                        requireContext(),
                        reward.name,
                        getString(R.string.use_points, reward.cost)
                    ){ cancelled ->
                        if (!cancelled){
                            setPoints(points - reward.cost)
                            rewardList.setData(rewards.toList())
                        }
                    }
                } else {
                    toast("Not enough points")
                }
            }
        }

        rewardList.addLineSeparator()

        rewardRepo.getRewards().observe(viewLifecycleOwner, {
            rewards = it
            rewardList.setData(it)
        })

        binding.createBtn.setOnClickListener {
            Pickers.text(requireContext(), getString(R.string.reward)) { name ->
                if (name != null) {
                    Pickers.number(
                        requireContext(),
                        getString(R.string.points),
                        allowNegative = false,
                        allowDecimals = false
                    ) { points ->
                        if (points != null) {
                            runInBackground {
                                withContext(Dispatchers.IO) {
                                    rewardRepo.addReward(Reward(0, name, points.toInt()))
                                }
                            }
                        }
                    }
                }
            }
        }

        val points = getPoints()
        binding.points.text = points.toString()
    }

    private fun setPoints(points: Long) {
        preferences.putLong(getString(R.string.pref_rewards), points)
        binding.points.text = points.toString()
    }

    private fun getPoints(): Long {
        return preferences.getLong(getString(R.string.pref_rewards)) ?: 0L
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRewardsBinding {
        return FragmentRewardsBinding.inflate(layoutInflater, container, false)
    }
}