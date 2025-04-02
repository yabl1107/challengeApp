package com.myself.mychallengueapp.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.myself.mychallengueapp.navigation.Routes

@Composable
fun BottomNav(navHostController: NavHostController, routes: List<Routes>) {
    BottomAppBar (){
        NavigationBar(
        ){
            val currentRoute = CurrentRoute(navHostController)
            routes.forEach{ item ->
                NavigationBarItem(selected = currentRoute == item.route,
                    onClick = { navHostController.navigate(item.route)},
                    icon = {
                        Icon(imageVector = item.icon,
                            contentDescription = item.title)
                    },
                    label = {
                        Text(text = item.title)
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}


@Composable
fun CurrentRoute(navHostController: NavHostController) : String?{
    val current by navHostController.currentBackStackEntryAsState()
    return current?.destination?.route
}