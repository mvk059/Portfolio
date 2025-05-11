package fyi.manpreet.portfolio.ui.apps.kenken.model

sealed interface KenKenGridIntent {
    data class SetGridSize(val size: KenKenGridSize) : KenKenGridIntent
    data class ToggleLine(val selectedLine: KenKenGridLine) : KenKenGridIntent
    data object CreateNewShape : KenKenGridIntent
    data class SetShapeOperation(val operation: KenKenOperation) : KenKenGridIntent
    data class SetShapeValue(val value: KenKenCellValue) : KenKenGridIntent
    data class UpdateCellSize(val cellSize: Float) : KenKenGridIntent
    data object Reset : KenKenGridIntent
}