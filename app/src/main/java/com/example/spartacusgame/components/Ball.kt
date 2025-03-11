package com.example.spartacusgame.components;

import androidx.compose.foundation.Image;
import androidx.compose.foundation.layout.offset;
import androidx.compose.foundation.layout.size;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.geometry.Offset;
import androidx.compose.ui.res.painterResource;
import androidx.compose.ui.unit.IntOffset;
import androidx.compose.ui.unit.dp;
import com.example.spartacusgame.R;

/**
 * Clase que gestiona la creación y visualización de una bola en la interfaz de usuario.
 * La posición de la bola se define mediante un objeto {@link Offset}.
 * 
 * @author oscarruiz-code
 */
class BallManager(
    private val position: Offset
) {
    /**
     * Método que crea y muestra una bola en la posición especificada.
     * Utiliza un recurso de imagen para representar la bola y la ajusta al tamaño y posición indicados.
     * 
     * Este método es anotado con {@link Composable}, lo que permite su uso en componentes de Compose.
     * 
     * @see Image
     * @see Modifier
     * @see Offset
     * @see IntOffset
     */
    @Composable
    fun CreateBall() {
        Image(
            painter = painterResource(id = R.drawable.bola1),
            contentDescription = "Ball",
            modifier = Modifier
                .size(50.dp)
                .offset { IntOffset(position.x.toInt(), position.y.toInt()) }
        )
    }
}
