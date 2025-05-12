package fyi.manpreet.portfolio.ui.apps.kenken.util

import fyi.manpreet.portfolio.ui.apps.kenken.model.GridLineType
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridLine
import fyi.manpreet.portfolio.ui.apps.kenken.util.Constants.HORIZONTAL_ID_PREFIX
import fyi.manpreet.portfolio.ui.apps.kenken.util.Constants.UNDERSCORE
import fyi.manpreet.portfolio.ui.apps.kenken.util.Constants.VERTICAL_ID_PREFIX

fun Pair<Int, Int>.getId(gridLineType: GridLineType): String {
    val (row, column) = this
    val prefix = if (gridLineType == GridLineType.HORIZONTAL) HORIZONTAL_ID_PREFIX else VERTICAL_ID_PREFIX
    return "${prefix}${row}${UNDERSCORE}${column}"
}

fun KenKenGridLine.getRowColumnFromId(): Pair<Int, Int> {
    val chunks = id.split(UNDERSCORE).drop(1).map { it.toInt() }
    require(chunks.size == 2) { "Invalid GridLine ID: $id" }
    return chunks[0] to chunks[1]
}