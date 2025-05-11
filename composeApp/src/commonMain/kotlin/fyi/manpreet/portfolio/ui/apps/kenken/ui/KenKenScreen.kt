package fyi.manpreet.portfolio.ui.apps.kenken.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fyi.manpreet.portfolio.ui.apps.kenken.viewmodel.KenKenViewModel

@Composable
fun KenKenScreen(
    modifier: Modifier = Modifier,
    viewModel: KenKenViewModel = remember { KenKenViewModel() },
) {
    val gridState by viewModel.gridState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

//        // Grid size selector
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Text("Grid Size: ${gridState.gridSize.value}")
//
//            Slider(
//                value = gridState.gridSize.value.toFloat(),
//                onValueChange = {
//                    viewModel.processIntent(KenKenGridIntent.SetGridSize(KenKenGridSize(it.toInt())))
//                },
//                valueRange = KenKenGridState.minGridSize.value.toFloat()..KenKenGridState.maxGridSize.value.toFloat(),
//                steps = 3,
//                modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
//            )
//        }
//
//        // Game grid
//        KenKenGrid(
//            modifier = Modifier.fillMaxWidth(0.8f),
//            gridSize = state.gridSize,
//            cellSize = 50f,
//            horizontalLines = state.horizontalLines,
//            verticalLines = state.verticalLines,
//            selectedLines = state.selectedLines,
//            shapes = state.shapes,
//            onLineClick = { line ->
//                viewModel.processIntent(KenKenIntent.ToggleLine(line))
//            },
//            onCellSizePixelsChange = viewModel::processIntent
//        )

        KenKenGrid1(
            modifier = Modifier.fillMaxSize(),
            gridSize = gridState.gridSize.value,
            cellSize = gridState.cellSize,
            horizontalLines = gridState.horizontalLines,
            verticalLines = gridState.verticalLines,
            onLineClick = viewModel::processIntent,
            onCellSizePixelsChange = viewModel::processIntent
        )
//
//        // Controls
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(
//                onClick = {
//                    viewModel.processIntent(KenKenIntent.Reset)
//                }
//            ) {
//                Text("Reset")
//            }
//
//            Button(
//                onClick = {
//                    // TODO: Implement shape creation from selected lines
//                    val cells = listOf(Pair(0, 0), Pair(0, 1)) // Example cells
//                    viewModel.processIntent(KenKenIntent.CreateShape(cells))
//                }
//            ) {
//                Text("Create Shape")
//            }
//        }
    }
} 