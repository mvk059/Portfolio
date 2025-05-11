package fyi.manpreet.portfolio.ui.apps.kenken.model

import kotlin.jvm.JvmInline

data class KenKenGridItem(
    val id: String,
    val row: KenKenRowCell,
    val column: KenKenColCell,
    val cellType: KenKenCellType,
    val isSelected: Boolean = false,
)

@JvmInline
value class KenKenCellValue(val value: Int)

@JvmInline
value class KenKenGridSize(val value: Int)

@JvmInline
value class KenKenRowCell(val value: Int)

@JvmInline
value class KenKenColCell(val value: Int)