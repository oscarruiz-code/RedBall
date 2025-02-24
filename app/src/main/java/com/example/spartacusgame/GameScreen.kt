package com.example.spartacusgame

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

@Composable
fun GameScreen(sharedViewModel: SharedViewModel) {
    val backgroundImage = if (sharedViewModel.background == "first") {
        R.drawable.fondopantalla
    } else {
        R.drawable.fondopantalla1
    }

    var circlePosition by remember { mutableStateOf(Offset(100f, 200f)) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        circlePosition = circlePosition.copy(x = circlePosition.x + dragAmount)
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.Red,
                    radius = 50f,
                    center = circlePosition
                )
            }
        }

        Button(
            onClick = {
                sharedViewModel.changeBackground("second")
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text("Cambiar fondo")
        }
    }
}
