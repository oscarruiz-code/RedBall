package com.example.spartacusgame.screens

import FloorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spartacusgame.R
import com.example.spartacusgame.components.*
import com.example.spartacusgame.utils.AudioManager
import com.example.spartacusgame.viewmodels.GameViewModel
import com.example.spartacusgame.viewmodels.SharedViewModel
import kotlinx.coroutines.delay
/**
 * Clase que nos maneja el estilo visual de nuestra pantalla juego y se encarga de hacer visible nuestros componentes
 *
 * @author oscarruiz-code
 *
 */
@Composable
fun GameScreen(viewModel: GameViewModel, navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val audioManager = remember { AudioManager(context) }

    //Inicio el Audio apartir de mis utilidades
    LaunchedEffect(Unit) {
        audioManager.playLoopingAudio(R.raw.juego)
    }

    DisposableEffect(Unit) {
        onDispose {
            audioManager.stopAudio()
        }
    }

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val screenWidthPx = with(LocalDensity.current) { screenWidthDp.toPx() }
    val screenHeightPx = with(LocalDensity.current) { screenHeightDp.toPx() }

    //Inicio y recojo las dimensiones de mi pantalla para ajustar todas las posiciones y todo
    LaunchedEffect(screenWidthPx, screenHeightPx) {
        viewModel.setScreenDimensions(screenWidthPx, screenHeightPx)
    }

    val ballManager = BallManager(viewModel.ballPosition)
    val floorManager = FloorManager(onPositionChange = { viewModel.floorBounds = it })
    val floorSecondManager = FloorManager(onPositionChange = { viewModel.floorSecondBounds = it })
    val enemyImage = ImageBitmap.imageResource(id = R.drawable.enemigo)


    //Es el encargado de tener todos los componentes actualizados y que vaya toda la carga fluida
    LaunchedEffect(Unit) {
        viewModel.startGameLoop()
        viewModel.startSquareGeneration()
        viewModel.startCollisionDetection()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF54C7F2))
    ) {

        //Imagen de fonde de nuestra pestana Juego
        Image(
            painter = painterResource(id = R.drawable.fondojuego),
            contentDescription = "Fondo",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        //Suelo principal abajo del todo
        floorManager.CreateFloor(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.BottomStart)
                .offset(y = -40.dp)
        )

        //Suelo secundario con el que colisionann balas y enemigos
        floorSecondManager.CreateFloor(
            modifier = Modifier
                .width(viewModel.floorSecondWidth.dp)
                .height(30.dp)
                .offset {
                    IntOffset(
                        viewModel.floorSecondPosition.x.toInt(),
                        viewModel.floorSecondPosition.y.toInt()
                    )
                },
            imageResId = R.drawable.suelo
        )

        //Creacion de nuestro componentes peronaje, boton y demas apartir de nuestro componentes
        ballManager.CreateBall()

        Joystick(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp),
            onMove = { viewModel.onJoystickMove(it) }
        )

        JumpButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(y = -60.dp)
                .padding(start = 32.dp),
            onJump = { viewModel.onShoot() }
        )

        //Encargado de generar ccuando existe una acion tanto balas como enemigos en la pantalla
        Box(modifier = Modifier.fillMaxSize()) {
            viewModel.shots.forEach { shot ->
                Image(
                    painter = painterResource(id = R.drawable.bala),
                    contentDescription = "Bala",
                    modifier = Modifier
                        .offset { IntOffset(shot.left.toInt(), shot.top.toInt()) }
                        .size(20.dp)
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                viewModel.fallingSquares.forEach { square ->
                    drawImage(
                        image = enemyImage,
                        dstOffset = IntOffset(square.left.toInt(), square.top.toInt()),
                        dstSize = IntSize(50.dp.roundToPx(), 50.dp.roundToPx())
                    )
                }
            }


            if (viewModel.gameOverMessageVisible) {
                Text(
                    text = "Game Over",
                    color = Color.Red,
                    fontSize = 48.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Text(
                text = "Score: ${viewModel.score}",
                color = Color.Black,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
            )
        }

        //Evento que sucede una vez cuando la colision entre enemigo y personaje ocurre
        LaunchedEffect(viewModel.isGameOver) {
            if (viewModel.isGameOver) {
                val playerName = sharedViewModel.playerName
                viewModel.addScore(playerName, viewModel.score)
                delay(2000)
                navController.navigate("main_screen") {
                    popUpTo("game_screen") { inclusive = true }
                }
                viewModel.resetGame()
            }
        }
    }
}
