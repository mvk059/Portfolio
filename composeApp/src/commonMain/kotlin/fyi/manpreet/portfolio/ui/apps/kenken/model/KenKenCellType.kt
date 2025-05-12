package fyi.manpreet.portfolio.ui.apps.kenken.model

sealed interface KenKenCellType {
    data class Start(val operation: KenKenOperation, val value: KenKenCellValue) : KenKenCellType
    data object NonStart : KenKenCellType
}

enum class GridLineType {
    HORIZONTAL, VERTICAL
}