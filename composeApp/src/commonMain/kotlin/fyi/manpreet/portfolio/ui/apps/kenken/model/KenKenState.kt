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
    val boundaryLineIds: Set<String> = emptySet(),
    val selectedLineIds: Set<String> = emptySet(),
    val shapes: List<KenKenShape> = emptyList()
) {
    companion object {
        val DEFAULT_GRID_SIZE = KenKenGridSize(6)
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

@Immutable
data class KenKenShape(
    val id: String,
    val cells: List<Pair<Int, Int>>,
    val operator: KenKenOperator,
    val isSelected: Boolean = false,
) {

    @Immutable
    data class KenKenOperator(
        val topLeft: Offset = Offset.Unspecified,
        val operation: KenKenOperation = KenKenOperation.ADD,
        val targetValue: KenKenCellValue = KenKenCellValue(0)
    )
}

@JvmInline
value class KenKenCellValue(val value: Int)

@JvmInline
value class KenKenGridSize(val value: Int)

@JvmInline
value class KenKenRowCell(val value: Int)

@JvmInline
value class KenKenColCell(val value: Int)
