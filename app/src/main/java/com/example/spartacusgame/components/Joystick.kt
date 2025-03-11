package com.example.spartacusgame.components;

import androidx.compose.foundation.Canvas;
import androidx.compose.foundation.gestures.detectDragGestures;
import androidx.compose.foundation.layout.Box;
import androidx.compose.foundation.layout.size;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.geometry.Offset;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.input.pointer.pointerInput;
import androidx.compose.ui.unit.dp;

/**
 * Componente de joystick que permite detectar movimientos táctiles y notificar la dirección del movimiento.
 * El joystick consta de un círculo blanco que se puede arrastrar dentro de un área limitada.
 * 
 * @param modifier Modificador de Compose que se aplica al contenedor del joystick.
 * @param onMove Función de callback que se ejecuta cuando el joystick se mueve.
 *               Recibe un objeto {@link Offset} que representa la dirección normalizada del movimiento.
 * 
 * @see Box
 * @see Canvas
 * @see detectDragGestures
 * @see Offset
 */
@Composable
fun Joystick(modifier: Modifier = Modifier, onMove: (Offset) -> Unit) {
    var knobPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .size(120.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        knobPosition = Offset(
                            x = (knobPosition.x + dragAmount.x).coerceIn(-40f, 40f),
                            y = (knobPosition.y + dragAmount.y).coerceIn(-40f, 40f)
                        )
                        onMove(knobPosition / 40f)
                    },
                    onDragEnd = {
                        knobPosition = Offset.Zero
                        onMove(Offset.Zero)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(40.dp)) {
            drawCircle(color = Color.White, radius = size.minDimension / 2) // Círculo blanco
        }
    }
}
