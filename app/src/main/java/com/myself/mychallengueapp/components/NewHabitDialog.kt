package com.myself.mychallengueapp.components

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.myself.mychallengueapp.data.model.HabitDC
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("UnrememberedMutableState")
@Composable
fun NewHabitDialog(onConfirm: (HabitDC, List<Boolean>) -> Unit, onDismisss: () -> Unit) {
    var habitName by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var isTimeSpecific by remember { mutableStateOf(false) }
    var duration by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null)  }
    var showTimePicker by remember { mutableStateOf(false) }
    val selectedDays = remember { mutableStateOf(List(7) { false }) }



    AlertDialog(
        onDismissRequest = onDismisss,
        title = { Text(text = "New Habit") },
        text = {
            Column {
                TextField(
                    value = habitName,
                    onValueChange = { habitName = it },
                    label = { Text(text = "Habit Name") }
                )
                SelectableDaysRow(
                    selectedDays = selectedDays.value,
                    onDaySelected = { index ->
                        val updatedDays = selectedDays.value.toMutableList()
                        updatedDays[index] = !updatedDays[index]
                        selectedDays.value = updatedDays
                    }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                    Text(text = "Time Habit")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if(isChecked){
                        TimeInputField(onTimeChange = { duration = it })
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isTimeSpecific,
                        onCheckedChange = { isTimeSpecific = it }
                    )
                    Text(text = "Time Specific")
                }

                Row {

                    if(isTimeSpecific){
                        if(selectedTime == null){
                            Text(text="Seleccionar hora")
                        }
                        else{
                            Text("Hora seleccionada: ${selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                        }

                        Button(onClick = { showTimePicker = true }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }

                    // Mostrar TimePickerDialog si showTimePicker es true
                    if (showTimePicker) {
                        TimePickerDialog(
                            LocalContext.current,
                            { _, hour, minute ->
                                selectedTime = LocalTime.of(hour, minute)
                                showTimePicker = false
                            },
                            0, 0, true
                        ).apply {
                            setOnCancelListener { showTimePicker = false }  // Manejar la cancelación
                        }.show()
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (habitName.isNotBlank()) {
                    val newHabit = HabitDC(
                        description = habitName,
                        tipo = if (isChecked) 0 else 1, //0 - time habit , 1 - task habit
                        duration = if (isChecked) duration else null,
                        timeSpecific = if (isTimeSpecific) selectedTime else null
                    )
                    onConfirm(newHabit,selectedDays.value)  // Llamar a onConfirm con el nuevo hábito
                    habitName = ""  // Limpiar el campo de texto
                    onDismisss()  // Cerrar el modal
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismisss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SelectableDaysRow(selectedDays : List<Boolean>, onDaySelected: (Int)->Unit) {
    val days = listOf("L", "M", "M", "J", "V", "S", "D")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        days.forEachIndexed { index, day ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selectedDays[index]) Color.Green else Color.Gray)
                    .clickable { onDaySelected(index) },
                contentAlignment = Alignment.Center
            ){
                Text(text = day, fontWeight = FontWeight.Bold)
            }
        }
    }
}