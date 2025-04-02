package com.myself.mychallengueapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.model.Habit_Days

data class habitWIthDays(
    @Embedded val habit : HabitDC,
    @Relation(
        parentColumn = "habitId",
        entityColumn = "habitId",
    )
    val days : List<Habit_Days>
)
