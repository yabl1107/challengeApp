package com.myself.mychallengueapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(
    val icon: ImageVector,
    val title: String,
    val route: String
) {
    object HomeView: Routes(Icons.Filled.DateRange, "Day", "day_view")
    object AddChallenge: Routes(Icons.Filled.AddCircle, "Add Challenge", "add_challenge_view")
    object ChallengeView: Routes(Icons.Filled.Face, "My Challengues", "challenge_view")
}