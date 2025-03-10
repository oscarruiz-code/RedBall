import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.toSize

class FloorManager(
    private val onPositionChange: (Rect) -> Unit
) {
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