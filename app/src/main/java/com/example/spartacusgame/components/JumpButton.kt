package com.example.spartacusgame.components;

import androidx.compose.foundation.Canvas;
import androidx.compose.foundation.gestures.detectTapGestures;
import androidx.compose.foundation.layout.Box;
import androidx.compose.foundation.layout.size;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.geometry.Offset;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.Path;
import androidx.compose.ui.input.pointer.pointerInput;
import androidx.compose.ui.unit.dp;

/**
 * Componente de botón de salto que permite detectar toques y ejecutar una acción de salto.
 * El botón está representado visualmente por un triángulo blanco.
 * 
 * @param modifier Modificador de Compose que se aplica al contenedor del botón.
 * @param onJump Función de callback que se ejecuta cuando se detecta un toque en el botón.
 * 
 * @see Box
 * @see Canvas
 * @see detectTapGestures
 * @see Path
 */
@Composable
fun JumpButton(modifier: Modifier = Modifier, onJump: () -> Unit) {
    Box(
        modifier = modifier
            .size(100.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onJump() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(40.dp)) {
            val path = Path().apply {
                moveTo(size.width / 2, 0f);
                lineTo(0f, size.height);
                lineTo(size.width, size.height);
                close();
            }
            drawPath(path, color = Color.White);
        }
    }
}
