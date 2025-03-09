package com.example.spartacusgame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spartacusgame.R
import com.example.spartacusgame.components.*
import com.example.spartacusgame.viewmodels.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun GameScreen(viewModel: GameViewModel, navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

    // Convertir dp a píxeles
    val screenWidthPx = with(LocalDensity.current) { screenWidthDp.toPx() }
    val screenHeightPx = with(LocalDensity.current) { screenHeightDp.toPx() }

    // Pasa las dimensiones de la pantalla al ViewModel
    LaunchedEffect(screenWidthPx, screenHeightPx) {
        viewModel.setScreenDimensions(screenWidthPx, screenHeightPx)
    }

    val ballManager = BallManager(viewModel.ballPosition)
    val floorManager = FloorManager(onPositionChange = { viewModel.floorBounds = it })
    val floorSecondManager = FloorManager(onPositionChange = { viewModel.floorSecondBounds = it })

    LaunchedEffect(Unit) {
        viewModel.startGameLoop()
        viewModel.startSquareGeneration()
        viewModel.startCollisionDetection()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF19E0EE))
    ) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondojuego),
            contentDescription = "Fondo",
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 30.dp),
            contentScale = ContentScale.Crop
        )

        // Suelo verde
        floorManager.CreateFloor(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .align(Alignment.BottomStart)
                .offset(y = -20.dp)
        )

        // Suelo rojo
        floorSecondManager.CreateFloor(
            modifier = Modifier
                .width(viewModel.floorSecondWidth.dp) // Ancho del suelo rojo
                .height(10.dp) // Altura del suelo rojo
                .offset {
                    IntOffset(
                        viewModel.floorSecondPosition.x.toInt(),
                        viewModel.floorSecondPosition.y.toInt()
                    )
                }
                .background(Color.Red)
        )

        // Bola
        ballManager.CreateBall()

        // Joystick
        Joystick(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp),
            onMove = { viewModel.onJoystickMove(it) }
        )

        // Botón de disparo
        JumpButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(y = 20.dp)
                .padding(start = 32.dp),
            onJump = { viewModel.onShoot() }
        )

        // Dibuja los disparos y cuadrados
        Box(modifier = Modifier.fillMaxSize()) {
            viewModel.shots.forEach { shot ->
                Box(
                    modifier = Modifier
                        .offset { IntOffset(shot.left.toInt(), shot.top.toInt()) }
                        .size(20.dp)
                        .background(Color.Blue, CircleShape)
                )
            }

            viewModel.fallingSquares.forEach { square ->
                Box(
                    modifier = Modifier
                        .offset { IntOffset(square.left.toInt(), square.top.toInt()) }
                        .size(50.dp)
                        .background(Color.Red)
                )
            }
        }

        // Mensaje de Game Over
        if (viewModel.gameOverMessageVisible) {
            Text(
                text = "Game Over",
                color = Color.Red,
                fontSize = 48.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Puntaje
        Text(
            text = "Score: ${viewModel.score}",
            color = Color.Black,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        )
    }

    // Navega al MainScreen cuando el juego termina
    LaunchedEffect(viewModel.isGameOver) {
        if (viewModel.isGameOver) {
            delay(2000)
            navController.navigate("main_screen") {
                popUpTo("game_screen") { inclusive = true }
            }
            viewModel.resetGame()
        }
    }
}