package com.myself.mychallengueapp.data.repository

import com.myself.mychallengueapp.data.dao.habitDaysDAO
import com.myself.mychallengueapp.data.model.Habit_Days
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class habitDaysRepository  @Inject constructor(private val habitDaysDAO: habitDaysDAO){
    suspend fun addHabitDay(habitDay: Habit_Days) = habitDaysDAO.insert(habitDay)
    fun getAllHabitDays() : Flow<List<Habit_Days>> = habitDaysDAO.getAllHabitDays().flowOn(
        Dispatchers.IO).conflate()
    suspend fun getHabitDaysByDayId(habitId: Int) = habitDaysDAO.getHabitDaysByDayId(habitId)

    suspend fun addAllHabitDays(habitDays: List<Habit_Days>) = habitDaysDAO.insertAll(habitDays)

    suspend fun deleteHabitDay(habitDay: Habit_Days) = habitDaysDAO.delete(habitDay)
    suspend fun deleteByHabitId(habitId: Int) = habitDaysDAO.deleteByHabitId(habitId)
}