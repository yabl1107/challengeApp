package com.myself.mychallengueapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.myself.mychallengueapp.data.model.ChallengeDC
import com.myself.mychallengueapp.data.model.HabitDC

data class ChallengeWithHabits(
    @Embedded val challenge: ChallengeDC, //Objeto challengue
    @Relation(
        parentColumn = "challengeId",
        entityColumn = "challengeId"
    )
    val habits: List<HabitDC> //Lista de objetos habit
)
