package com.myself.mychallengueapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.myself.mychallengueapp.views.ChallengeDetailView
import com.myself.mychallengueapp.views.DayView
import com.myself.mychallengueapp.views.addChallenge
import com.myself.mychallengueapp.views.challengeView

@Composable
fun NavManager(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Routes.AddChallenge.route) {
        composable(Routes.HomeView.route) {
            DayView()
        }
        composable(Routes.AddChallenge.route) {
            addChallenge(navHostController)
        }
        composable(Routes.ChallengeView.route) {
            challengeView(navHostController)
        }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            ChallengeDetailView(navHostController,id)
        }
    }
}