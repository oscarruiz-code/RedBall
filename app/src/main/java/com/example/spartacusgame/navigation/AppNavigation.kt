package com.example.spartacusgame.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spartacusgame.screens.GameScreen
import com.example.spartacusgame.screens.MainScreen
import com.example.spartacusgame.screens.SplashScreen
import com.example.spartacusgame.viewmodels.SharedViewModel
import com.example.spartacusgame.viewmodels.GameViewModel

@Composable
fun AppNavigation(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController)
        }
        composable("main_screen") {
            MainScreen(navController, sharedViewModel)
        }
        composable("game_screen") {
            GameScreen(gameViewModel)
        }
    }
}
