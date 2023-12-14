package com.github.shmvanhouten.adventofcode2023.day14

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid

fun spinSmart(_grid: Grid<Char>, times: Int): Grid<Char> {
    val grid = _grid.toMutableGrid()
    val previousStates = mutableListOf<String>()
    var i = 0
    var currentState = ""
    while (!previousStates.contains(currentState)) {
        previousStates += currentState
        spin(grid)
        currentState = grid.toString()
        i++
    }
    val firstInstanceOfRepeating = previousStates.indexOf(currentState)
    val repeatCycle = i - firstInstanceOfRepeating
    val spinsLeft = (times - firstInstanceOfRepeating) % repeatCycle
    spin(grid, spinsLeft)
    return grid
}

fun spin(grid: MutableGrid<Char>, times: Int = 1): Grid<Char> {

    repeat(times) {
        grid.tiltNorth()
        grid.tiltWest()
        grid.tiltSouth()
        grid.tiltEast()
    }
    return grid
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

fun MutableGrid<Char>.tiltWest(): Grid<Char> {
    rows().forEachIndexed { y, row ->
        var highestAvailable = 0
        row.forEachIndexed { x, c ->
            if(c == 'O') {
                val coordinate = Coordinate(x, y)
                this[coordinate] = '.'
                this[Coordinate(highestAvailable++, y)] = 'O'
            } else if(c == '#') {
                highestAvailable = x + 1
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

fun MutableGrid<Char>.tiltEast(): Grid<Char> {
    rows().forEachIndexed { y, row ->
        var highestAvailable = this.width - 1
        row.withIndex().reversed().forEach { (x, c) ->
            if(c == 'O') {
                this[Coordinate(x, y)] = '.'
                this[Coordinate(highestAvailable--, y)] = 'O'
            } else if(c == '#') {
                highestAvailable = x - 1
            }
        }
    }
    return this
}

fun Grid<Char>.calculateLoad(): Int {
    return this.withIndex().filter { it.item == 'O' }
        .sumOf { this.height - it.location.y }
}
