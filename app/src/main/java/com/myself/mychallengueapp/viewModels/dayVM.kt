package com.myself.mychallengueapp.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.model.Habit_Days
import com.myself.mychallengueapp.data.model.habitLogs
import com.myself.mychallengueapp.data.repository.habitDaysRepository
import com.myself.mychallengueapp.data.repository.habitLogsRepository
import com.myself.mychallengueapp.data.repository.habitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class dayVM @Inject constructor(
    private val habit: habitRepository,
    private val logsRepository: habitLogsRepository,
    private val habitDays: habitDaysRepository,
): ViewModel() {

    private var habitList : List<HabitDC> = emptyList()
    var selectedDate by mutableStateOf(LocalDate.now())
        private set

    private val _habitDaysList = MutableStateFlow<List<Habit_Days>>(emptyList())
    val habitDaysList = _habitDaysList.asStateFlow()

    private val _logsList = MutableStateFlow<List<habitLogs>>(emptyList())
    val logsList = _logsList.asStateFlow()

    var selectedHabits by mutableStateOf<List<HabitDC>>(emptyList())
        private set


    fun onChangeDate(newDate: LocalDate) {
        selectedDate = newDate
        getSelectedDayHabits(selectedDate)
    }


    init {

        viewModelScope.launch(Dispatchers.IO) {
            //Traer habitDays
            habitDays.getAllHabitDays().collect { habitDays ->
                if (habitDays.isNullOrEmpty()) {
                    _habitDaysList.value = emptyList()
                } else {
                    _habitDaysList.value = habitDays
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) { //Collect bloquea hilo
            //Traer los habitos
            habitList = habit.getAllHabitsOnce()
            logsRepository.getAllHabitLogs().collect { habitLogs ->
                if(habitLogs.isNullOrEmpty()){
                    _logsList.value = emptyList()
                }else{
                    _logsList.value = habitLogs
                }
            }
        }

    }

    fun getSelectedDayHabits(date: LocalDate) { //Obtiene los habitos para el dia seleccionado
        val ids = habitDaysList.value.mapNotNull { habitDays ->
            if (habitDays.dayId + 1 == date.dayOfWeek.value) habitDays.habitId else null
        }
        selectedHabits = habitList.filter { habit ->
            habit.habitId in ids
        }
    }

    fun toggleHabitLog(habitId: Int, newStatus: Boolean, date: LocalDate = LocalDate.now()) {
        viewModelScope.launch(Dispatchers.IO) {
            if (newStatus) {
                logsRepository.addHabitLog(habitLogs(habitId = habitId, date = date))
            } else {
                logsRepository.deleteHabitLog(habitId, date)
            }
        }
    }

    fun getState(habitId: Int, selectedDate: LocalDate): Boolean {
        return _logsList.value.any { logElem ->
            val dayMatch = logElem.date.dayOfWeek.value == selectedDate.dayOfWeek.value
            val habitMatch = logElem.habitId == habitId
            dayMatch && habitMatch
        }
    }

}