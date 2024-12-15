package com.github.shmvanhouten.adventofcode2024.day15

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import com.github.shmvanhouten.adventofcode.utility.strings.blocks

private val ROBOT = '@'
private val BOX = 'O'
private val LEFT_BOX = '['
private val RIGHT_BOX = ']'
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

fun followInstructionsWidenedGrid(_grid: Grid<Char>, instuctions: String): Grid<Char> {
    val grid = _grid.toMutableGrid()
    var robot = grid.firstLocationOf { it == ROBOT }!!
    instuctions.map { toDirection(it) }.forEach { dir ->
        robot = grid.moveItemWidened(robot, dir)
    }
    return grid
}

fun getGPS(grid: Grid<Char>): Int {
    return grid.coordinatesMatching { it == BOX || it == LEFT_BOX }
        .sumOf { it.x + it.y * 100 }
}

private fun toDirection(char: Char): Direction {
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

private fun MutableGrid<Char>.moveItemWidened(loc: Coordinate, dir: Direction, item: Char = this[loc]): Coordinate {
    if(!canMove(loc, dir)) {
        return loc
    }
    val newLoc = loc.move(dir)
    val itemAtNewLoc = this[newLoc]
    if(itemAtNewLoc == WALL) {
        return loc
    } else if (itemAtNewLoc == LEFT_BOX || itemAtNewLoc == RIGHT_BOX) {
        if(dir == Direction.NORTH || dir == Direction.SOUTH) {
            val otherHalfLoc = getOtherHalf(newLoc, itemAtNewLoc)
            val thisHalfNewLoc = moveItemWidened(newLoc, dir)
            val otherHalfNewLoc = moveItemWidened(otherHalfLoc, dir)
            if(thisHalfNewLoc == newLoc || otherHalfNewLoc == otherHalfLoc) { // either half of box did not move
                return loc
            }
        } else {
            val boxNewLoc = moveItemWidened(newLoc, dir)
            if(boxNewLoc == newLoc) { // box did not move
                return loc
            }
        }
    }
    this[loc] = EMPTY
    this[newLoc] = item
    return newLoc
}

private fun MutableGrid<Char>.canMove(loc: Coordinate, dir: Direction): Boolean {
    val newLoc = loc.move(dir)
    val itemAtNewLoc = this[newLoc]
    if(itemAtNewLoc == WALL) {
        return false
    } else if (itemAtNewLoc == LEFT_BOX || itemAtNewLoc == RIGHT_BOX) {
        if(dir == Direction.NORTH || dir == Direction.SOUTH) {
            val otherHalfLoc = getOtherHalf(newLoc, itemAtNewLoc)
            return canMove(newLoc, dir) && canMove(otherHalfLoc, dir)
        } else {
            return canMove(newLoc, dir)
        }
    } else {
        return true
    }
}



fun getOtherHalf(newLoc: Coordinate, item: Char): Coordinate {
    return when(item) {
        LEFT_BOX -> newLoc.move(Direction.EAST)
        RIGHT_BOX -> newLoc.move(Direction.WEST)
        else -> error("box was not a box $item")
    }
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

fun widenGrid(grid: Grid<Char>): Grid<Char> {
    return Grid(
        grid.rows().map { row ->
            buildList {
                row.forEach { if (it == ROBOT) {
                    add(ROBOT)
                    add(EMPTY)
                } else if(it == BOX) {
                    add(LEFT_BOX)
                    add(RIGHT_BOX)
                } else {
                    add(it)
                    add(it)
                }
                }
            }
        }
    )
}
