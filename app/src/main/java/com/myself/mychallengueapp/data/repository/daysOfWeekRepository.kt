package com.myself.mychallengueapp.data.repository

import com.myself.mychallengueapp.data.dao.DaysOfWeekDAO
import com.myself.mychallengueapp.data.model.Days_Of_Week
import javax.inject.Inject

class daysOfWeekRepository @Inject constructor(private val daysOfWeekDAO: DaysOfWeekDAO){
    suspend fun addDays(days: List<Days_Of_Week>) = daysOfWeekDAO.insertDays(days)
    suspend fun getAllDays() = daysOfWeekDAO.getAllDays()

    suspend fun getDayWithHabits(dayId: Int) = daysOfWeekDAO.getDayWithHabits(dayId)
    suspend fun getAllDaysWithHabits() = daysOfWeekDAO.getAllDaysWithHabits()
}