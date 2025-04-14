package com.myself.mychallengueapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.viewModels.dayVM
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun HabitDay(
    modifier: Modifier = Modifier,
    habit: HabitDC, selectedDate: LocalDate,
    done : Boolean,
    timeDone : Long,
    onClickButton: (habitId: Int, description:String, b: Boolean, selectedDate: LocalDate, duration: Long?) -> Unit
) {

    val isEnabled = (
            selectedDate == LocalDate.now()
                    && (habit.timeSpecific == null || habit.timeSpecific.isAfter(LocalTime.now()))
                    && !(habit.duration!=null && done)
            )
    //var isEnabled = true
    //Tarea de hoy, Si tiene tiempo especifico despues de ahora (Aun se puede hacer)

    val colors = MaterialTheme.colorScheme

    val habitDayColor : Pair<Color, Color> = if (done) Pair(colors.tertiary, colors.onTertiary)
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

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            CompositionLocalProvider(LocalContentColor provides habitDayColor.second) {
                Column(
                    modifier = Modifier.weight(3f)
                ) {
                    Text(text = habit.description)
                    //Text("tipo ${habit.tipo}")
                    //Text(text= "Habit id: ${habit.habitId}")
                    if (habit.timeSpecific != null) {
                        Text(text = "Do Until: ${habit.timeSpecific}")
                    }
                    if (habit.tipo == 0) {
                        Text(text = "Do it for: ${habit.duration} min")
                    }
                    if (habit.tipo == 0 && timeDone>0) {
                        val totalSeconds = habit.duration!! * 60 - timeDone
                        val minutes = totalSeconds / 60
                        val seconds = totalSeconds % 60
                        Text(text = "Remaining: ${minutes}m ${seconds}s")
                    }
                }

                if(isEnabled)
                {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ){
                        Button(
                            enabled = (isEnabled),
                            onClick = {
                                onClickButton(habit.habitId,habit.description, !done, selectedDate, habit.duration)},
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
                                imageVector = if (habit.tipo == 0) Icons.Default.PlayArrow else Icons.Default.Check,
                                contentDescription = "Check",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

        }
    }
}




// Segundo color (Ejemplo)
