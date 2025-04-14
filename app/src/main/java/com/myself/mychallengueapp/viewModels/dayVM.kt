package com.myself.mychallengueapp.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import com.myself.mychallengueapp.data.repository.timeLogsRepository


@HiltViewModel
class dayVM @Inject constructor(
    private val habit: habitRepository,
    private val logsRepository: habitLogsRepository,
    private val habitDays: habitDaysRepository,
    private val timeLogsRepository: timeLogsRepository
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

    var timerState by mutableStateOf<TimerState?>(null)
        private set
    private var timerJob: Job? = null

    var savedTimers = mutableStateMapOf<String, Long>()
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
            val days = timeLogsRepository.getLastDate()
            if(days!=LocalDate.now().toEpochDay()){
                timeLogsRepository.clearAllTimers()
                timeLogsRepository.setLastDate(LocalDate.now())
            }

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
            habit.habitId in ids && (habit.createdAt.isBefore(date) || habit.createdAt.equals(date))
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

    data class TimerState(
        val habitId: Int,
        val description : String,
        val selectedDate: LocalDate,
        val duration: Long,
        val timeElapsed: Long = 0L,
        val isRunning: Boolean = false,
        val isFinished: Boolean = false
    )
    fun startTimer(habitId: Int, description: String, b: Boolean, selectedDate: LocalDate, duration: Long) {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            val previous = timeLogsRepository.getTimerValueSync(habitId, selectedDate)

            timerState = TimerState(habitId, description ,selectedDate, duration, previous,true)

            while (timerState!!.timeElapsed < duration * 60) {
                delay(1000)
                timerState = timerState!!.copy(
                    timeElapsed = timerState!!.timeElapsed + 1
                )
            }
            //timerState = timerState?.copy(isRunning = false, isFinished = true)

            //Eliminar del dataStore
            val key = "${habitId}_${selectedDate}"
            savedTimers.remove(key)
            timeLogsRepository.deleteTimerValue(habitId, selectedDate)

            //Guardar log terminado
            timerState = null
            toggleHabitLog(habitId, true)
        }
    }

    fun getTimeifExists(habitId: Int, selectedDate: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val previous = timeLogsRepository.getTimerValueSync(habitId, selectedDate)
            val key = "${habitId}_${selectedDate}"
            savedTimers[key] = previous
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerState = timerState?.copy(isRunning = false)
    }

    fun resetTimer() {
        timerJob?.cancel()
        viewModelScope.launch {
            timerState?.let {
                timeLogsRepository.saveTimerValue(
                    it.habitId,
                    it.selectedDate,
                    it.timeElapsed
                )
            }
            timerState = null
        }
    }


    fun getState(habitId: Int, selectedDate: LocalDate): Boolean {
        return _logsList.value.any { logElem ->
            val dayMatch = logElem.date.dayOfWeek.value == selectedDate.dayOfWeek.value
            val habitMatch = logElem.habitId == habitId
            dayMatch && habitMatch
        }
    }

    // Función para generar claves dinámicas basadas en el habitId


    fun clearTimerLogs(){
        viewModelScope.launch (Dispatchers.IO){
            timeLogsRepository.clearAllTimers()
        }
    }

}