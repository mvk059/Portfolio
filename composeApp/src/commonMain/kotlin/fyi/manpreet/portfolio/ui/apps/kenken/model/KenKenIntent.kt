package fyi.manpreet.portfolio.ui.apps.kenken.model

import androidx.compose.ui.geometry.Offset

sealed interface KenKenGridIntent {
    data class ToggleLine(val selectedLine: KenKenGridLine) : KenKenGridIntent
    data class ToggleShapeOperator(val shape: KenKenShape) : KenKenGridIntent
    data class UpdateCellSize(val cellSize: Offset) : KenKenGridIntent
    data class UpdateGridSize(val gridSize: KenKenGridSize) : KenKenGridIntent
    data class UpdateGroupSize(val groupSize: KenKenGroupSize) : KenKenGridIntent
    data class ShapeSelection(val shape: KenKenShape) : KenKenGridIntent
    data class UpdateShape(val shape: KenKenShape, val operation: KenKenOperation, val targetValue: KenKenCellValue) : KenKenGridIntent
    data object Reset : KenKenGridIntent
}