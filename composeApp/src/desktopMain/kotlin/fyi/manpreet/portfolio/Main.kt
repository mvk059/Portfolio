package fyi.manpreet.portfolio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import fyi.manpreet.portfolio.ui.apps.kenken.ui.KenKenScreen
import fyi.manpreet.portfolio.ui.apps.kenken.viewmodel.KenKenViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() {
    singleWindowApplication {

        KenKenScreen(modifier = Modifier.fillMaxSize())
        return@singleWindowApplication
        /*
//        GridDemo()

        val viewModel: KenKenViewModel = remember { KenKenViewModel() }
        val state by viewModel.gridState.collectAsState()

//        val horizontalLines = remember { mutableStateListOf<Pair< Offset, Offset>>() }
//        val verticalLines = remember { mutableStateListOf<Pair< Offset, Offset>>() }

//        val cellSize = 50f
        val rows = 4 + 1
        val cols = 4 + 1

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),//.background(Color.Red),
        ) {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
//                    .background(Color.Blue)
                    .pointerInput(true) {
                        detectTapGestures { offset ->
                            val clickX = offset.x
                            val clickY = offset.y

                            state.horizontalLines.forEach { line ->
                                val (startX, startY) = line.start
                                val (endX, endY) = line.end
                                val lineStartX = startX * state.cellWidthSize
                                val lineStartY = startY * state.cellWidthSize
                                val lineEndX = endX * state.cellWidthSize

                                if (abs(clickY - lineStartY) < 20 && clickX in lineStartX..lineEndX) {
//                                    viewModel.onLineClicked(line)
                                    return@detectTapGestures
                                }
                            }
                        }
                    },
            ) {


                /*
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, 0f),
                    end = Offset(width, 0f),
                    strokeWidth = 1f,
                )

                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, 0f),
                    end = Offset(0f, width),
                    strokeWidth = 1f,
                )

                drawLine(
                    color = Color.LightGray,
                    start = Offset(width, 0f),
                    end = Offset(width, width),
                    strokeWidth = 1f,
                )

                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, width),
                    end = Offset(width, width),
                    strokeWidth = 1f,
                )
                 */

                state.horizontalLines.forEach { line ->
                    drawLine(
                        color = if (line in state.selectedLines) Color.Blue else Color.Gray, //Color.LightGray,//(nextInt()),
                        start = line.start,
                        end = line.end,
                        strokeWidth = 5f,
                    )
                }

                state.verticalLines.forEach { line ->
                    drawLine(
                        color = if (line in state.selectedLines) Color.Blue else Color.Gray, //Color.LightGray,
                        start = line.start,
                        end = line.end,
                        strokeWidth = 5f,
                    )
                }

                /*
                repeat(rows) { row ->
                    // Draw horizontal lines
                    repeat(cols - 1) { col ->
                        val start = Offset(col * cellSize, row * cellSize)
                        val end = Offset((col * cellSize) + cellSize, row * cellSize)
                        drawLine(
                            color = Color.LightGray,//(nextInt()),
                            start = start,
                            end = end,
                            strokeWidth = 1f,
                        )
                        horizontalLines.add(start to end)
                    }

                    if (row < rows - 1) {
                        // Draw vertical lines
                        repeat(cols) { col ->
                            val start = Offset(col * cellSize, row * cellSize),
                            val end = Offset(col * cellSize, (row * cellSize) + cellSize),

                            drawLine(
                                color = Color.LightGray,//(nextInt()),
                                start = start,
                                end = end,
                                strokeWidth = 1f,
                            )
                            verticalLines.add(start to end)
                        }
                    }
                }
                 */
            }
        }


//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//        ) {
//
//            item {
//                Box(
//                    modifier = Modifier.background(Color.LightGray, )
//                )
//            }
//        }

         */
    }
}