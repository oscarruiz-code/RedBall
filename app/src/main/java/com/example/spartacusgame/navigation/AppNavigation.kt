package com.example.spartacusgame.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spartacusgame.GameScreen
import com.example.spartacusgame.MainScreen
import com.example.spartacusgame.SplashScreen
import com.example.spartacusgame.SharedViewModel

@Composable
fun AppNavigation(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController)
        }
        composable("main_screen") {
            MainScreen(navController, sharedViewModel)
        }
        composable("game_screen") {
            GameScreen(sharedViewModel)
        }
    }
}
