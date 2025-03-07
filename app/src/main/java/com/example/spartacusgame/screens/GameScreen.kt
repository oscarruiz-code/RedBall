package com.example.spartacusgame.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spartacusgame.components.BallManager
import com.example.spartacusgame.components.FloorManager
import com.example.spartacusgame.components.Joystick
import com.example.spartacusgame.components.JumpButton
import com.example.spartacusgame.viewmodels.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val ballManager = BallManager(viewModel.ballPosition)  // Referencia a la posición de la bola desde el ViewModel
    val floorManager = FloorManager(onPositionChange = { viewModel.floorBounds = it })
    val floorSecondManager = FloorManager(onPositionChange = { viewModel.floorSecondBounds = it })

    LaunchedEffect(Unit) {
        viewModel.startGameLoop()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Suelo verde
        floorManager.CreateFloor(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .align(Alignment.BottomStart)
                .background(Color.Green)
        )

        // Suelo rojo
        floorSecondManager.CreateFloor(
            modifier = Modifier
                .width(80.dp)
                .height(10.dp)
                .align(Alignment.Center)
                .offset { IntOffset(viewModel.floorSecondPosition.x.toInt(), 180) }
                .background(Color.Red)
        )


        ballManager.CreateBall()

        // Joystick
        Joystick(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp),
            onMove = { viewModel.onJoystickMove(it) }
        )

        // Botón de Salto
        JumpButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(y = (-100).dp)
                .padding(start = 32.dp),
            onJump = { viewModel.onJump() }
        )
    }
}
