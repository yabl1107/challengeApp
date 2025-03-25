package com.myself.mychallengueapp.data.repository

import com.myself.mychallengueapp.data.dao.habitLogsDAO
import com.myself.mychallengueapp.data.model.habitLogs
import javax.inject.Inject

class habitLogsRepository  @Inject constructor(private val habitLogsDAO: habitLogsDAO){
    suspend fun addHabitLog(habitLog: habitLogs) = habitLogsDAO.insert(habitLog)
    suspend fun getAllHabitLogs() = habitLogsDAO.getAllHabitLogs()
}