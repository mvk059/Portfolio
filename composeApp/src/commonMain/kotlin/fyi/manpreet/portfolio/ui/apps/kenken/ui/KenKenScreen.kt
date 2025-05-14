package fyi.manpreet.portfolio.ui.apps.kenken.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridIntent
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridSize
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridState
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGroupSize
import fyi.manpreet.portfolio.ui.apps.kenken.viewmodel.KenKenViewModel
import fyi.manpreet.portfolio.window.calculateWindowWidthSize

@Composable
fun KenKenScreen(
    modifier: Modifier = Modifier,
    viewModel: KenKenViewModel = remember { KenKenViewModel() },
) {
    val gridState by viewModel.gridState.collectAsStateWithLifecycle()
    val selectedShape = gridState.shapes.firstOrNull { it.isSelected }
    val windowWidth = calculateWindowWidthSize()
    val aspectRatio = when (windowWidth) {
        WindowWidthSizeClass.Compact -> 0.8f
        WindowWidthSizeClass.Medium -> 0.8f
        WindowWidthSizeClass.Expanded -> 0.6f
        else -> 1f
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Grid size selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("Grid Size: ${gridState.gridSize.value}")

            Slider(
                value = gridState.gridSize.value.toFloat(),
                onValueChange = {
                    viewModel.processIntent(KenKenGridIntent.UpdateGridSize(KenKenGridSize(it.toInt())))
                },
                valueRange = KenKenGridState.minGridSize.value.toFloat()..KenKenGridState.maxGridSize.value.toFloat(),
                steps = 3,
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("Group Size: ${gridState.groupSize.value}")

            Slider(
                value = gridState.groupSize.value.toFloat(),
                onValueChange = {
                    viewModel.processIntent(KenKenGridIntent.UpdateGroupSize(KenKenGroupSize(it.toInt())))
                },
                valueRange = KenKenGridState.minGridSize.value.toFloat()..KenKenGridState.maxGridSize.value.toFloat(), // TODO Update
                steps = 3,
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
            )
        }

        // Game grid
        KenKenGrid(
            modifier = Modifier.fillMaxSize(aspectRatio),
            gridSize = gridState.gridSize.value,
            horizontalLines = gridState.horizontalLines,
            verticalLines = gridState.verticalLines,
            selectedLineIds = gridState.selectedLineIds,
            shapes = gridState.shapes,
            onLineClick = viewModel::processIntent,
            onCellSizePixelsChange = viewModel::processIntent,
            onShapeSelection = viewModel::processIntent,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                onClick = {}
            ) {
                Text("Generate Random Puzzle")
            }

            Button(
                onClick = {}
            ) {
                Text("Solve Puzzle")
            }
        }

        if (selectedShape != null) {
            KenKenInputDialog(
                shape = selectedShape,
                onShapeUpdate = viewModel::processIntent,
            )
        }
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