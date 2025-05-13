package fyi.manpreet.portfolio.ui.apps.kenken.usecase

import androidx.compose.ui.geometry.Offset
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridLine
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridSize
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenShape
import fyi.manpreet.portfolio.ui.apps.kenken.util.getBottomID
import fyi.manpreet.portfolio.ui.apps.kenken.util.getLeftID
import fyi.manpreet.portfolio.ui.apps.kenken.util.getRightID
import fyi.manpreet.portfolio.ui.apps.kenken.util.getShapeID
import fyi.manpreet.portfolio.ui.apps.kenken.util.getStartCoordinatesFromId
import fyi.manpreet.portfolio.ui.apps.kenken.util.getTopID

class KenKenShapeUseCase {

    fun detectShapes(
        gridSize: KenKenGridSize,
        selectedLineIds: Set<String>,
        horizontalLines: List<KenKenGridLine>,
        shapes: List<KenKenShape>,
    ): List<KenKenShape> = buildList {
        val gridSize = gridSize.value - 1
        val visited = mutableSetOf<Pair<Int, Int>>()

        // Examine each cell as a potential starting point
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                if ((row to col) in visited) continue

                val shapeCells = floodFill(startRow = row, startCol = col, gridSize = gridSize, visited = visited, selectedLineIds = selectedLineIds)
                if (shapeCells.isEmpty()) continue

                var shape = KenKenShape(
                    id = (row to col).getShapeID(),
                    cells = shapeCells,
                    operator = findShapePosition(shape = shapeCells.first(), horizontalLines = horizontalLines, shapes = shapes)
                )
                val existingShape = shapes.firstOrNull { it.id == (row to col).getShapeID() }
                if (existingShape != null)
                    shape = shape.copy(operator = shape.operator.copy(operation = existingShape.operator.operation))
                add(shape)
            }

        }
    }

    /**
     * Find the top left offset for the [shape]
     */
    private fun findShapePosition(shape: Pair<Int, Int>, horizontalLines: List<KenKenGridLine>, shapes: List<KenKenShape>): KenKenShape.KenKenOperator {
        horizontalLines.forEachIndexed { index, line ->
            if (shape == line.getStartCoordinatesFromId()) {
                val boxWidth = line.end.x - line.start.x
                val topLeft = Offset(line.start.x + boxWidth / 3, line.start.y + boxWidth / 8)
                return KenKenShape.KenKenOperator(topLeft = topLeft)
            }
        }
        throw IllegalArgumentException("Shape position is not valid. ")
    }

    /**
     * Identify all the grid boxes that form a shape using the flood fill algorithm
     */
    private fun floodFill(
        startRow: Int,
        startCol: Int,
        gridSize: Int,
        visited: MutableSet<Pair<Int, Int>>,
        selectedLineIds: Set<String>,
    ): List<Pair<Int, Int>> {
        val shape = mutableListOf<Pair<Int, Int>>()
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(Pair(startRow, startCol))

        while (queue.isNotEmpty()) {
            val (row, col) = queue.removeFirst()

            if (Pair(row, col) in visited) continue

            visited.add(Pair(row, col))
            shape.add(Pair(row, col))

            // Check the top boundary
            val top = (row to col).getTopID()
            if (row - 1 in 0 until gridSize && col in 0 until gridSize &&
                top !in selectedLineIds &&
                Pair(row - 1, col) !in visited
            ) {
                queue.add(row - 1 to col)
            }

            // Check the right boundary
            val right = (row to col).getRightID()
            if (row in 0 until gridSize && col + 1 in 0 until gridSize &&
                right !in selectedLineIds &&
                Pair(row, col + 1) !in visited
            ) {
                queue.add(row to col + 1)
            }

            // Check the bottom boundary
            val bottom = (row to col).getBottomID()
            if (row + 1 in 0 until gridSize && col in 0 until gridSize &&
                bottom !in selectedLineIds &&
                Pair(row + 1, col) !in visited
            ) {
                queue.add(row + 1 to col)
            }

            // Check the left boundary
            val left = (row to col).getLeftID()
            if (row in 0 until gridSize && col - 1 in 0 until gridSize &&
                left !in selectedLineIds &&
                Pair(row, col - 1) !in visited
            ) {
                queue.add(row to col - 1)
            }
        }

        return shape
    }
}