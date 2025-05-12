package fyi.manpreet.portfolio.ui.apps.kenken.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import kotlin.jvm.JvmInline

@Immutable
data class KenKenGridState(
    val gridSize: KenKenGridSize = DEFAULT_GRID_SIZE,
    val cellSize: Offset = Offset(50f, 50f),
    val cells: List<List<KenKenGridItem>> = emptyList(),
    val horizontalLines: List<KenKenGridLine> = emptyList(),
    val verticalLines: List<KenKenGridLine> = emptyList(),
    val selectedLineIds: Set<String> = emptySet(),
) {
    companion object {
        val DEFAULT_GRID_SIZE = KenKenGridSize(4)
        val minGridSize = KenKenGridSize(4)
        val maxGridSize = KenKenGridSize(8)
    }

}

@Immutable
data class KenKenGridItem(
    val id: String,
    val row: KenKenRowCell,
    val column: KenKenColCell,
    val cellType: KenKenCellType,
    val isSelected: Boolean = false,
)

@Immutable
data class KenKenGridLine(
    val id: String,
    val start: Offset = Offset.Unspecified,
    val end: Offset = Offset.Unspecified,
    val gridLineType: GridLineType,
)

@JvmInline
value class KenKenCellValue(val value: Int)

@JvmInline
value class KenKenGridSize(val value: Int)

@JvmInline
value class KenKenRowCell(val value: Int)

@JvmInline
value class KenKenColCell(val value: Int)
