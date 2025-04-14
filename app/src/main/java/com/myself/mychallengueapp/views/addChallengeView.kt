package com.myself.mychallengueapp.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
import com.myself.mychallengueapp.ui.theme.myLightGreen
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.LocalDate
import kotlin.math.roundToInt


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
                    readOnly = true,
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

                val delete = SwipeAction(
                    icon = rememberVectorPainter(Icons.Default.Delete),
                    background = MaterialTheme.colorScheme.error,
                    onSwipe = {
                        addChallengeVM.deleteHabitAtIndex(index)
                    }
                )
                SwipeableActionsBox(endActions = listOf(delete), swipeThreshold = 270.dp) {
                    habitElement(habit){
                        addChallengeVM.editHabit(habit)
                    }
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



@Composable
fun habitElement(
    habit : Pair<HabitDC,List<Boolean>>,
    progress: Float = 0f,
    showProgress: Boolean = false,
    nextDay : LocalDate? = null,
    data: Triple<Int, Int, Int>? = null,
    onEditHabit : () -> Unit) {
    Log.d("Mendsaje desde habitElement :::: ", "Progress : ${progress} , habit : ${habit.first.description})")
    Card (
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp), // Sombra
        modifier = Modifier
    ){
        val fs = 14.sp

        if(showProgress)
        {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            //Icon
            Box(
                modifier = Modifier.padding(vertical = 10.dp).height(23.dp),
                contentAlignment = Alignment.Center
            ){
                when(habit.first.tipo){//0 - TimeHabit , 1 - habit
                    0 -> {
                        Image(
                            painter = painterResource(id = R.drawable.clock),
                            contentDescription = "Time Habit",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )
                    }

                    1 -> {
                        Image(
                            painter = painterResource(id = R.drawable.task_icon),
                            contentDescription = "Task Habit",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if(showProgress) {
                Text(
                    text = "${(progress * 100).roundToInt()}%",
                    fontSize = 15.sp
                )
            }
        }

        Row(modifier = Modifier.padding(horizontal = 15.dp)) {
            Text(text = habit.first.description, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Row (modifier = Modifier
            .padding(horizontal = 15.dp)
            .height(IntrinsicSize.Min) ,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (modifier = Modifier
                .weight(2f)
            ){

                nextDay?.let {
                    Text(text = "next: $it", fontSize = fs)
                }

                habit.first.duration?.let{
                    Text(text = "Duration: ${habit.first.duration} min", fontSize = fs)
                }
                habit.first.timeSpecific?.let{
                    Text(text = "Time Specific: ${habit.first.timeSpecific}", fontSize = fs)
                }

                val selectedDays = habit.second
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp), horizontalArrangement = Arrangement.Start) {
                    val days = listOf("M", "T", "W", "T", "F", "S", "S")
                    val color = if(isSystemInDarkTheme()) Color.Green else myLightGreen
                    days.forEachIndexed { index, day ->
                        Text(
                            text = day,
                            modifier = Modifier.padding(end = 8.dp),
                            fontWeight = FontWeight.Bold,
                            color = if (selectedDays[index]) color else Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            if(!showProgress){
                IconButton(onClick = {
                    /*TODO*/
                    onEditHabit()
                },
                    modifier = Modifier.weight(0.7f)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
            }else{
                Column (
                    modifier = Modifier.weight(0.7f).fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ){
                    CenteredRowItem(Color.Yellow,20,"${data?.first}")
                    CenteredRowItem(MaterialTheme.colorScheme.error,20,"${data?.second}")
                    CenteredRowItem(MaterialTheme.colorScheme.tertiary,20,"${data?.third}")
                }

            }

        }
    }



}



@Composable
fun CenteredRowItem(
    iconColor: Color,
    size : Int,
    text: String
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .background(iconColor, CircleShape)
                .size(size.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text)
    }
}







