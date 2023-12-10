package com.github.shmvanhouten.adventofcode2023.day10

import com.github.shmvanhouten.adventofcode.utility.grid.Grid

internal fun printGrid(grid: Grid<Tile>) {
    val resetToBlack = "\u001b[30m"
    val yellow = "\u001b[33m"
    val blue = "\u001b[34m"
    val red = "\u001b[31m"
    println(grid.map {
        when (it.identification) {
            TileIdentification.UNIDENTIFIED -> it.tile.toString()
            TileIdentification.PIPE -> "$blue${replace(it.tile)}$resetToBlack"
            TileIdentification.LEFT_OF_PIPE -> "$yellow${replace(it.tile)}$resetToBlack"
            TileIdentification.RIGHT_OF_PIPE -> "$red${replace(it.tile)}$resetToBlack"
        }
    })
}

private fun replace(c: Char) = when(c) {
    '7' -> '┓'
    'J' -> '┛'
    'F' -> '┏'
    'L' -> '┗'
    '|' -> '┃'
    '-' -> '━'
    else -> c
}