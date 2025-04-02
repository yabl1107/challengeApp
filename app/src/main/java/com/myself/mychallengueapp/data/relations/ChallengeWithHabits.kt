package com.myself.mychallengueapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.myself.mychallengueapp.data.model.ChallengeDC
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.model.Habit_Days

data class ChallengeWithHabits(
    @Embedded val challenge: ChallengeDC, //Objeto challengue
    @Relation(
        entity = HabitDC::class, // Especificamos que queremos recuperar HabitDC
        parentColumn = "challengeId",
        entityColumn = "challengeId"
    )
    val habits: List<habitWIthDays> //Lista de objetos habit
)
