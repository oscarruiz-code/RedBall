package com.example.spartacusgame.navigation;

import androidx.compose.runtime.Composable;
import androidx.lifecycle.viewmodel.compose.viewModel;
import androidx.navigation.compose.NavHost;
import androidx.navigation.compose.composable;
import androidx.navigation.compose.rememberNavController;
import com.example.spartacusgame.screens.*;
import com.example.spartacusgame.viewmodels.GameViewModel;
import com.example.spartacusgame.viewmodels.SharedViewModel;

/**
 * Componente de navegación principal de la aplicación.
 * Define las rutas de navegación y las pantallas asociadas a cada ruta.
 * Utiliza un controlador de navegación para gestionar la transición entre pantallas.
 */
@Composable
fun AppNavigation(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController();
    val gameViewModel: GameViewModel = viewModel();

    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController);
        }
        composable("main_screen") {
            MainScreen(navController, sharedViewModel);
        }
        composable("game_screen") {
            GameScreen(gameViewModel, navController, sharedViewModel);
        }
        composable("score_screen") {
            ScoreScreen(navController, gameViewModel.scores);
        }
    }
}
