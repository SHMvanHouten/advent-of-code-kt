package com.github.shmvanhouten.adventofcode2024.day15

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import com.github.shmvanhouten.adventofcode.utility.strings.blocks

private val ROBOT = '@'
private val BOX = 'O'
private val WALL = '#'
private val EMPTY = '.'

fun followInstructions(_grid: Grid<Char>, instuctions: String): Grid<Char> {
    val grid = _grid.toMutableGrid()
    var robot = grid.firstLocationOf { it == ROBOT }!!
    instuctions.map { toDirection(it) }.forEach { dir ->
        robot = grid.moveItem(robot, dir)
    }
    return grid
}

fun getGPS(grid: Grid<Char>): Int {
    return grid.coordinatesMatching { it == BOX }
        .sumOf { it.x + it.y * 100 }
}

fun toDirection(char: Char): Direction {
    print(char)
    return when(char) {
        '>' -> Direction.EAST
        'v' -> Direction.SOUTH
        '<' -> Direction.WEST
        '^' -> Direction.NORTH
        else -> error("unknown direction for $char")
    }
}

internal fun parse(input: String): Pair<Grid<Char>, String> {
    val (grid, instructions) = input.blocks()
    return charGrid(grid) to instructions.filter { it != '\n' }
}

private fun MutableGrid<Char>.moveItem(loc: Coordinate, dir: Direction, item: Char = this[loc]): Coordinate {
    val newLoc = loc.move(dir)
    val itemAtNewLoc = this[newLoc]
    if(itemAtNewLoc == WALL) {
        return loc
    } else if (itemAtNewLoc == BOX) {
        val boxNewLoc = moveItem(newLoc, dir)
        if(boxNewLoc == newLoc) {
            return loc
        }
    }
    this[loc] = EMPTY
    this[newLoc] = item
    return newLoc
}