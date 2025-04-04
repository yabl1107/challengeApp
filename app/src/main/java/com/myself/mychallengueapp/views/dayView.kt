package com.myself.mychallengueapp.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myself.mychallengueapp.components.CalendarRow
import com.myself.mychallengueapp.components.HabitDay
import com.myself.mychallengueapp.viewModels.dayVM
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DayView(
    dayViewVM: dayVM = hiltViewModel()
) {
    val selectedDate = dayViewVM.selectedDate
    val habitDaysList = dayViewVM.habitDaysList.collectAsState()
    val logs = dayViewVM.logsList.collectAsState()

    val selectedHabits = dayViewVM.selectedHabits
    val colors = MaterialTheme.colorScheme

    Column (modifier = Modifier
        .padding(10.dp)
    )
    {
        CalendarRow(startDate = LocalDate.now(),daysToShow = 14, selectedDate = selectedDate
            ,onClick = {newDate -> dayViewVM.onChangeDate(newDate)} )

        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier.padding(horizontal = 15.dp)
        ){
            Text(
                text = "Selected date: ${selectedDate.dayOfMonth}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text="Tasks due today: ${selectedHabits.size}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )
            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn (){
                items(selectedHabits){ habit ->
                    Log.d("habit","description:  ${habit.description} , tipo : ${habit.tipo}, time: ${habit.timeSpecific}, localtime: ${LocalTime.now()}, isAfter: ${habit.timeSpecific?.isAfter(LocalTime.now())}")
                    HabitDay(
                        habit = habit,
                        selectedDate = selectedDate,
                        done = dayViewVM.getState(habit.habitId,selectedDate),
                        dayVM = dayViewVM
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
    LaunchedEffect(logs.value) {
        dayViewVM.getSelectedDayHabits(selectedDate)
    }
}


