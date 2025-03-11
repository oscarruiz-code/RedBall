package com.example.spartacusgame.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spartacusgame.R
import com.example.spartacusgame.utils.AudioManager

data class Score(val playerName: String, val score: Int)

/**
 * Clase que nos maneja el estilo visual de nuestra pantalla puntuacion y se encarga de hacer visible nuestros componentes
 *
 * @author oscarruiz-code
 *
 */
@Composable
fun ScoreScreen(navController: NavController, scores: List<Score>) {
    val context = LocalContext.current
    val audioManager = remember { AudioManager(context) }

    //Inicio el Audio apartir de mis utilidades
    LaunchedEffect(Unit) {
        audioManager.playLoopingAudio(R.raw.puntuacion)
    }

    DisposableEffect(Unit) {
        onDispose {
            audioManager.stopAudio()
        }
    }

    //Representa la puntuacion ligada a nuestro usuario que se ha registrado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Historial de Puntuaciones", fontSize = 24.sp)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(scores) { score ->
                    Text("${score.playerName}: ${score.score} puntos", fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("main_screen") }) {
                Text("Volver al Menú Principal")
            }
        }
    }
}