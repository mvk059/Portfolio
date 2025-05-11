package fyi.manpreet.portfolio.ui.apps.kenken.model

import androidx.compose.ui.geometry.Offset

data class KenKenGridLine(
    val start: Offset = Offset.Unspecified,
    val end: Offset = Offset.Unspecified,
    val isSelected: Boolean = false,
)

data class KenKenGridState(
    val gridSize: KenKenGridSize = DEFAULT_GRID_SIZE,
    val cellSize: Float = 50f,
    val cells: List<List<KenKenGridItem>> = emptyList(),
    val horizontalLines: List<KenKenGridLine> = emptyList(),//List(DEFAULT_GRID_SIZE.value) { KenKenGridLine() },
    val verticalLines: List<KenKenGridLine> = emptyList(),//List(DEFAULT_GRID_SIZE.value) { KenKenGridLine() },
    val selectedLines: List<KenKenGridLine> = emptyList(), //List(DEFAULT_GRID_SIZE.value) { KenKenGridLine() },
) {

    companion object {
        val DEFAULT_GRID_SIZE = KenKenGridSize(4)
        val minGridSize = KenKenGridSize(4)
        val maxGridSize = KenKenGridSize(8)
    }
}