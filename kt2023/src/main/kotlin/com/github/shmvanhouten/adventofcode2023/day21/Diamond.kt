package com.github.shmvanhouten.adventofcode2023.day21

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.toString
import kotlin.math.roundToLong


fun Grid<Char>.takeSteps(targetSteps: Int = 1, startingPosition: Coordinate = this.firstCoordinateMatching { it == 'S' }!!): Set<Coordinate> {
    var positions = setOf(startingPosition)
    repeat(targetSteps) {
        positions = positions.flatMap {
            it.getSurroundingManhattan()
        }.filter { this.contains(it) }.filter { this[it] != '#' && this[it] != '_'}.toSet()
    }
    return positions
}

/**
 * only works for square grids starting in the middle with the middle row and column being clear
 * and the minimum steps > width * 2.5
 */
fun takeStepsOnInfinitelyRepeating(steps: Int, grid: Grid<Char>): Long {
    check(grid.isSquare())

     val nrOfFilledGrids = ((steps * 2.toDouble() / grid.width) - 1).roundToUnevenLong()
//    val nrOfFilledGrids = (steps * 2 / grid.width) - 1L
    val filledOutGridsSameAsMiddle = ((nrOfFilledGrids/2) + 1) + (nrOfFilledGrids/2).downTo(1).sumOf { it * 2 }
    val filledOutGridsDiffFromMiddle = (nrOfFilledGrids/2) + ((nrOfFilledGrids/2) - 1).downTo(1).sumOf { it * 2 }

    var numberSoFar: Long = if(nrOfFilledGrids % 4 == 3L) {
        filledOutGridsDiffFromMiddle * numberOfStepsInFilledOutGrid(grid) {(steps + it) % 2 == 0} +
                filledOutGridsSameAsMiddle * numberOfStepsInFilledOutGrid(grid) {(steps + it) % 2 == 1}
    } else if(nrOfFilledGrids % 4 == 1L) {
        filledOutGridsSameAsMiddle * numberOfStepsInFilledOutGrid(grid) { (steps + it) % 2 == 0 } +
                filledOutGridsDiffFromMiddle * numberOfStepsInFilledOutGrid(grid) { (steps + it) % 2 == 1 }
    } else {
        // todo: these don't work
        filledOutGridsDiffFromMiddle * numberOfStepsInFilledOutGrid(grid) {(steps + it) % 2 == 0} +
                filledOutGridsSameAsMiddle * numberOfStepsInFilledOutGrid(grid) {(steps + it) % 2 == 1}
    }
    val topAndSideEdges = grid.takeSteps((steps - 1 - grid.width/2) % grid.width, middleLeft(grid))//.also {println("coming in from middleLeft"); println(printWithCoordinates(grid, it)) }
            .size +
            grid.takeSteps((steps - 1 - grid.width/2) % grid.width, topMiddle(grid))//.also {println("coming in from topMiddle"); println(printWithCoordinates(grid, it)) }
            .size +
            grid.takeSteps((steps - 1 - grid.width/2) % grid.width, middleRight(grid))//.also {println("coming in from middleRight"); println(printWithCoordinates(grid, it)) }
            .size +
            grid.takeSteps((steps - 1 - grid.width/2) % grid.width, bottomMiddle(grid))//.also {println("coming in from bottomMiddle"); println(printWithCoordinates(grid, it))}
            .size
    numberSoFar += topAndSideEdges

    val diagonalSecondBreachEdges = grid.takeSteps(grid.width + (steps % grid.width) - 1, topLeft(grid))//.also {println("coming in from top left (BR)"); println(printWithCoordinates(grid, it)) }
        .size +
            grid.takeSteps(grid.width + (steps % grid.width) - 1, topRight(grid))//.also {println("coming in from topRight (BL)"); println(printWithCoordinates(grid, it)) }
                .size +
            grid.takeSteps(grid.width + (steps % grid.width) - 1, bottomLeft(grid))//.also {println("coming in from bottom left (TR)"); println(printWithCoordinates(grid, it)) }
                .size +
            grid.takeSteps(grid.width + (steps % grid.width) - 1, bottomRight(grid))//.also {println("coming in from bottom right (TL)"); println(printWithCoordinates(grid, it)) }
                    .size

    numberSoFar += diagonalSecondBreachEdges * (nrOfFilledGrids/2)

    val diagonalFirstBreachEdges = grid.takeSteps((steps % grid.width) - 1, topLeft(grid))//.also{println("coming in from topLeft (BR)"); println(printWithCoordinates(grid, it)) }
        .size +
            grid.takeSteps((steps % grid.width) - 1, topRight(grid))//.also {println("coming in from topRight (BL)"); println(printWithCoordinates(grid, it)) }
                .size +
            grid.takeSteps((steps % grid.width) - 1, bottomLeft(grid))//.also {println("coming in from bottomLeft (TR)"); println(printWithCoordinates(grid, it)) }
                .size +
            grid.takeSteps((steps % grid.width) - 1, bottomRight(grid))//.also {println("coming in from bottomRight (TL)"); println(printWithCoordinates(grid, it)) }
                .size

    numberSoFar += diagonalFirstBreachEdges * ((nrOfFilledGrids/2) + 1)

    return numberSoFar
}

private fun Double.roundToUnevenLong(): Long {
    val long = this.roundToLong()
    return if(long %2 == 0L) {
        long - 1
    } else long
}

fun numberOfStepsInFilledOutGrid(grid: Grid<Char>, isEven: (Int) -> Boolean): Long {
    println()
    return 0.until(grid.height).joinToString("\n") { y ->
        0.until(grid.width).map { x ->
            val loc = Coordinate(x, y)
            if (grid[loc] == '#') '#'
            else if (isEven(x + y)) 'O'
            else '.'
        }.joinToString("")
    }//.also(::println)
        .count { it == 'O' }.toLong()
}

fun Grid<Char>.takeStep(startingPositions: Set<Coordinate> = setOf(this.firstCoordinateMatching { it == 'S' }!!)): Pair<Set<Coordinate>, List<Coordinate>> {
    val (inside, outside) = startingPositions.flatMap {
        it.getSurroundingManhattan()
    }.partition { this.contains(it) }
    return inside.filter { this[it] != '#'}.toSet() to outside
}

private fun bottomMiddle(grid: Grid<Char>) =
    grid.middleCoordinate().copy(y = grid.height - 1)

private fun middleRight(grid: Grid<Char>) =
    grid.middleCoordinate().copy(x = grid.width - 1)

private fun topMiddle(grid: Grid<Char>) =
    grid.middleCoordinate().copy(y = 0)

private fun middleLeft(grid: Grid<Char>) =
    grid.middleCoordinate().copy(x = 0)

private fun topLeft(grid: Grid<Char>? = null) = Coordinate(0, 0)

private fun topRight(grid: Grid<Char>) = Coordinate(grid.width - 1, 0)
private fun bottomLeft(grid: Grid<Char>) = Coordinate(0, grid.height - 1)
private fun bottomRight(grid: Grid<Char>) = Coordinate(grid.width - 1, grid.height - 1)


private val resetToBlack = "\u001b[30m"
private val red = "\u001b[31m"

fun printWithCoordinates(
    grid: Grid<Char>,
    it: Set<Coordinate>,
) = grid.toString(it, 'O')
    .replace("_", "${red}_$resetToBlack") + "\n\n"