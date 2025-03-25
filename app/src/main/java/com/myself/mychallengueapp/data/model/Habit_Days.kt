package com.myself.mychallengueapp.data.model

import androidx.room.Entity

@Entity(tableName = "habit_days",primaryKeys = ["habitId","dayId"])
data class Habit_Days(
    val habitId: Int,
    val dayId: Int
)
