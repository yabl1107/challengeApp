package com.myself.mychallengueapp.data.repository

import com.myself.mychallengueapp.data.dao.habitDAO
import com.myself.mychallengueapp.data.model.HabitDC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class habitRepository @Inject constructor(private val habitDAO: habitDAO){
    suspend fun addHabit(habit: HabitDC) = habitDAO.insert(habit)
    suspend fun updateHabit(habit: HabitDC) = habitDAO.update(habit)
    suspend fun deleteHabit(habit: HabitDC) = habitDAO.delete(habit)

    fun getAllHabits() : Flow<List<HabitDC>> = habitDAO.getAllHabits().flowOn(Dispatchers.IO).conflate()

}