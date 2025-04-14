package com.myself.mychallengueapp.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.myself.mychallengueapp.components.CalendarRow
import com.myself.mychallengueapp.components.HabitDay
import com.myself.mychallengueapp.viewModels.dayVM
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayView(
    dayViewVM: dayVM = hiltViewModel()
) {
    val selectedDate = dayViewVM.selectedDate
    val habitDaysList = dayViewVM.habitDaysList.collectAsState()
    val logs = dayViewVM.logsList.collectAsState()

    val selectedHabits = dayViewVM.selectedHabits
    val colors = MaterialTheme.colorScheme

    val timerState = dayViewVM.timerState
    val savedTimers = dayViewVM.savedTimers



    Column(
        modifier = Modifier
            .padding(10.dp)
    )
    {
        CalendarRow(startDate = LocalDate.now(),
            daysToShow = 14,
            selectedDate = selectedDate,
            onClick = { newDate -> dayViewVM.onChangeDate(newDate) })

        Spacer(modifier = Modifier.height(15.dp))


        timerState?.let{
            AlertDialog(
                title = { Text(timerState.description) },
                onDismissRequest = {
                    dayViewVM.resetTimer()
                },
                shape = MaterialTheme.shapes.medium,
                confirmButton = {},
                text = {
                    val timeLeft = (timerState.duration * 60 - timerState.timeElapsed).coerceAtLeast(0)
                    val minutes = timeLeft / 60
                    val seconds = timeLeft % 60

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ){
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                        ) {
                            TimerCircle(timeLeft = timeLeft, duration = it.duration)
                            Text(
                                text = String.format("%02d:%02d", minutes, seconds),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                },
                dismissButton = {
                    Button(onClick = { dayViewVM.resetTimer() }) {
                        Text("Cancel")
                    }
                }
            )
        }



        Column(
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            Row()
            {
                Column(
                    modifier = Modifier.weight(4f)
                )
                {
                    Text(
                        text = "${selectedDate.dayOfWeek.name} ${selectedDate.dayOfMonth}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (selectedDate.isEqual(LocalDate.now())) "Tasks due today: ${selectedHabits.size}" else "Tasks: ${selectedHabits.size}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )

                }
                Box(modifier = Modifier.weight(1f)) {
                    InfoButtonWithDropdown(){ onExpandedChange ->
                        dropMenuItem("Time expired",20,MaterialTheme.colorScheme.error){onExpandedChange(false)}
                        dropMenuItem("Available for completion",20,MaterialTheme.colorScheme.primary) { onExpandedChange(false)}
                        dropMenuItem("Completed",20,MaterialTheme.colorScheme.tertiary) {onExpandedChange(false) }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn() {
                items(selectedHabits) { habit ->
                    //Log.d("habit","description:  ${habit.description} , tipo : ${habit.tipo}, time: ${habit.timeSpecific}, localtime: ${LocalTime.now()}, isAfter: ${habit.timeSpecific?.isAfter(LocalTime.now())}")
                    var timeDone = 0L
                    if(selectedDate.isEqual(LocalDate.now())){
                        dayViewVM.getTimeifExists(habit.habitId,selectedDate)
                        timeDone = savedTimers["${habit.habitId}_${selectedDate}"] ?: 0L
                    }
                    HabitDay(
                        habit = habit,
                        selectedDate = selectedDate,
                        done = dayViewVM.getState(habit.habitId, selectedDate),
                        timeDone = timeDone
                    ){ habitId, desc, b, selectedDate, duration ->
                        if(duration != null && duration>0){ //Time Habit
                            //Open Clock
                            dayViewVM.startTimer(habitId,desc, b, selectedDate, duration)
                        }else{
                            dayViewVM.toggleHabitLog(habitId, b, selectedDate)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
    LaunchedEffect(logs.value) {
        dayViewVM.getSelectedDayHabits(selectedDate)
    }
    LaunchedEffect(savedTimers) {
        dayViewVM.getSelectedDayHabits(selectedDate)
    }
}


@Composable
fun InfoButtonWithDropdown(content :  @Composable (onExpandedChange: (Boolean) -> Unit)-> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = Color.Gray
            )
        }

        // DropdownMenu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },  // Cierra el menú si se toca fuera
        ) {
            content(){ expanded = it }
        }
    }
}

@Composable
fun dropMenuItem(text : String, size : Int, color : Color, onClick : (Boolean) -> Unit) {
    DropdownMenuItem(
        text = {
            Row() {
                Box(
                    modifier = Modifier
                        .background(
                            color,
                            CircleShape
                        )
                        .size(size.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text)
            }
        },
        onClick = {
            onClick(false) // Cerramos el menú después de hacer clic
        }
    )
}





@Composable
fun TimerCircle(timeLeft: Long, duration: Long) {
    val sweepAngle = (360f * (1 - (timeLeft.toFloat() / (duration * 60)))) // Ángulo del arco, basado en el tiempo restante

    Canvas(modifier = Modifier.size(150.dp)) {
        // Dibujar el fondo del círculo (circular background)
        drawCircle(color = Color.Gray, style = Stroke(width = 10f))

        // Dibujar el arco que va decreciendo conforme el tiempo
        drawArc(
            color = Color.Green,
            startAngle = -90f,  // Empieza desde arriba (12:00)
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = 12f)
        )
    }
}