package fyi.manpreet.portfolio.ui.apps.kenken.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridIntent
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridLine
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridSize
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class KenKenViewModel : ViewModel() {

    private val _gridState = MutableStateFlow(KenKenGridState())
    val gridState: StateFlow<KenKenGridState> = _gridState
        .onStart { initialiseGrid(null, null) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = KenKenGridState()
        )

    private fun initialiseGrid(gridSize: Int?, cellSize: Float?) {
        val gridSize = gridSize ?: _gridState.value.gridSize.value
        val cellSize = cellSize ?: _gridState.value.cellSize
        require(gridSize in KenKenGridState.minGridSize.value..KenKenGridState.maxGridSize.value) { "Grid size is not in range" }

        val horizontalLines = mutableListOf<KenKenGridLine>()
        val verticalLines = mutableListOf<KenKenGridLine>()
        repeat(gridSize) { row ->
            repeat(gridSize - 1) { col ->
                horizontalLines.add(
                    KenKenGridLine(
                        start = Offset(col * cellSize, row * cellSize),
                        end = Offset((col * cellSize) + cellSize, row * cellSize),
                    )
                )
            }
            if (row < gridSize - 1) {
                // Draw vertical lines
                repeat(gridSize) { col ->
                    verticalLines.add(
                        KenKenGridLine(
                            start = Offset(col * cellSize, row * cellSize),
                            end = Offset(col * cellSize, (row * cellSize) + cellSize),
                        )
                    )
                }
            }

        }
        _gridState.update {
            it.copy(
                gridSize = KenKenGridSize(gridSize),
                cellSize = cellSize,
                horizontalLines = horizontalLines,
                verticalLines = verticalLines,
            )
        }
    }

    fun processIntent(intent: KenKenGridIntent) {
        when (intent) {
            KenKenGridIntent.CreateNewShape -> TODO()
            KenKenGridIntent.Reset -> TODO()
            is KenKenGridIntent.SetGridSize -> {
                initialiseGrid(intent.size.value, _gridState.value.cellSize)
            }

            is KenKenGridIntent.SetShapeOperation -> TODO()
            is KenKenGridIntent.SetShapeValue -> TODO()
            is KenKenGridIntent.ToggleLine -> {
                _gridState.update { state ->
                    val horizontalLines = state.horizontalLines.map { currentLine ->
                        if (currentLine.start == intent.selectedLine.start && currentLine.end == intent.selectedLine.end) currentLine.copy(isSelected = !currentLine.isSelected)
                        else currentLine
                    }
                    val verticalLines = state.verticalLines.map { currentLine ->
                        if (currentLine.start == intent.selectedLine.start && currentLine.end == intent.selectedLine.end) currentLine.copy(isSelected = !currentLine.isSelected)
                        else currentLine
                    }
                    state.copy(
                        horizontalLines = horizontalLines,
                        verticalLines = verticalLines,
                    )
                }
            }

            is KenKenGridIntent.UpdateCellSize -> {
//                println("Update cell size 1: ${intent.cellSize}")
//                initialiseGrid(_gridState.value.gridSize.value, intent.cellSize)
            }
        }
    }
} 