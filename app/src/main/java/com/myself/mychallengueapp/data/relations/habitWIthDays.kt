package com.myself.mychallengueapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.myself.mychallengueapp.data.model.Days_Of_Week
import com.myself.mychallengueapp.data.model.HabitDC

data class habitWIthDays(
    @Embedded val habit : HabitDC,
    @Relation(
        parentColumn = "habitId",
        entityColumn = "dayId",
    )
    val days : List<Days_Of_Week>
)
