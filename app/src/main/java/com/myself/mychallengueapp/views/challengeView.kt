package com.myself.mychallengueapp.views

import androidx.compose.ui.graphics.Color
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.myself.mychallengueapp.R
import com.myself.mychallengueapp.components.LoaderDataLottie
import com.myself.mychallengueapp.data.model.ChallengeDC
import com.myself.mychallengueapp.viewModels.challengeVM
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun challengeView(navHostController: NavHostController, challengeVM: challengeVM = hiltViewModel()) {

    Column (
        modifier = Modifier
            .padding(top = 25.dp)
    ){
        Row (modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
        ){
            Box(modifier= Modifier
                .weight(1f)
                .height(100.dp)
            ){
                LoaderDataLottie(R.raw.challenges)
            }
            PrimaryCard(modifier = Modifier.weight(1f),challengeVM.countChallenges("active"),"Active")
            PrimaryCard(modifier = Modifier.weight(1f),challengeVM.countChallenges("finished"),"Finished")
        }


        val challenges = challengeVM.challenges.collectAsState()

        LazyColumn (
            contentPadding = PaddingValues(16.dp),
        ){
            items(challenges.value) {
                    item ->
                ChallengeElement(navHostController, item)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun ChallengeElement(navHostController: NavHostController, challenge : ChallengeDC) {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp), // Sombra
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable {
                navHostController.navigate("detail/${challenge.challengeId}")
            },
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = challenge.description,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            Text(
                text="End date: ${challenge.endDate ?: '∞'}",
                fontSize = 14.sp,
            )
        }
    }
}



@Composable
fun PrimaryCard(modifier: Modifier = Modifier, numero: Int, text : String) {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(), // Sombra
        modifier = modifier.padding(10.dp)
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = text,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = numero.toString(),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 50.sp
            )
        }
    }
}