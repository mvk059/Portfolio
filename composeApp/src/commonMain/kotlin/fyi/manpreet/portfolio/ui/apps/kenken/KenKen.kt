package fyi.manpreet.portfolio.ui.apps.kenken

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Data class representing a single grid item
 */
data class GridItem(
    val id: String,
    val row: Int,
    val column: Int,
    val content: String = "",
    val isSelected: Boolean = false
)

/**
 * Data class representing a grid cell boundary
 */
data class GridBoundary(
    val id: String,
    val type: BoundaryType,
    val row: Int,
    val column: Int,
    val isSelected: Boolean = false
)

/**
 * Enum defining the boundary type (horizontal or vertical)
 */
enum class BoundaryType {
    HORIZONTAL, VERTICAL
}

/**
 * Sealed class representing grid click events
 */
sealed class GridClickEvent {
    data class CellClicked(val item: GridItem) : GridClickEvent()
    data class BoundaryClicked(val boundary: GridBoundary) : GridClickEvent()
}

/**
 * Grid state management class that handles grid dimensions and interactions
 */
class GridState(
    private val rows: Int,
    private val columns: Int,
    initialItems: List<GridItem> = emptyList(),
    initialBoundaries: List<GridBoundary> = emptyList()
) {
    // Tracks cell state
    private val _gridItems = MutableStateFlow(
        initialItems.ifEmpty {
            List(rows * columns) { index ->
                GridItem(
                    id = "cell_${index}",
                    row = index / columns,
                    column = index % columns
                )
            }
        }
    )
    val gridItems: StateFlow<List<GridItem>> = _gridItems.asStateFlow()

    // Tracks boundary state
    private val _gridBoundaries = MutableStateFlow(
        initialBoundaries.ifEmpty {
            buildList {
                // Horizontal boundaries (rows + 1) * columns
                for (row in 0..rows) {
                    for (col in 0 until columns) {
                        add(
                            GridBoundary(
                                id = "h_${row}_${col}",
                                type = BoundaryType.HORIZONTAL,
                                row = row,
                                column = col
                            )
                        )
                    }
                }
                // Vertical boundaries rows * (columns + 1)
                for (row in 0 until rows) {
                    for (col in 0..columns) {
                        add(
                            GridBoundary(
                                id = "v_${row}_${col}",
                                type = BoundaryType.VERTICAL,
                                row = row,
                                column = col
                            )
                        )
                    }
                }
            }
        }
    )
    val gridBoundaries: StateFlow<List<GridBoundary>> = _gridBoundaries.asStateFlow()

    // Click events flow
    private val _clickEvents = MutableStateFlow<GridClickEvent?>(null)
    val clickEvents: StateFlow<GridClickEvent?> = _clickEvents.asStateFlow()

    /**
     * Handle a cell click
     */
    fun onCellClicked(row: Int, column: Int) {
        val index = row * columns + column
        val updatedItems = _gridItems.value.toMutableList()

        if (index < updatedItems.size) {
            val clickedItem = updatedItems[index]
            // Toggle selection state
            updatedItems[index] = clickedItem.copy(isSelected = !clickedItem.isSelected)
            _gridItems.value = updatedItems

            // Emit click event
            _clickEvents.value = GridClickEvent.CellClicked(updatedItems[index])
        }
    }

    /**
     * Handle a boundary click
     */
    fun onBoundaryClicked(id: String) {
        val boundaries = _gridBoundaries.value.toMutableList()
        val index = boundaries.indexOfFirst { it.id == id }

        if (index != -1) {
            val clickedBoundary = boundaries[index]
            // Toggle selection state
            boundaries[index] = clickedBoundary.copy(isSelected = !clickedBoundary.isSelected)
            _gridBoundaries.value = boundaries

            // Emit click event
            _clickEvents.value = GridClickEvent.BoundaryClicked(boundaries[index])
        }
    }

    /**
     * Reset event after consumption
     */
    fun consumeClickEvent() {
        _clickEvents.value = null
    }

    /**
     * Reset all selections
     */
    fun clearAllSelections() {
        _gridItems.update { items ->
            items.map { it.copy(isSelected = false) }
        }

        _gridBoundaries.update { boundaries ->
            boundaries.map { it.copy(isSelected = false) }
        }
    }
}

/**
 * Composable function that renders the grid with cells and boundaries
 */
@Composable
fun ClickableGrid(
    modifier: Modifier = Modifier,
    gridState: GridState,
    cellContent: @Composable (GridItem) -> Unit = { item ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.content.ifEmpty { "${item.row},${item.column}" })
        }
    }
) {
    val items by gridState.gridItems.collectAsState()
    val boundaries by gridState.gridBoundaries.collectAsState()

    // Local state to track component dimensions
    var gridSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    // Calculate cell size based on grid size and item count
    val cellSize = with(density) {
        val columns = items.maxOf { it.column } + 1
        val width = (gridSize.width / columns).toDp()
        val rows = items.maxOf { it.row } + 1
        val height = (gridSize.height / rows).toDp()
        width.coerceAtMost(height)
    }

    // Get horizontal and vertical boundaries
    val horizontalBoundaries = boundaries.filter { it.type == BoundaryType.HORIZONTAL }
    val verticalBoundaries = boundaries.filter { it.type == BoundaryType.VERTICAL }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { gridSize = it }
            .drawBehind {
                // Draw all horizontal boundaries
                horizontalBoundaries.forEach { boundary ->
                    val y = size.height * boundary.row / (horizontalBoundaries.maxOf { it.row } + 1)
                    val lineColor = if (boundary.isSelected) Color.Red else Color.Gray
                    val strokeWidth = if (boundary.isSelected) 4f else 2f

                    drawLine(
                        color = lineColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }

                // Draw all vertical boundaries
                verticalBoundaries.forEach { boundary ->
                    val x = size.width * boundary.column / (verticalBoundaries.maxOf { it.column } + 1)
                    val lineColor = if (boundary.isSelected) Color.Red else Color.Gray
                    val strokeWidth = if (boundary.isSelected) 4f else 2f

                    drawLine(
                        color = lineColor,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = strokeWidth
                    )
                }
            }
            // Handle boundary clicks
            .pointerInput(boundaries) {
                detectTapGestures { offset ->
                    // Check if a boundary was clicked
                    // First check horizontal boundaries
                    val rows = horizontalBoundaries.maxOf { it.row } + 1
                    val cols = verticalBoundaries.maxOf { it.column } + 1
                    val cellHeight = size.height / rows
                    val cellWidth = size.width / cols

                    // Tolerance for boundary click detection
                    val clickTolerance = 16f

                    // Check horizontal boundaries
                    for (row in 0..rows) {
                        val y = row * cellHeight
                        if (offset.y in (y - clickTolerance)..(y + clickTolerance)) {
                            // Find which column this is in
                            val col = (offset.x / cellWidth).toInt().coerceIn(0, cols - 1)
                            val boundaryId = "h_${row}_${col}"
                            horizontalBoundaries.find { it.id == boundaryId }?.let {
                                gridState.onBoundaryClicked(it.id)
                                return@detectTapGestures
                            }
                        }
                    }

                    // Check vertical boundaries
                    for (col in 0..cols) {
                        val x = col * cellWidth
                        if (offset.x in (x - clickTolerance)..(x + clickTolerance)) {
                            // Find which row this is in
                            val row = (offset.y / cellHeight).toInt().coerceIn(0, rows - 1)
                            val boundaryId = "v_${row}_${col}"
                            verticalBoundaries.find { it.id == boundaryId }?.let {
                                gridState.onBoundaryClicked(it.id)
                                return@detectTapGestures
                            }
                        }
                    }
                }
            }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(items.maxOf { it.column } + 1),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items, key = { it.id }) { item ->
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(cellSize)
                        .clickable { gridState.onCellClicked(item.row, item.column) },
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.isSelected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    cellContent(item)
                }
            }
        }
    }
}

/**
 * Example usage of the ClickableGrid
 */
@Composable
fun GridDemo() {
    // Create a grid state with 5 rows and 5 columns
    val gridState = remember { GridState(rows = 5, columns = 5) }
    val clickEvent by gridState.clickEvents.collectAsState()

    // Process click events
    LaunchedEffect(clickEvent) {
        clickEvent?.let {
            when (it) {
                is GridClickEvent.CellClicked -> {
                    println("Cell clicked: Row ${it.item.row}, Column ${it.item.column}")
                }
                is GridClickEvent.BoundaryClicked -> {
                    println("Boundary clicked: ${it.boundary.type} at Row ${it.boundary.row}, Column ${it.boundary.column}")
                }
            }
            gridState.consumeClickEvent()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Clickable Grid Example",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ClickableGrid(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            gridState = gridState
        )
    }
}