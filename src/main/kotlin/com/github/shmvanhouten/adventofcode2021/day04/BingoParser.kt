package com.github.shmvanhouten.adventofcode2021.day04

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2020.util.blocks

fun toBingoGame(input: String): Pair<List<Long>, BingoGame> {
    val blocks = input.blocks()
    val winningNumbers = blocks[0].split(',').map { it.toLong() }
    val boards = blocks.subList(1, blocks.size)
        .map { toBingoBoard(it) }
    return winningNumbers to BingoGame(boards)
}

fun toBingoBoard(input: String): BingoBoard {
    val cells = input.lines()
        .mapIndexed { x, line ->
            line.split(' ')
                .filter { it.isNotEmpty() }
                .mapIndexed { y, number -> toCell(number, x, y) }
        }
        .flatten()
        .associateBy { it.number }
    return BingoBoard(cells)
}

fun toCell(number: String, x: Int, y: Int) : Cell{
    return Cell(
        Coordinate(x,y),
        number.toLong(),
        false
    )
}
