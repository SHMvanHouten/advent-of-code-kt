package com.github.shmvanhouten.adventofcode2023.day21

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.toString


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
fun takeStepsOnInfinitelyRepeating(steps: Int, grid: Grid<Char>, startingPosition: Coordinate = grid.middleCoordinate()): Long {
    check(grid.isSquare())

    val reachesEdgesAfter = grid.width/2 + 1
    val timesGridEdgeIsBreached = (steps - reachesEdgesAfter)/grid.width + 1
    val amountOfStepsIntoPerimiterGrids = (steps - reachesEdgesAfter) % grid.width // + 1 (but first step is already counted)

    val numberOfCompletelyFilledOutGrids = (timesGridEdgeIsBreached * 4) //+ 1 // (our starting grid)
    var numberSoFar = 9 * numberOfStepsInFilledOutGrid(grid) {(steps + it) % 2 == 0}
    numberSoFar += 4 * numberOfStepsInFilledOutGrid(grid) {(steps + it) % 2 == 1} // todo: how do we divide up these (9 + 4 = numberOfCompletelyFilledOutGrids)

    // these check out for an even number of steps... (but don't see why uneven would be different)
    val topAndSideEdges = grid.takeSteps(amountOfStepsIntoPerimiterGrids, middleLeft(grid)).size +
            grid.takeSteps(amountOfStepsIntoPerimiterGrids, topMiddle(grid)).size +
            grid.takeSteps(amountOfStepsIntoPerimiterGrids, middleRight(grid)).size +
            grid.takeSteps(amountOfStepsIntoPerimiterGrids, bottomMiddle(grid)).size
    numberSoFar += topAndSideEdges
//
//    val middleSecondBreachEdges = grid.takeSteps(grid.width.roofDiv(2) + 1 + amountOfStepsIntoPerimiterGrids, middleLeft(grid)).size +
//            grid.takeSteps(grid.width.roofDiv(2) + 1 + amountOfStepsIntoPerimiterGrids, topMiddle(grid)).size +
//            grid.takeSteps(grid.width.roofDiv(2) + 1 + amountOfStepsIntoPerimiterGrids, middleRight(grid)).size +
//            grid.takeSteps(grid.width.roofDiv(2) + 1 + amountOfStepsIntoPerimiterGrids, bottomMiddle(grid)).size
//    numberSoFar += middleSecondBreachEdges


    val diagonalSecondBreachEdges = grid.takeSteps(grid.width + (amountOfStepsIntoPerimiterGrids / 2) - 1, topLeft(grid))
//        .also {
//            println("coming in from top left (BR)")
//            println(printWithCoordinates(grid, it)) }
        .size +
            grid.takeSteps(grid.width + (amountOfStepsIntoPerimiterGrids / 2) - 1, topRight(grid))
//                .also {
//                    println("coming in from topRight (BL)")
//                    println(printWithCoordinates(grid, it)) }
                .size +
            grid.takeSteps(grid.width + (amountOfStepsIntoPerimiterGrids / 2) - 1, bottomLeft(grid))
//                .also {
//                    println("coming in from bottom left (TR)")
//                    println(printWithCoordinates(grid, it)) }
                .size +
            grid.takeSteps(grid.width + (amountOfStepsIntoPerimiterGrids / 2) - 1, bottomRight(grid))
//                .also {
//                    println("coming in from bottom right (TL)")
//                    println(printWithCoordinates(grid, it)) }
                    .size

    numberSoFar += diagonalSecondBreachEdges * 2 // todo: 2 should be calculated
//
    val diagonalFirstBreachEdges = grid.takeSteps((amountOfStepsIntoPerimiterGrids / 2) - 1, topLeft(grid))
        .also {
                    println("coming in from topLeft (BR)")
                    println(printWithCoordinates(grid, it)) }
        .size +
            grid.takeSteps((amountOfStepsIntoPerimiterGrids/ 2) - 1, topRight(grid))
                .also {
                    println("coming in from topRight (BL)")
                    println(printWithCoordinates(grid, it)) }
                .size +
            grid.takeSteps((amountOfStepsIntoPerimiterGrids/ 2) - 1, bottomLeft(grid))
                .also {
                    println("coming in from bottomLeft (TR)")
                    println(printWithCoordinates(grid, it)) }
                .size +
            grid.takeSteps((amountOfStepsIntoPerimiterGrids/ 2) - 1, bottomRight(grid))
                .also {
                    println("coming in from bottomRight (TL)")
                    println(printWithCoordinates(grid, it)) }
                .size

    numberSoFar += diagonalFirstBreachEdges * 3 // todo: 3 should be calculated

    return numberSoFar
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