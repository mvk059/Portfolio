package fyi.manpreet.portfolio.ui.apps.kenken.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridIntent
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridLine
import kotlin.math.abs

@Composable
fun KenKenGrid1(
    modifier: Modifier = Modifier,
    gridSize: Int,
    cellSize: Float,
    horizontalLines: List<KenKenGridLine>,
    verticalLines: List<KenKenGridLine>,
    onLineClick: (KenKenGridIntent.ToggleLine) -> Unit,
    onCellSizePixelsChange: (KenKenGridIntent.UpdateCellSize) -> Unit,
) {

    val textMeasurer = rememberTextMeasurer()
    val canvasSize = remember { mutableStateOf(0f) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
//            .aspectRatio(1f)
            .onSizeChanged {
//                val cellSizePixels = it.width / gridSize
//                onCellSizePixelsChange(KenKenGridIntent.UpdateCellSize(cellSizePixels.toFloat()))
            }
            .pointerInput(true) {
                detectTapGestures { offset ->
                    val clickX = offset.x// * cellSize
                    val clickY = offset.y// * cellSize

                    horizontalLines.forEach { line ->
                        if (abs(clickY - line.start.y) < 10 && offset.x in line.start.x..line.end.x) {
                            onLineClick(KenKenGridIntent.ToggleLine(line))
                            return@detectTapGestures
                        }
                    }

                    verticalLines.forEach { line ->
                        if (abs(clickX - line.start.x) < 10 && offset.y in line.start.y..line.end.y) {
                            onLineClick(KenKenGridIntent.ToggleLine(line))
                            return@detectTapGestures
                        }
                    }
                }
            }

    ) {
        canvasSize.value = size.width

        // Draw grid lines
        horizontalLines.forEachIndexed { index, line ->
            drawLine(
                color = if (line.isSelected) Color.Blue else Color.Gray,
                start = line.start,
                end = line.end,
                strokeWidth = if (line.isSelected) 4f else 2f
            )

            if (index < horizontalLines.size - 3) { // - grid size - 1
                drawText(
                    textMeasurer = textMeasurer,
                    text = "1",
                    topLeft = line.start,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                    )
                )
            }
        }

        verticalLines.forEach { line ->
            drawLine(
                color = if (line.isSelected) Color.Blue else Color.Gray,
                start = line.start,
                end = line.end,
                strokeWidth = if (line.isSelected) 4f else 2f
            )
        }
    }
}