package com.example.spartacusgame.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.toSize

class FloorManager(
    private val onPositionChange: (Rect) -> Unit
) {
    @Composable
    fun CreateFloor(modifier: Modifier) {
        Box(
            modifier = modifier.onGloballyPositioned { coordinates ->
                onPositionChange(Rect(
                    offset = coordinates.positionInRoot(),
                    size = coordinates.size.toSize()
                ))
            }
        )
    }
}
