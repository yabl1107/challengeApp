package com.myself.mychallengueapp.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.myself.mychallengueapp.components.NewHabitDialog
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.viewModels.challengeDetailVM
import com.myself.mychallengueapp.viewModels.challengeVM
import java.time.format.DateTimeFormatter

@Composable
fun ChallengeDetailView(
    navHostController: NavHostController,
    id: Int, challengeDetailVM: challengeDetailVM = hiltViewModel()
) {
    //Text("Challenge detail view id N° ${id}")

    val challengeDetail by challengeDetailVM.challengeState.collectAsState()
    var habitToEdit by remember {mutableStateOf<Pair<HabitDC,List<Boolean>>?>(null)}

    challengeDetail?.let {
        val challengeCreationDate = it.challenge.createdAt
        val challengeEndDate = it.challenge.endDate
        Column (
            Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 15.dp)
                .verticalScroll(rememberScrollState())
        ){
            //Text(text = it.challenge.challengeId.toString(),fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                IconButton(
                    onClick = {
                        navHostController.popBackStack()
                    }
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }

            Text(text = it.challenge.description,fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))

            val timeFormatted = it.challenge.createdAtTime.format(DateTimeFormatter.ofPattern("HH:mm"))

            Text(text="Created at: ${it.challenge.createdAt}   $timeFormatted")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "The reason is: ${it.challenge.reason}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "End date: ${it.challenge.endDate.toString()}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){
                InfoButtonWithDropdown(){ onExpandedChange ->
                    dropMenuItem("Upcoming",20, Color.Yellow) { onExpandedChange(false)}
                    dropMenuItem("Missed",20,MaterialTheme.colorScheme.error){onExpandedChange(false)}
                    dropMenuItem("Done",20,MaterialTheme.colorScheme.tertiary) {onExpandedChange(false) }
                }
            }

            it.habits.forEach { habitWithDays ->
                habitWithDays.let { el ->
                    var days = mutableListOf<Boolean>(false,false,false,false,false,false,false)
                    el.days.forEach { habit_Days ->
                        days[habit_Days.dayId] = true
                    }


                    val stats =challengeDetailVM.countMatchingDaysWithProgressAndCompletion(
                        el.daysLogs,
                        el.habit.createdAt,
                        challengeEndDate,
                        days
                    )
                    /*
                    val stats = com.myself.mychallengueapp.viewModels.challengeDetailVM.ProgressStats(
                        total = 10,
                        passed = 5,
                        completed = 3,
                        nextDay = null
                    )*/


                    //Text(text= "Progress Bar: ${total.second}/${total.first}")
                    //Text(text = "Completed: ${total.third}  Uncompleted: ${total.second-total.third}")

                    val arg = Pair(el.habit,days)
                    val progress = stats.passed.toFloat()/stats.total.toFloat()
                    val safeProgress = if (progress.isNaN()) 0f else progress
                    val upcoming = stats.total-stats.passed
                    val missed = stats.passed-stats.completed
                    val completed = stats.completed

                    habitElement(
                        habit = arg,
                        progress = safeProgress,
                        showProgress = true,
                        nextDay = stats.nextDay,
                        data = Triple(upcoming,missed,completed)
                    ){}

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }

    if(habitToEdit!=null){
        NewHabitDialog(
            settedInfo = habitToEdit,
            onConfirm = {habit, list ->
                //Agregar editar funcion
                challengeDetailVM.editHabit(habit,list)
                habitToEdit = null
            },
            onDismisss = {
                habitToEdit = null
            }
        )
    }



}