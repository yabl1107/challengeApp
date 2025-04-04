package com.myself.mychallengueapp.data.repository

import android.util.Log
import com.myself.mychallengueapp.data.dao.habitDAO
import com.myself.mychallengueapp.data.dao.habitDaysDAO
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.model.Habit_Days
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class habitRepository @Inject constructor(
    private val habitDAO: habitDAO,
    private val habitDaysDAO: habitDaysDAO
){
    suspend fun addHabit(habit: HabitDC) = habitDAO.insert(habit)
    suspend fun updateHabit(habit: HabitDC) = habitDAO.update(habit)
    suspend fun deleteHabit(habit: HabitDC) = habitDAO.delete(habit)

    suspend fun getAllHabitsOnce(): List<HabitDC> = habitDAO.getAllHabitsOnce()

    suspend fun getAllHabits() : Flow<List<HabitDC>> = habitDAO.getAllHabits().flowOn(Dispatchers.IO).conflate()

    suspend fun updateHabitWithDays(habit: HabitDC, selectedDays: List<Boolean>){
        val habitDays = selectedDays.mapIndexedNotNull { index, isSelected ->
            if (isSelected) Habit_Days(habitId = habit.habitId, dayId = index) else null
        }

        Log.d("habitRepository", "Updating habit with ID: ${habit.habitId}")
        Log.d("habitRepository", "Habit Days: $habitDays")
        Log.d("habitRepository", "Habit: $habit")
        habitDAO.update(habit)
        habitDaysDAO.deleteByHabitId(habit.habitId)
        habitDaysDAO.insertAll(habitDays)
    }

}