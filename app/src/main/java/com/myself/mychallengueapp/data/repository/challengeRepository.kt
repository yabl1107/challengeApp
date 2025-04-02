package com.myself.mychallengueapp.data.repository

import androidx.room.Transaction
import com.myself.mychallengueapp.data.dao.challengeDAO
import com.myself.mychallengueapp.data.dao.habitDAO
import com.myself.mychallengueapp.data.dao.habitDaysDAO
import com.myself.mychallengueapp.data.model.ChallengeDC
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.model.Habit_Days
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class challengeRepository @Inject constructor(private val challengeDAO: challengeDAO,
    private val habitDAO: habitDAO, private val habitDaysDAO: habitDaysDAO
){
    suspend fun addChallengue(challenge: ChallengeDC) = challengeDAO.insert(challenge)
    suspend fun updateChallengue(challenge: ChallengeDC) = challengeDAO.update(challenge)
    suspend fun deleteChallengue(challenge: ChallengeDC) = challengeDAO.delete(challenge)

    fun getAllChallenges(): Flow<List<ChallengeDC>> = challengeDAO.getAllChallenges().flowOn(
        Dispatchers.IO).conflate()

    suspend fun getChallengeWithHabits(challengeId: Long) = challengeDAO.getChallengeWithHabits(challengeId)

    @Transaction
    suspend fun addChallengeWithHabits(challenge: ChallengeDC, habits: List<Pair<HabitDC, List<Habit_Days>>>) {
        val challengeId = challengeDAO.insert(challenge) // Insertar Challenge y obtener ID

        //Insertar cada habito con el id correcto
        habits.forEach{ (habit,selectedDays) ->
            val updatedHabit = habit.copy(challengeId=challengeId.toInt())
            val habitId = habitDAO.insert(updatedHabit)
            val updatedDays = selectedDays.map { habit_days ->
                habit_days.copy(habitId = habitId.toInt())
            }
            habitDaysDAO.insertAll(updatedDays) // Insertar Habits con challengeId correcto
        }
    }
}