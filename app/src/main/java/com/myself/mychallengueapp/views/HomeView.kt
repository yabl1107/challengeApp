package com.myself.mychallengueapp.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.myself.mychallengueapp.components.BottomNav
import com.myself.mychallengueapp.navigation.NavManager
import com.myself.mychallengueapp.navigation.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeView() {
    val navController = rememberNavController()
    val navigationRoutes = listOf(
        Routes.HomeView,
        Routes.AddChallenge,
        Routes.ChallengeView
    )
    Scaffold(
        bottomBar = { BottomNav(navController, navigationRoutes) }
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavManager(navController)
        }
    }
}