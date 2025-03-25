package com.myself.mychallengueapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.myself.mychallengueapp.data.model.Days_Of_Week
import com.myself.mychallengueapp.data.model.HabitDC


data class daysWithHabits(
    @Embedded val day : Days_Of_Week,
    @Relation(
        parentColumn = "dayId",
        entityColumn = "habitId"
    )
    val habits : List<HabitDC>
)
