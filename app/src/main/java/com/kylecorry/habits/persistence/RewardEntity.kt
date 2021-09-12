package com.kylecorry.habits.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kylecorry.habits.domain.Habit
import com.kylecorry.habits.domain.Reward
import java.time.LocalDate

@Entity(tableName = "rewards")
data class RewardEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "cost") val cost: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    fun toReward(): Reward {
        return Reward(id, name, cost)
    }

    companion object {
        fun fromReward(reward: Reward): RewardEntity {
            return RewardEntity(reward.name, reward.cost).also {
                it.id = reward.id
            }
        }
    }

}