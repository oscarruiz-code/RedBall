package com.example.spartacusgame.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.spartacusgame.R

class BallManager(
    private val position: Offset
) {
    @Composable
    fun CreateBall() {
        Image(
            painter = painterResource(id = R.drawable.bolajuego), // Asegúrate de que este ID sea correcto
            contentDescription = "Ball",
            modifier = Modifier
                .size(50.dp)
                .offset { IntOffset(position.x.toInt(), position.y.toInt()) }
        )
    }
}
