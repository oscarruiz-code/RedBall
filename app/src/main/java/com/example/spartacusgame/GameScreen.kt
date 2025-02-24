package com.example.spartacusgame

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.delay
import androidx.compose.foundation.gestures.detectTapGestures

@Composable
fun GameScreen(sharedViewModel: SharedViewModel) {

    var ballPosition by remember { mutableStateOf(Offset(200f, 500f)) }
    var isFalling by remember { mutableStateOf(true) }
    var isJumping by remember { mutableStateOf(false) }

    var ballBounds by remember { mutableStateOf(Rect.Zero) }
    var floorBounds by remember { mutableStateOf(Rect.Zero) }

    var ballVelocity by remember { mutableStateOf(0f) } // Velocidad lateral
    var jumpVelocity by remember { mutableStateOf(0f) } // Velocidad de salto
    val gravity = 1.5f  // Gravedad

    LaunchedEffect(Unit) {
        while (true) {
            delay(16) // Simula 60 FPS

            // Movimiento lateral con joystick
            ballPosition = ballPosition.copy(x = ballPosition.x + ballVelocity)

            // Aplicar gravedad si está en el aire
            if (isFalling) {
                ballPosition = ballPosition.copy(y = ballPosition.y + jumpVelocity)
                jumpVelocity += gravity // Aumentar la velocidad de caída
            }

            // Si la bola toca el suelo, detener la caída y corregir posición
            if (ballBounds.overlaps(floorBounds)) {
                isFalling = false
                isJumping = false
                jumpVelocity = 0f
                ballPosition = ballPosition.copy(y = floorBounds.top - ballBounds.height)
            } else {
                isFalling = true
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Suelo (pegado a la izquierda)
        Box(
            modifier = Modifier
                .width(400.dp)
                .height(50.dp)
                .align(Alignment.BottomStart)
                .background(Color.Green)
                .onGloballyPositioned { coordinates ->
                    floorBounds = Rect(
                        offset = coordinates.positionInRoot(),
                        size = coordinates.size.toSize()
                    )
                }
        )

        // Círculo rojo (Personaje)
        Canvas(
            modifier = Modifier
                .size(50.dp)
                .offset { IntOffset(ballPosition.x.toInt(), ballPosition.y.toInt()) }
                .onGloballyPositioned { coordinates ->
                    ballBounds = Rect(
                        offset = coordinates.positionInRoot(),
                        size = coordinates.size.toSize()
                    )
                }
        ) {
            drawCircle(
                color = Color.Red,
                radius = size.minDimension / 2,
                center = Offset(size.width / 2, size.height / 2)
            )
        }

        // 🎮 Joystick (Derecha)
        Joystick(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
        ) { direction ->
            ballVelocity = direction.x * 5f // Ajusta la velocidad
        }

        // ⬆️ Botón de Salto (Movido más arriba)
        JumpButton(
            modifier = Modifier
                .align(Alignment.CenterStart) // Mueve el botón más arriba
                .offset(y = (-100).dp) // Ajusta la altura
                .padding(start = 32.dp)
        ) {
            if (!isJumping) {
                isJumping = true
                jumpVelocity = -20f  // Velocidad inicial de salto
            }
        }
    }
}

@Composable
fun Joystick(modifier: Modifier = Modifier, onMove: (Offset) -> Unit) {
    var knobPosition by remember { mutableStateOf(Offset.Zero) } // Posición del núcleo gris oscuro

    Box(
        modifier = modifier
            .size(120.dp)
            .background(Color.LightGray, shape = CircleShape)  // Base gris claro
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        knobPosition = Offset(
                            x = (knobPosition.x + dragAmount.x).coerceIn(-40f, 40f),
                            y = (knobPosition.y + dragAmount.y).coerceIn(-40f, 40f)
                        )
                        onMove(knobPosition / 40f) // Normaliza el movimiento
                    },
                    onDragEnd = {
                        knobPosition = Offset.Zero  // Al soltar, vuelve al centro
                        onMove(Offset.Zero)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Fondo gris claro
        Canvas(modifier = Modifier.size(120.dp)) {
            drawCircle(color = Color.LightGray, radius = size.minDimension / 2)
        }
        // Núcleo gris oscuro (se mueve con el dedo)
        Canvas(
            modifier = Modifier
                .size(40.dp)
                .offset { IntOffset(knobPosition.x.toInt(), knobPosition.y.toInt()) }
        ) {
            drawCircle(color = Color.DarkGray, radius = size.minDimension / 2)
        }
    }
}

@Composable
fun JumpButton(modifier: Modifier = Modifier, onJump: () -> Unit) {
    Box(
        modifier = modifier
            .size(100.dp)
            .background(Color.LightGray, shape = CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onJump() } // Ejecuta el salto al tocar el botón
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Fondo del botón (gris claro)
        Canvas(modifier = Modifier.size(100.dp)) {
            drawCircle(color = Color.LightGray, radius = size.minDimension / 2)
        }

        // Flecha (Núcleo gris oscuro con forma de flecha)
        Canvas(modifier = Modifier.size(40.dp)) {
            val path = Path().apply {
                moveTo(size.width / 2, 0f)  // Punta de la flecha
                lineTo(0f, size.height)     // Base izquierda
                lineTo(size.width, size.height)  // Base derecha
                close()
            }
            drawPath(path, color = Color.DarkGray)
        }
    }
}
