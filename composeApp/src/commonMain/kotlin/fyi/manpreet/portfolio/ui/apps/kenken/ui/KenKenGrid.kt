package fyi.manpreet.portfolio.ui.apps.kenken.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
fun KenKenGrid(
    modifier: Modifier = Modifier,
    gridSize: Int,
    horizontalLines: List<KenKenGridLine>,
    verticalLines: List<KenKenGridLine>,
    selectedLineIds: Set<String>,
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
                val cellWidthSize = it.width / (gridSize - 1)
                val cellHeightSize = it.height / (gridSize - 1)
                onCellSizePixelsChange(KenKenGridIntent.UpdateCellSize(Offset(cellWidthSize.toFloat(), cellHeightSize.toFloat())))
            }
            .pointerInput(horizontalLines, verticalLines) {
                detectTapGestures { offset ->

                    horizontalLines.forEach { line ->
                        if (abs(offset.y - line.start.y) < 30 && offset.x in line.start.x..line.end.x) {
                            onLineClick(KenKenGridIntent.ToggleLine(line))
                            return@detectTapGestures
                        }
                    }

                    verticalLines.forEach { line ->
                        if (abs(offset.x - line.start.x) < 30 && offset.y in line.start.y..line.end.y) {
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
                color = if (line.id in selectedLineIds) Color.Blue else Color.Gray,
                start = line.start,
                end = line.end,
                strokeWidth = if (line.id in selectedLineIds) 4f else 2f
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
                color = if (line.id in selectedLineIds) Color.Blue else Color.Gray,
                start = line.start,
                end = line.end,
                strokeWidth = if (line.id in selectedLineIds) 4f else 2f
            )
        }
    }
}
