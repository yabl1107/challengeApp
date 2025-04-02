package com.myself.mychallengueapp.views

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myself.mychallengueapp.components.CalendarRow
import com.myself.mychallengueapp.data.model.HabitDC
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
        .padding(10.dp))
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

@Composable
fun HabitDay(
    modifier: Modifier = Modifier,
    habit: HabitDC, selectedDate: LocalDate,
    done : Boolean,
    dayVM: dayVM ) {

    var isEnabled = (selectedDate == LocalDate.now() &&
            (habit.tipo==1 || habit.timeSpecific?.isAfter(LocalTime.now()) == true)
            ) //Tarea de hoy, Si es tipo task o tiempo especifico despues de ahora (Aun se puede hacer)

    val colors = MaterialTheme.colorScheme

    val habitDayColor : Pair<Color,Color> = if (done) Pair(colors.tertiary, colors.onTertiary)
    else if (isEnabled) Pair(colors.primary,colors.onTertiary)
    else Pair(colors.error,colors.onError)

    Column (modifier = modifier
        .fillMaxWidth()
        .background(
            habitDayColor.first,
            shape = RoundedCornerShape(10.dp)
        )
        .padding(10.dp)
    ){
        Row(){
            CompositionLocalProvider(LocalContentColor provides habitDayColor.second) {
                Column() {
                    Text(text = habit.description)
                    //Text("tipo ${habit.tipo}")
                    //Text(text= "Habit id: ${habit.habitId}")
                    if (habit.timeSpecific != null) {
                        Text(text = "Do Until: ${habit.timeSpecific}")
                    }
                    if (habit.tipo == 1) {
                        Text(text = "Do it for: ${habit.duration}")
                    }
                }


                if(isEnabled)
                {
                    Button(
                        enabled = (isEnabled),
                        onClick = { dayVM.toggleHabitLog(habit.habitId, !done, selectedDate) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (done) colors.tertiary else colors.secondary, // Verde cuando está marcado
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .size(50.dp),
                        border = BorderStroke(
                            2.dp,
                            if (done) colors.onTertiary else colors.onSecondary
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }



        }

    }
    }
}


