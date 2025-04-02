package com.myself.mychallengueapp.data.repository

import com.myself.mychallengueapp.data.dao.habitLogsDAO
import com.myself.mychallengueapp.data.model.habitLogs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject

class habitLogsRepository  @Inject constructor(private val habitLogsDAO: habitLogsDAO){
    suspend fun addHabitLog(habitLog: habitLogs) = habitLogsDAO.insert(habitLog)
    fun getAllHabitLogs() = habitLogsDAO.getAllHabitLogs().flowOn(Dispatchers.IO).conflate()
    suspend fun deleteHabitLog(habitId : Int, date: LocalDate) = habitLogsDAO.delete(habitId,date)
}