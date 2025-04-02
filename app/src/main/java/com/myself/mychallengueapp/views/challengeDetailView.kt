package com.myself.mychallengueapp.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.viewModels.challengeDetailVM
import com.myself.mychallengueapp.viewModels.challengeVM

@Composable
fun ChallengeDetailView(
    navHostController: NavHostController,
    id: Int, challengeDetailVM: challengeDetailVM = hiltViewModel()
) {
    //Text("Challenge detail view id N° ${id}")

    val challengeDetail by challengeDetailVM.challengeState.collectAsState()

    challengeDetail?.let {
        Column (
            Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 15.dp)
        ){
            //Text(text = it.challenge.challengeId.toString(),fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Text(text = it.challenge.description,fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "The reason is: ${it.challenge.reason}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "End date: ${it.challenge.endDate.toString()}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(10.dp))
            it.habits.forEach { habitWithDays ->
                habitWithDays.let { el ->

                    var days = mutableListOf<Boolean>(false,false,false,false,false,false,false)
                    el.days.forEach { habit_Days ->
                        days[habit_Days.dayId] = true
                    }
                    val arg = Pair(el.habit,days)
                    habitElement(arg)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }


}