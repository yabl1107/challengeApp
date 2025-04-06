package com.myself.mychallengueapp.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.Key.Companion.D
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myself.mychallengueapp.data.model.ChallengeDC
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.model.Habit_Days
import com.myself.mychallengueapp.data.model.habitLogs
import com.myself.mychallengueapp.data.relations.ChallengeWithHabits
import com.myself.mychallengueapp.data.relations.habitWIthDays
import com.myself.mychallengueapp.data.repository.challengeRepository
import com.myself.mychallengueapp.data.repository.habitLogsRepository
import com.myself.mychallengueapp.data.repository.habitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class challengeDetailVM @Inject constructor(
    private val repository: challengeRepository,
    private val habitRepository : habitRepository,
    private val habitLogsRepository : habitLogsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){


    private val _challengeState = MutableStateFlow<ChallengeWithHabits?>(null)
    val challengeState: StateFlow<ChallengeWithHabits?> = _challengeState


    init {
        val id = savedStateHandle.get<String>("id")?.toLong()
        id?.let {

            viewModelScope.launch {
                repository.getChallengeWithHabits(it).collect{ challenge ->
                    _challengeState.value = challenge

                    //Procesamos los hábitos del challenge
                    //processHabits(challenge.habits, challenge.challenge)
                }
            }

        }
    }


    fun editHabit(habit : HabitDC, ListDays : List<Boolean>){
        viewModelScope.launch {
            habitRepository.updateHabitWithDays(habit,ListDays)
        }
    }



    data class ProgressStats(
        val total: Int,
        val passed: Int,
        val completed: Int,
        val nextDay: LocalDate?,
    )

    fun countMatchingDaysWithProgressAndCompletion(
        logs : List<habitLogs>,
        startDate: LocalDate,
        endDate: LocalDate?,
        selectedDaysBool: List<Boolean>,
        referenceDate: LocalDate = LocalDate.now()
    ): ProgressStats {


        var completedDates = logs.map{
            it.date
        }
        var selectedDays = selectedDaysBool.mapIndexedNotNull { index, b ->
            if (b) index else null
        }


        var total = 0
        var passed = 0
        var completed = 0
        var date = startDate
        var nextDay : LocalDate? = null

        while (!date.isAfter(endDate)) {
            val dayIndex = date.dayOfWeek.value - 1 // lunes = 0 ... domingo = 6
            if (dayIndex in selectedDays) {
                total++
                if (!date.isAfter(referenceDate)) {
                    passed++
                    if (date in completedDates) {
                        completed++
                    }
                }
            }
            date = date.plusDays(1)
        }

        var date2 = LocalDate.now()
        //Ecnontrar nextDay
        while(!date2.isAfter(endDate)){
            val dayIndex2 = date2.dayOfWeek.value - 1 // lunes = 0 ... domingo = 6
            if (dayIndex2 in selectedDays && !date2.isBefore(startDate)) {
                nextDay = date2
                break
            }
            date2 = date2.plusDays(1)
        }

        return ProgressStats(
            total = total,
            passed = passed,
            completed = completed,
            nextDay = nextDay
        )

    }




}