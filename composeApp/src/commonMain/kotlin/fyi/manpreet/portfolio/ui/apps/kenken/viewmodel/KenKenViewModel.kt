package fyi.manpreet.portfolio.ui.apps.kenken.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fyi.manpreet.portfolio.ui.apps.kenken.model.GridLineType
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridIntent
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridLine
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridSize
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridState
import fyi.manpreet.portfolio.ui.apps.kenken.usecase.KenKenShapeUseCase
import fyi.manpreet.portfolio.ui.apps.kenken.util.getHorizontalId
import fyi.manpreet.portfolio.ui.apps.kenken.util.getStartAndEndCoordinatesFromId
import fyi.manpreet.portfolio.ui.apps.kenken.util.getVerticalId
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

    private val shapeUseCase: KenKenShapeUseCase = KenKenShapeUseCase()

    private fun initialiseGrid(gridSize: Int?, cellSize: Offset?) {
        val gridSize = gridSize ?: _gridState.value.gridSize.value
        val cellSize = cellSize ?: _gridState.value.cellSize
        require(gridSize in KenKenGridState.minGridSize.value..KenKenGridState.maxGridSize.value) { "Grid size is not in range" }

        val horizontalLines = mutableListOf<KenKenGridLine>()
        val verticalLines = mutableListOf<KenKenGridLine>()

        repeat(gridSize) { row ->

            // Setup horizontal grid lines
            repeat(gridSize - 1) { col ->
                val id = (row to col).getHorizontalId()
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
                    val id = (row to col).getVerticalId()
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

        val boundaryLineIds = _gridState.value.boundaryLineIds.ifEmpty { getBoundaryIds(horizontalLines, verticalLines) }
        val selectedLineIds = _gridState.value.selectedLineIds.ifEmpty { boundaryLineIds }

        _gridState.update {
            it.copy(
                gridSize = KenKenGridSize(gridSize),
                cellSize = cellSize,
                horizontalLines = horizontalLines,
                verticalLines = verticalLines,
                boundaryLineIds = boundaryLineIds,
                selectedLineIds = selectedLineIds,
                shapes = shapeUseCase.detectShapes(_gridState.value.gridSize, _gridState.value.selectedLineIds)
            )
        }
    }

    fun processIntent(intent: KenKenGridIntent) {
        when (intent) {
            KenKenGridIntent.CreateNewShape -> createNewShape()
            is KenKenGridIntent.SetShapeOperation -> TODO()
            is KenKenGridIntent.SetShapeValue -> TODO()
            is KenKenGridIntent.ToggleLine -> toggleLine(intent.selectedLine)
            is KenKenGridIntent.UpdateCellSize -> updateCellSize(intent.cellSize)
            KenKenGridIntent.Reset -> TODO()
        }
    }

    private fun createNewShape() {
        val shapes = shapeUseCase.detectShapes(_gridState.value.gridSize, _gridState.value.selectedLineIds)
        _gridState.update { it.copy(shapes = shapes) }
    }

    private fun toggleLine(clickedLine: KenKenGridLine) {
        // Boundary line click
        if (clickedLine.id in _gridState.value.boundaryLineIds) return

        val targetLines = if (clickedLine.gridLineType == GridLineType.VERTICAL) _gridState.value.verticalLines else _gridState.value.horizontalLines
        val targetLine = targetLines.find { it.id == clickedLine.id } ?: return

        // Add or remove the clicked line from the selected lines
        val selectedIds = _gridState.value.selectedLineIds.toHashSet()
        if (targetLine.id in selectedIds) selectedIds.remove(targetLine.id)
        else selectedIds.add(targetLine.id)

        _gridState.update { it.copy(selectedLineIds = selectedIds) }
        createNewShape()
    }

    private fun updateCellSize(cellSize: Offset) {
        initialiseGrid(gridSize = _gridState.value.gridSize.value, cellSize = cellSize)
    }

    private fun getBoundaryIds(horizontalLines: MutableList<KenKenGridLine>, verticalLines: MutableList<KenKenGridLine>): Set<String> = buildSet {
        val gridSize = _gridState.value.gridSize.value

        horizontalLines.forEach { line ->
            val (row, _) = line.getStartAndEndCoordinatesFromId()
            if (row == 0 || row == gridSize - 1) add(line.id)
        }

        verticalLines.forEach { line ->
            val (_, col) = line.getStartAndEndCoordinatesFromId()
            if (col == 0 || col == gridSize - 1) add(line.id)
        }
    }
}