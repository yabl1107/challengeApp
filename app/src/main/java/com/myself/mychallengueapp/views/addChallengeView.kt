package com.myself.mychallengueapp.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myself.mychallengueapp.viewModels.addChallengeVM
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.myself.mychallengueapp.R
import com.myself.mychallengueapp.components.DatePickerDialogExample2
import com.myself.mychallengueapp.components.LoaderDataLottie
import com.myself.mychallengueapp.components.NewHabitDialog
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.navigation.Routes


@Composable
fun addChallenge(navController: NavController, addChallengeVM: addChallengeVM = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }


    val state = addChallengeVM.state
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 50.dp)
            .padding(horizontal = 40.dp),
        contentAlignment = Alignment.TopCenter
    ){
        Column (
            //horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(modifier = Modifier
                    .weight(1.5f)
                    .height(100.dp)
                ){
                    LoaderDataLottie(R.raw.avion)
                }
                Column (modifier = Modifier
                    .weight(2f)
                ){
                    Text(text = "Let's create")
                    Text(text = "A new challenge...")
                }
            }
            spacer(10)
            Text(
                text = "What is something you want to achieve?"
            )
            spacer(10)
            TextField(
                value = state.description,
                onValueChange = {addChallengeVM.onValue(it,"description")},
                label = {Text("Describe your goal")},
                modifier = Modifier.fillMaxWidth()
            )
            if(!state.validDescription){
                Text(
                    text = "This field cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            spacer(10)
            Text(
                text = "Why is this important to you?"
            )
            spacer(10)
            TextField(
                value = state.reason,
                onValueChange = {addChallengeVM.onValue(it,"reason")},
                label = {Text("Your motivation")},
                modifier = Modifier.fillMaxWidth()
            )
            if(!state.validReason){
                Text(
                    text = "This field cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            spacer(10)
            Text(
                text = "Set a target completion date"
            )
            spacer(10)

            Row (
                verticalAlignment = Alignment.CenterVertically,
            ){
                TextField(
                    value = state.endDate?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("End Date") },
                    modifier = Modifier.weight(4f),
                )
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                    addChallengeVM.showCalendar(true)
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
            if(!state.validEndDate){
                Text(
                    text = "This field cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            spacer(15)

            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically){
                Text(text = "Habits to develop")
                IconButton(onClick={showDialog = true}){
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }
            if(!state.validHabits){
                Text(
                    text = "Add at least one habit",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            state.habitList.forEachIndexed { index,habit ->
                habitElement(habit){
                    addChallengeVM.editHabit(habit)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            spacer(15)
            Button(modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterHorizontally),
                onClick = {
                    if(addChallengeVM.validateNewChallenge()){
                        addChallengeVM.addChallenge()
                        navController.navigate(Routes.ChallengeView.route)
                    }
                }
            ){
                Text(text = "Save")
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.save_50),
                    contentDescription = "save_icon",
                    colorFilter = ColorFilter.tint( MaterialTheme.colorScheme.onPrimary )
                )

            }
        }

    }

    // Dialog para agregar hábito
    if (showDialog) {
        NewHabitDialog(
            onConfirm = {
                    newHabit, selectedDays ->
                addChallengeVM.newHabit(newHabit, selectedDays)
            },
            onDismisss = {
                showDialog = false
            }
        )
    }

    //Dialog para editar habito
    if(state.habitToEdit!=null){
        NewHabitDialog(
            settedInfo = state.habitToEdit,
            onConfirm = {habit, list ->
                //Agregar editar funcion
                addChallengeVM.editHabitAtIndex(state.habitList.indexOf(state.habitToEdit),habit,list)
                addChallengeVM.editHabit(null)
            },
            onDismisss = {
                addChallengeVM.editHabit(null)
            }
        )
    }




    DatePickerDialogExample2(state.showCalendar,
        onDateSelected = { dateSelected ->
            addChallengeVM.updateEndDate(dateSelected)
            addChallengeVM.showCalendar(false)
        },
        onDismiss = {
            addChallengeVM.showCalendar(false)
        }
    )


}



@Composable
fun spacer(value : Int) {
    Spacer(modifier = Modifier.height(value.dp))
}

@Preview(showBackground = true)
@Composable
fun habitElemPreview() {
    val habit = HabitDC(description = "Hacer ejercicio", tipo = 0)
    val days = listOf(true,false,false,false,false,false,false)
    habitElement(Pair(habit,days)){

    }
}


@Composable
fun habitElement( habit : Pair<HabitDC,List<Boolean>>,onEditHabit : () -> Unit) {
    //val habit = HabitDC(description = "Hacer ejercicio", tipo = 0)

    Card (
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp), // Sombra
        modifier = Modifier
    ){
        val fs = 14.sp
        Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)) {
            Text(text = habit.first.description, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Row (modifier = Modifier.padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (modifier = Modifier
                .weight(2f)
            ){
                when(habit.first.tipo){//0 - TimeHabit , 1 - habit
                    0 -> Text(text = "Time Habit", fontSize = fs)
                    1 -> Text(text = "Task Habit", fontSize = fs)
                }
                if(habit.first.duration != null){
                    Text(text = "Duration: ${habit.first.duration}", fontSize = fs)
                }
                if(habit.first.timeSpecific != null){
                    Text(text = "Time Specific: ${habit.first.timeSpecific}", fontSize = fs)
                }

                val selectedDays = habit.second
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp), horizontalArrangement = Arrangement.Start) {
                    val days = listOf("L", "M", "M", "J", "V", "S", "D")
                    days.forEachIndexed { index, day ->
                        Text(
                            text = day,
                            modifier = Modifier.padding(end = 8.dp),
                            fontWeight = FontWeight.Bold,
                            color = if (selectedDays[index]) Color.Green else Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            IconButton(onClick = {
            /*TODO*/
                onEditHabit()
            },
                modifier = Modifier.weight(0.7f)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}











