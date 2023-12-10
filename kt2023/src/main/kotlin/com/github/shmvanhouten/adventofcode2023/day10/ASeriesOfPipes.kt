package com.github.shmvanhouten.adventofcode2023.day10

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
import com.github.shmvanhouten.adventofcode2023.day10.TileIdentification.*

fun farthestAwayFromStart(grid: Grid<Char>): Int {
    val (startingPoint, startingDirection: Direction) = getStartingPointAndDirection(grid)
    return (generateSequence(startingPoint.move(startingDirection) to startingDirection) { (loc, direction) ->
        val tile = grid[loc]
        val newDirection = reOrient(tile, direction)
        loc.move(newDirection) to newDirection
    }.takeWhile { grid[it.first] != 'S' }.count() + 1) / 2
}

fun countEnclosedTiles(_grid: Grid<Char>): Int {
    val (startingPoint, startingDirection: Direction) = getStartingPointAndDirection(_grid)

    val grid = _grid.map { Tile(it) }.toMutableGrid()
    grid[startingPoint] = grid[startingPoint].copy(identification = PIPE)

    moveThroughPipeMarkingAdjacentTiles(startingPoint, startingDirection, grid)

    extrapolateAdjacentTiles(grid)

    printGrid(grid)
    val outsideStatus = findWhichSideOfLoopIsInside(grid)
    return grid.count { it.identification == outsideStatus }
}

private fun moveThroughPipeMarkingAdjacentTiles(
    startingPoint: Coordinate,
    startingDirection: Direction,
    grid: MutableGrid<Tile>

) = generateSequence(startingPoint.move(startingDirection) to startingDirection) { (loc, direction) ->
    val tile = grid[loc]
    grid[loc] = tile.markAs(PIPE)

    loc.markAdjacentTiles(grid, direction, tile.tile)

    val newDirection = reOrient(tile.tile, direction)
    loc.move(newDirection) to newDirection
}.dropWhile { grid[it.first].tile != 'S' }.first().let { Unit }

private fun getStartingPointAndDirection(grid: Grid<Char>): Pair<Coordinate, Direction> {
    val startingPoint = grid.firstCoordinateMatching { it == 'S' }!!
    val startingDirection: Direction = pickFirstDirection(startingPoint, grid)
    return startingPoint to startingDirection
}

private fun pickFirstDirection(startingPoint: Coordinate, grid: Grid<Char>): Direction =
    startingPoint.getSurroundingManhattanWithDirection().first { (c, direction) ->
        val neighbour = grid.getOrNull(c)
        neighbour != null && neighbour.isAccessibleFrom(direction.opposite())
    }.second

private fun findWhichSideOfLoopIsInside(grid: Grid<Tile>): TileIdentification =
    grid.perimiter()
        .first { it.isOutsideOfPipe() }
        .identification.opposite()

private fun extrapolateAdjacentTiles(grid: MutableGrid<Tile>) {
    extrapolateAdjacentTiles(grid, LEFT_OF_PIPE)
    extrapolateAdjacentTiles(grid, RIGHT_OF_PIPE)
}

private fun extrapolateAdjacentTiles(grid: MutableGrid<Tile>, identification: TileIdentification) {
    val leftTiles = grid.coordinatesMatching { it.identification == identification }
    fillOutFromTile(grid, leftTiles, identification)
}

private fun fillOutFromTile(grid: MutableGrid<Tile>, locs: List<Coordinate>, status: TileIdentification) {
    val unchecked = locs.flatMap { it.getSurroundingManhattan() }.toMutableList()
    while (unchecked.isNotEmpty()) {
        val location = unchecked.removeLast()
        val nextTile = grid.getOrNull(location)
        if(nextTile?.identification == UNIDENTIFIED) {
            grid[location] = nextTile.copy(identification = status)
            unchecked += location.getSurroundingManhattan()
        }
    }
}
