package fyi.manpreet.portfolio.ui.apps.kenken.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fyi.manpreet.portfolio.ui.apps.kenken.model.GridLineType
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
        .onStart { initialiseGrid(_gridState.value.gridSize.value, _gridState.value.cellSize) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = KenKenGridState()
        )

    private fun initialiseGrid(gridSize: Int?, cellSize: Offset?) {
        val gridSize = gridSize ?: _gridState.value.gridSize.value
        val cellSize = cellSize ?: _gridState.value.cellSize
        require(gridSize in KenKenGridState.minGridSize.value..KenKenGridState.maxGridSize.value) { "Grid size is not in range" }

        val horizontalLines = mutableListOf<KenKenGridLine>()
        val verticalLines = mutableListOf<KenKenGridLine>()

        repeat(gridSize) { row ->

            // Setup horizontal grid lines
            repeat(gridSize - 1) { col ->
                val id = "h_${row}_${col}"
                horizontalLines.add(
                    KenKenGridLine(
                        id = id,
                        start = Offset(col * cellSize.x, row * cellSize.y),
                        end = Offset((col * cellSize.x) + cellSize.x, row * cellSize.y),
                        gridLineType = GridLineType.HORIZONTAL,
                    )
                )
            }

            // Setup vertical grid lines
            if (row < gridSize - 1) {
                // Draw vertical lines
                repeat(gridSize) { col ->
                    val id = "v_${row}_${col}"
                    verticalLines.add(
                        KenKenGridLine(
                            id = id,
                            start = Offset(col * cellSize.x, row * cellSize.y),
                            end = Offset(col * cellSize.x, (row * cellSize.y) + cellSize.y),
                            gridLineType = GridLineType.VERTICAL,
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
        println("Update cell size 2: ${_gridState.value.horizontalLines}")
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
            is KenKenGridIntent.ToggleLine -> toggleLine(intent.selectedLine)
            is KenKenGridIntent.UpdateCellSize -> {
                println("Update cell size 1: ${intent.cellSize}")
                initialiseGrid(_gridState.value.gridSize.value, intent.cellSize)
            }
        }
    }

    private fun toggleLine(clickedLine: KenKenGridLine) {
        val targetLines = if (clickedLine.gridLineType == GridLineType.VERTICAL) _gridState.value.verticalLines else _gridState.value.horizontalLines
        val targetLine = targetLines.find { it.id == clickedLine.id } ?: return

        val selectedIds = _gridState.value.selectedLineIds.toHashSet()
        if (targetLine.id in selectedIds) selectedIds.remove(targetLine.id)
        else selectedIds.add(targetLine.id)

        _gridState.update { it.copy(selectedLineIds = selectedIds) }
    }
} 