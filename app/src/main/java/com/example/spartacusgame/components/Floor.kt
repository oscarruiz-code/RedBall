package com.example.spartacusgame.components;

import androidx.compose.foundation.Canvas;
import androidx.compose.foundation.Image;
import androidx.compose.foundation.layout.Box;
import androidx.compose.foundation.layout.fillMaxSize;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.geometry.Offset;
import androidx.compose.ui.geometry.Rect;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.layout.onGloballyPositioned;
import androidx.compose.ui.layout.positionInRoot;
import androidx.compose.ui.res.painterResource;
import androidx.compose.ui.unit.toSize;

/**
 * Clase que gestiona la creación y visualización de un suelo en la interfaz de usuario.
 * Permite representar el suelo como una imagen o una línea transparente, y notifica cambios en su posición.
 * 
 * @param onPositionChange Función de callback que se ejecuta cuando la posición del suelo cambia.
 *                         Recibe un objeto {@link Rect} que representa la posición y tamaño del suelo.
 */
class FloorManager(
    private val onPositionChange: (Rect) -> Unit
) {
    /**
     * Método que crea y muestra un suelo en la interfaz de usuario.
     * El suelo puede ser representado como una imagen o una línea transparente, dependiendo de si se proporciona un recurso de imagen.
     * Además, notifica cambios en la posición del suelo mediante el callback {@code onPositionChange}.
     * 
     * Este método es anotado con {@link Composable}, lo que permite su uso en componentes de Compose.
     * 
     * @param modifier Modificador de Compose que se aplica al contenedor del suelo.
     * @param imageResId Identificador del recurso de imagen para representar el suelo. Si es nulo, se dibuja una línea transparente.
     * 
     * @see Box
     * @see Image
     * @see Canvas
     * @see Rect
     * @see onGloballyPositioned
     */
    @Composable
    fun CreateFloor(modifier: Modifier, imageResId: Int? = null) {
        Box(
            modifier = modifier
                .onGloballyPositioned { coordinates ->
                    onPositionChange(
                        Rect(
                            offset = coordinates.positionInRoot(),
                            size = coordinates.size.toSize()
                        )
                    )
                }
        ) {
            if (imageResId != null) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Suelo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawLine(
                        color = Color.Transparent,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 4f
                    )
                }
            }
        }
    }
}
