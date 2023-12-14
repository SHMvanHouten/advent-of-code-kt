package com.github.shmvanhouten.adventofcode2023.day14

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid

fun main() {
    readFile("/input-day14.txt")
        .lines()
        .onEach(::println)
}

fun tiltNorth(grid: Grid<Char>): Grid<Char> = grid.toMutableGrid().tiltNorth()

fun MutableGrid<Char>.tiltNorth(): Grid<Char> {
    columns().forEachIndexed { x, column ->
        var highestAvailable = 0
        column.forEachIndexed { y, c ->
            if(c == 'O') {
                val coordinate = Coordinate(x, y)
                this[coordinate] = '.'
                this[Coordinate(x, highestAvailable++)] = 'O'
            } else if(c == '#') {
                highestAvailable = y + 1
            }
        }
    }
    return this
}

fun MutableGrid<Char>.tiltSouth(): Grid<Char> {
    columns().forEachIndexed { x, column ->
        var highestAvailable = this.height - 1
        column.withIndex().reversed().forEach { (y, c) ->
            if(c == 'O') {
                val coordinate = Coordinate(x, y)
                this[coordinate] = '.'
                this[Coordinate(x, highestAvailable--)] = 'O'
            } else if(c == '#') {
                highestAvailable = y - 1
            }
        }
    }
    return this
}

fun Grid<Char>.calculateLoad(): Int {
    return this.withIndex().filter { it.item == 'O' }
        .sumOf { this.height - it.location.y }
}
