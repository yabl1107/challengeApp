package com.myself.mychallengueapp.data.repository

import com.myself.mychallengueapp.data.dao.habitDaysDAO
import com.myself.mychallengueapp.data.model.Habit_Days
import javax.inject.Inject

class habitDaysRepository  @Inject constructor(private val habitDaysDAO: habitDaysDAO){
    suspend fun addHabitDay(habitDay: Habit_Days) = habitDaysDAO.insert(habitDay)
    suspend fun getAllHabitDays() = habitDaysDAO.getAllHabitDays()
}