package com.myself.mychallengueapp.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myself.mychallengueapp.data.model.ChallengeDC
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.model.Habit_Days
import com.myself.mychallengueapp.data.repository.challengeRepository
import com.myself.mychallengueapp.state.addChallengeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class addChallengeVM @Inject constructor(private val repository: challengeRepository): ViewModel(){
    var state by mutableStateOf(addChallengeState())
        private set


    fun onValue(value: String, text: String){
        when(text){
            "reason" -> state = state.copy(reason=value)
            "description" -> state = state.copy(description=value)
        }
    }
    fun updateEndDate(localDate: LocalDate?) {
        state = state.copy(endDate = localDate)
    }

    fun addChallenge() {
        viewModelScope.launch {
            val newChallenge = ChallengeDC(
                description = state.description,
                reason = state.reason,
                endDate = state.endDate
            )
            val habitsWithDays: List<Pair<HabitDC, List<Habit_Days>>> =
                state.habitList.map { (habit, selectedDays) ->
                    val habitDays = selectedDays.mapIndexedNotNull { index, isSelected ->
                        if (isSelected) Habit_Days(habitId = 0, dayId = index) else null
                    }
                    habit to habitDays //es lo mismo que Pair(habit, habitDays)
                }
            repository.addChallengeWithHabits(newChallenge, habitsWithDays)
        }
    }

    fun newHabit(habit: HabitDC, selectedDays: List<Boolean>){
        val newList = state.habitList + Pair(habit, selectedDays)
        state = state.copy(habitList = newList )
    }

    fun showCalendar(value : Boolean){
        state = state.copy(showCalendar = value)
    }

}