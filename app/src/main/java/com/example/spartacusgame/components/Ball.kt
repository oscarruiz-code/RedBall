package com.example.spartacusgame.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

class BallManager(
    private val position: Offset
) {
    @Composable
    fun CreateBall() {
        Canvas(
            modifier = Modifier
                .size(50.dp)
                .offset {
                    IntOffset(position.x.toInt(), position.y.toInt())
                }
        ) {
            drawCircle(
                color = Color.Red,
                radius = size.minDimension / 2,
                center = Offset(size.width / 2, size.height / 2)
            )
        }
    }
}
