package com.myself.mychallengueapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.myself.mychallengueapp.viewModels.addChallengeVM
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogExample(
    onDateSelected : (LocalDate) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    val selectedMillis = datePickerState.selectedDateMillis
                    selectedMillis?.let {
                        val localDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(localDate) // Actualiza el ViewModel
                    }
                }) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Button(onClick = { openDialog.value = true }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Seleccionar fecha")
        Text(text = "End Date")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogExample2(
    show : Boolean,
    onDateSelected : (LocalDate) -> Unit,
    onDismiss : () -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    if (show) {
        DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    selectedMillis?.let {
                        val localDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(localDate) // Actualiza el ViewModel
                    }
                }) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@Composable
fun TimeInputField(value : Long?, onTimeChange: (Long) -> Unit) {
    var timeText by remember { mutableStateOf(value?.toString() ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column {
        TextField(
            value = timeText,
            onValueChange = {
                timeText = it
                val minutes = it.toLongOrNull()
                if (minutes != null && minutes > 0) {
                    onTimeChange(minutes * 60 * 1000) // Convertir minutos a milisegundos
                }else{
                    onTimeChange(0)
                }
            },
            label = { Text("Duración (min)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        errorMessage?.let {
            Text(it, color = Color.Red, fontSize = 12.sp)
        }
    }
}


