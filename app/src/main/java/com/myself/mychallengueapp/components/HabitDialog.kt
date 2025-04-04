package com.myself.mychallengueapp.components

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myself.mychallengueapp.R
import com.myself.mychallengueapp.data.model.HabitDC
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun previewNewHabitDialog(){
    NewHabitDialog(
        onConfirm = { a,b ->
        }
    ) {

    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun NewHabitDialog(settedInfo : Pair<HabitDC,List<Boolean>>? = null, onConfirm: (HabitDC, List<Boolean>) -> Unit, onDismisss: () -> Unit) {

    var defHabit = settedInfo?.first ?: HabitDC()
    var defDays = settedInfo?.second ?: List(7) {false}
    val title = settedInfo?.let { "Edit Habit" } ?: "New Habit"
    val textBtn = settedInfo?.let { "Edit "} ?: "Add"


    var habitName by remember { mutableStateOf(defHabit.description) }

    var isChecked by remember { mutableStateOf(if(defHabit.duration!=null) true else false) }
    var isTimeSpecific by remember { mutableStateOf(if(defHabit.timeSpecific!=null) true else false) }

    //var isChecked by remember { mutableStateOf(true) }
    //var isTimeSpecific by remember { mutableStateOf(true) }

    var duration by remember { mutableStateOf<Long?>(defHabit.duration) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(defHabit.timeSpecific)  }
    var showTimePicker by remember { mutableStateOf(false) }
    val selectedDays = remember { mutableStateOf(defDays) }

    //Validaciones
    var nameValid by remember { mutableStateOf(true) }
    var durationValid by remember { mutableStateOf(true) }
    var daysValid by remember { mutableStateOf(true) }
    var timeValid by remember { mutableStateOf(true) }

    val enableButton by remember {
        derivedStateOf {
            selectedDays.value.contains(true) && nameValid && daysValid && (durationValid || !isChecked) && (timeValid || !isTimeSpecific)
        }
    }

    AlertDialog(
        onDismissRequest = onDismisss,
        title = { Text(text = title) },
        text = {
            Column {
                TextField(
                    value = habitName,
                    onValueChange = { newValue ->
                        habitName = newValue
                        nameValid = newValue.isNotBlank()
                                    },
                    label = { Text(text = "Habit Name") }
                )
                if(!nameValid){
                    Text(
                        text = "This field cannot be empty",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                SelectableDaysRow(
                    selectedDays = selectedDays.value,
                    onDaySelected = { index ->
                        val updatedDays = selectedDays.value.toMutableList()
                        updatedDays[index] = !updatedDays[index]
                        selectedDays.value = updatedDays
                        daysValid = selectedDays.value.contains(true)
                    }
                )
                if(!daysValid){
                    Text(
                        text = "You must select at least one day",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                    Text(text = "Time Habit")
                }
                if(isChecked) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        var showMin = duration?.div(3600) ?: duration
                        TimeInputField(
                            value = showMin,
                            onTimeChange = {
                                duration = it
                                durationValid = it > 0
                            }
                        )

                    }
                    if (!durationValid) {
                        Text(
                            text = "This field cannot be empty",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                else{
                    duration = null
                }



                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isTimeSpecific,
                        onCheckedChange = { isTimeSpecific = it }
                    )
                    Text(text = "Time Specific")
                }

                if(isTimeSpecific){
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        TextField(
                            value = selectedTime?.toString() ?: "",
                            onValueChange = {

                            },
                            readOnly = true,
                            label = { Text("Fixed time") },
                            modifier = Modifier.weight(4f),
                        )
                        IconButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                showTimePicker = true
                            }
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.reloj_100),
                                contentDescription = "Clock Icon",
                                modifier = Modifier,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                        }
                    }
                    if(!timeValid){
                        Text(
                            text = "This field cannot be empty",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }


                }



            }

            // Mostrar TimePickerDialog si showTimePicker es true
            if (showTimePicker) {
                TimePickerDialog(
                    LocalContext.current,
                    { _, hour, minute ->
                        selectedTime = LocalTime.of(hour, minute)
                        timeValid = true
                        showTimePicker = false
                    },
                    0, 0, true
                ).apply {
                    setOnCancelListener { showTimePicker = false }  // Manejar la cancelación
                }.show()
            }


        },
        confirmButton = {

            Button(
                enabled = enableButton,
                onClick = {

                    if(habitName.isBlank()) nameValid = false
                    if(isChecked && duration == null) durationValid = false
                    if(isTimeSpecific && selectedTime == null) timeValid = false
                    if(!selectedDays.value.contains(true)) daysValid = false

                    if(enableButton){
                        val newHabit = HabitDC(
                            //Variables no modificables
                            habitId = defHabit.habitId,
                            challengeId = defHabit.challengeId,
                            //Variables modificales
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
                Text(textBtn)
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
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        days.forEachIndexed { index, day ->
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .padding(0.5.dp)
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