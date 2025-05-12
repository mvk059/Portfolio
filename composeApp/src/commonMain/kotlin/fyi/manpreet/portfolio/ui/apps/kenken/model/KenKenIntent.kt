package fyi.manpreet.portfolio.ui.apps.kenken.model

import androidx.compose.ui.geometry.Offset

sealed interface KenKenGridIntent {
    data class ToggleLine(val selectedLine: KenKenGridLine) : KenKenGridIntent
    data object CreateNewShape : KenKenGridIntent
    data class SetShapeOperation(val operation: KenKenOperation) : KenKenGridIntent
    data class SetShapeValue(val value: KenKenCellValue) : KenKenGridIntent
    data class UpdateCellSize(val cellSize: Offset) : KenKenGridIntent
    data object Reset : KenKenGridIntent
}