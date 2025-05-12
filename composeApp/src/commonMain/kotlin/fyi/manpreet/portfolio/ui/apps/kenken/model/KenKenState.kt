package fyi.manpreet.portfolio.ui.apps.kenken.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset

@Immutable
data class KenKenGridLine(
    val id: String,
    val start: Offset = Offset.Unspecified,
    val end: Offset = Offset.Unspecified,
    val gridLineType: GridLineType,
)

@Immutable
data class KenKenGridState(
    val gridSize: KenKenGridSize = DEFAULT_GRID_SIZE,
    val cellSize: Offset = Offset(50f, 50f),
    val cells: List<List<KenKenGridItem>> = emptyList(),
    val horizontalLines: List<KenKenGridLine> = emptyList(),//List(DEFAULT_GRID_SIZE.value) { KenKenGridLine() },
    val verticalLines: List<KenKenGridLine> = emptyList(),//List(DEFAULT_GRID_SIZE.value) { KenKenGridLine() },
    val selectedLineIds: Set<String> = emptySet(),
) {

    companion object {
        val DEFAULT_GRID_SIZE = KenKenGridSize(4)
        val minGridSize = KenKenGridSize(4)
        val maxGridSize = KenKenGridSize(8)
    }
}