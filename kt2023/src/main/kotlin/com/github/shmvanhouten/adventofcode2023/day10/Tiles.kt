package com.github.shmvanhouten.adventofcode2023.day10

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
import com.github.shmvanhouten.adventofcode2023.day10.TileIdentification.*

data class Tile(val tile: Char, val identification: TileIdentification = UNIDENTIFIED) {
    fun markAs(status: TileIdentification): Tile = copy(identification = status)
    fun isOutsideOfPipe(): Boolean = identification == LEFT_OF_PIPE || identification == RIGHT_OF_PIPE
}

enum class TileIdentification {
    UNIDENTIFIED,
    PIPE,
    LEFT_OF_PIPE,
    RIGHT_OF_PIPE;

    fun opposite(): TileIdentification = when (this) {
        LEFT_OF_PIPE -> RIGHT_OF_PIPE
        RIGHT_OF_PIPE -> LEFT_OF_PIPE
        else -> error("$this does not have an opposite")
    }
}

internal fun reOrient(tile: Char, direction: Direction) =
    when (tile) {
        '|', '-' -> direction
        'L' -> if (direction == SOUTH) EAST else NORTH
        'J' -> if (direction == SOUTH) WEST else NORTH
        '7' -> if (direction == NORTH) WEST else SOUTH
        'F' -> if (direction == NORTH) EAST else SOUTH
        'S' -> direction // it doesn't matter
        else -> error("unknown tile $tile")
    }

internal fun Char.isAccessibleFrom(dir: Direction): Boolean = when (this) {
    '|' -> dir == NORTH || dir == SOUTH
    '-' -> dir == WEST || dir == EAST
    'L' -> dir == NORTH || dir == EAST
    'J' -> dir == NORTH || dir == WEST
    '7' -> dir == SOUTH || dir == WEST
    'F' -> dir == SOUTH || dir == EAST
    '.' -> false
    else -> error("unknown tile $this")
}

internal fun Coordinate.markAdjacentTiles(
    grid: MutableGrid<Tile>,
    direction: Direction,
    tile: Char
) {
    val (locsLeft, locsRight) = tile.listLocsOnBothSides(direction)
        .eachApply { it.map { dir -> this.move(dir) } }

    locsLeft.forEach { locLeft -> grid.markLocationAs(locLeft, LEFT_OF_PIPE) }
    locsRight.forEach { locRight -> grid.markLocationAs(locRight, RIGHT_OF_PIPE) }
}

private fun Char.listLocsOnBothSides(
    direction: Direction
): Sides<List<Direction>> {
    return when (this) {
        '|', '-' -> Sides(listOf(direction.turnLeft()), listOf(direction.turnRight()))
        'L' -> when (direction) {
            WEST -> listOf(WEST, SOUTH).onTheLeft()
            SOUTH -> listOf(WEST, SOUTH).onTheRight()
            else -> error("cannot move in direction $this for tile L")
        }

        'J' -> when (direction) {
            SOUTH -> listOf(EAST, SOUTH).onTheLeft()
            EAST -> listOf(EAST, SOUTH).onTheRight()
            else -> error("cannot move in direction $this for tile J")
        }

        '7' -> when (direction) {
            EAST -> listOf(NORTH, EAST).onTheLeft()
            NORTH -> listOf(NORTH, EAST).onTheRight()
            else -> error("cannot move in direction $this for tile 7")
        }

        'F' -> when (direction) {
            NORTH -> listOf(NORTH, WEST).onTheLeft()
            WEST -> listOf(NORTH, WEST).onTheRight()
            else -> error("cannot move in direction $this for tile F")
        }

        else -> error("unknown tile $this")
    }
}

private fun MutableGrid<Tile>.markLocationAs(
    location: Coordinate,
    identification: TileIdentification
) {
    val tile = this.getOrNull(location)
    if (tile?.identification == UNIDENTIFIED)
        this[location] = tile.copy(identification = identification)
}

data class Sides<T>(val left: T, val right: T) {
    fun <R> eachApply(transform: (T) -> R): Sides<R> = Sides(transform(left), transform(right))
}

fun <T> List<T>.onTheLeft() = Sides(left = this, right = emptyList())
fun <T> List<T>.onTheRight() = Sides(left = emptyList(), right = this)