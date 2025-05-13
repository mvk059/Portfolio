package fyi.manpreet.portfolio.ui.apps.kenken.util

import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridLine

fun Pair<Int, Int>.getHorizontalId(): String = buildString {
    val (row, col) = this@getHorizontalId
    append("$row $col $row ${col + 1}")
}

fun Pair<Int, Int>.getVerticalId(): String = buildString {
    val (row, col) = this@getVerticalId
    append("$row $col ${row + 1} $col")
}

fun Pair<Int, Int>.getTopID(): String = buildString {
    val (row, col) = this@getTopID
    append("$row $col $row ${col + 1}")
}

fun Pair<Int, Int>.getRightID(): String = buildString {
    val (row, col) = this@getRightID
    append("$row ${col + 1} ${row + 1} ${col + 1}")
}

fun Pair<Int, Int>.getBottomID(): String = buildString {
    val (row, col) = this@getBottomID
    append("${row + 1} $col ${row + 1} ${col + 1}")
}

fun Pair<Int, Int>.getLeftID(): String = buildString {
    val (row, col) = this@getLeftID
    append("$row $col ${row + 1} $col")
}

fun KenKenGridLine.getStartAndEndCoordinatesFromId(): Pair<Int, Int> {
    val chunks = id.split(" ").map(String::toInt)
    require(chunks.size == 4) { "Invalid GridLine ID: $id" }
    return chunks.first() to chunks.last()
}

fun KenKenGridLine.getStartCoordinatesFromId(): Pair<Int, Int> {
    val chunks = id.split(" ").map(String::toInt)
    require(chunks.size == 4) { "Invalid GridLine ID: $id" }
    return chunks.first() to chunks[1]
}