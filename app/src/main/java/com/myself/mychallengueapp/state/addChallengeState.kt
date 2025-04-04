package com.myself.mychallengueapp.state

import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.model.Habit_Days
import java.time.LocalDate

data class addChallengeState(
    val reason : String = "",
    val description : String = "",
    val endDate : LocalDate? =  null,

    val validDescription : Boolean = true,
    val validReason : Boolean = true,
    val validEndDate : Boolean = true,
    val validHabits : Boolean = true,

    val isFormValid : Boolean = true,


    val habitList : List<Pair<HabitDC, List<Boolean>>> = emptyList(),
    val habitToEdit : Pair<HabitDC, List<Boolean>>? = null,
    val isSaved : Boolean = false,

    val showCalendar : Boolean = false
)