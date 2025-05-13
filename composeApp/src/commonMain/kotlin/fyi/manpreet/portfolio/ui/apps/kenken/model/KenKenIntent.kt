package fyi.manpreet.portfolio.ui.apps.kenken.model

import androidx.compose.ui.geometry.Offset

sealed interface KenKenGridIntent {
    data class ToggleLine(val selectedLine: KenKenGridLine) : KenKenGridIntent
    data class ToggleShapeOperator(val shape: KenKenShape) : KenKenGridIntent
    data class UpdateCellSize(val cellSize: Offset) : KenKenGridIntent
    data object Reset : KenKenGridIntent
}