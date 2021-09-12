package com.kylecorry.habits.persistence

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kylecorry.habits.domain.Reward

class RewardRepo(context: Context) {

    private val rewardDao = AppDatabase.getInstance(context).rewardDao()

    fun getRewards(): LiveData<List<Reward>> {
        return Transformations.map(rewardDao.getAll()) {
            it.map { h -> h.toReward() }
        }
    }

    suspend fun deleteReward(reward: Reward) {
        rewardDao.delete(RewardEntity.fromReward(reward))
    }

    suspend fun addReward(reward: Reward): Long {
        return if (reward.id == 0L) {
            rewardDao.insert(RewardEntity.fromReward(reward))
        } else {
            rewardDao.update(RewardEntity.fromReward(reward))
            reward.id
        }
    }

}