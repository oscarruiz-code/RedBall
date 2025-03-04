package com.example.spartacusgame.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun JumpButton(modifier: Modifier = Modifier, onJump: () -> Unit) {
    Box(
        modifier = modifier
            .size(100.dp)
            .background(Color.LightGray, shape = CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onJump() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            drawCircle(color = Color.LightGray, radius = size.minDimension / 2)
        }
        Canvas(modifier = Modifier.size(40.dp)) {
            val path = Path().apply {
                moveTo(size.width / 2, 0f)
                lineTo(0f, size.height)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(path, color = Color.DarkGray)
        }
    }
}
