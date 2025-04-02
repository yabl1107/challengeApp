package com.myself.mychallengueapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "habit_days",
    primaryKeys = ["habitId","dayId"],
    foreignKeys = [
        ForeignKey(
            entity = HabitDC::class,
            parentColumns = ["habitId"],
            childColumns = ["habitId"],
            onDelete = CASCADE
        )
    ]
)
data class Habit_Days(
    val habitId: Int,
    val dayId: Int
)
