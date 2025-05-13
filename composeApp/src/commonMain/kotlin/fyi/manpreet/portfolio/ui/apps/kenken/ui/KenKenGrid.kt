package fyi.manpreet.portfolio.ui.apps.kenken.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
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
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenShape
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KenKenGrid(
    modifier: Modifier = Modifier,
    gridSize: Int,
    horizontalLines: List<KenKenGridLine>,
    verticalLines: List<KenKenGridLine>,
    selectedLineIds: Set<String>,
    shapes: List<KenKenShape>,
    onLineClick: (KenKenGridIntent.ToggleLine) -> Unit,
    onCellSizePixelsChange: (KenKenGridIntent.UpdateCellSize) -> Unit,
    onShapeOperatorClick: (KenKenGridIntent.ToggleShapeOperator) -> Unit,
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
            .pointerInput(horizontalLines, verticalLines, shapes) {
                detectTapGestures { offset ->

                    horizontalLines.forEach { line ->
                        if (abs(offset.y - line.start.y) < 20 && offset.x in line.start.x..line.end.x) {
                            onLineClick(KenKenGridIntent.ToggleLine(line))
                            return@detectTapGestures
                        }
                    }

                    verticalLines.forEach { line ->
                        if (abs(offset.x - line.start.x) < 20 && offset.y in line.start.y..line.end.y) {
                            onLineClick(KenKenGridIntent.ToggleLine(line))
                            return@detectTapGestures
                        }
                    }

                    shapes.forEach { shape ->
                        if (abs(offset.x - shape.operator.topLeft.x) < 20 && abs(offset.y - shape.operator.topLeft.y) < 20) {
                            onShapeOperatorClick(KenKenGridIntent.ToggleShapeOperator(shape))
                        }
                    }
                }
            }

    ) {
        canvasSize.value = size.width

        // Draw grid lines
        horizontalLines.forEachIndexed { index, line ->
            drawLine(
                color = if (line.id in selectedLineIds) Color.Black else Color.LightGray,
                start = line.start,
                end = line.end,
                strokeWidth = if (line.id in selectedLineIds) 4f else 2f
            )
        }

        verticalLines.forEach { line ->
            drawLine(
                color = if (line.id in selectedLineIds) Color.Black else Color.LightGray,
                start = line.start,
                end = line.end,
                strokeWidth = if (line.id in selectedLineIds) 4f else 2f
            )
        }

        shapes.forEach { shape ->

            drawText(
                textMeasurer = textMeasurer,
                text = shape.operator.operation.symbol,
                topLeft = shape.operator.topLeft,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                )
            )
        }
    }
}
