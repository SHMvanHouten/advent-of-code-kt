package com.github.shmvanhouten.adventofcode2022.day02

import com.github.shmvanhouten.adventofcode2022.day02.RESULT.*
import com.github.shmvanhouten.adventofcode2022.day02.Shape.*

private const val ELF_ROCK = "A"
private const val ELF_PAPER = "B"
private const val ELF_SCISSORS = "C"

private const val ME_ROCK = "X"
private const val ME_PAPER = "Y"
private const val ME_SCISSORS = "Z"


class RockPaperScissors(private val input: String) {
    fun scorep1(): Long {
        return input.lines()
            .map { it.words() }
            .sumOf { scorep1(it[0].findRequiredShapeForMe(), it[1].findRequiredShapeForMe()) }
    }
    private fun scorep1(elf: Shape, me: Shape): Long {
        return me.value() + result(elf, me)
    }

    fun scorep2(): Long {
        return input.lines()
            .map { it.words() }
            .sumOf { scorep2(it[0].findRequiredShapeForMe(), it[1].toRequiredResult()) }
    }

    private fun scorep2(elf: Shape, requiredResult: RESULT): Long {
        return requiredResult.value() + findRequiredShapeForMe(elf, requiredResult).value()
    }

    private fun findRequiredShapeForMe(elf: Shape, requiredResult: RESULT): Shape {
        return when (requiredResult) {
            DRAW -> elf
            WIN -> elf.superior()
            LOSE -> elf.inferior()
        }
    }

    private fun result(elf: Shape, me: Shape): Long {
        return if(elf == me) 3
        else if(elf.ordinal == me.superior().ordinal) 0
        else 6
    }

}

private fun String.toRequiredResult(): RESULT {
    return when(this) {
        "X" -> LOSE
        "Y" -> DRAW
        "Z" -> WIN
        else -> error("unknown $this")
    }
}

enum class RESULT {
    LOSE,
    DRAW,
    WIN
}

private enum class Shape {
    ROCK,
    PAPER,
    SCISSORS;

    fun superior(): Shape {
        return when(this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }
    }

    fun inferior(): Shape {
        return when(this) {
            PAPER -> ROCK
            SCISSORS -> PAPER
            ROCK -> SCISSORS
        }
    }
}

private fun Shape.value(): Long {
    return this.ordinal.toLong() + 1
}

private fun RESULT.value(): Long {
    return when(this) {
        LOSE -> 0
        DRAW -> 3
        WIN -> 6
    }
}

private fun String.findRequiredShapeForMe(): Shape {
    return when(this) {
        ME_ROCK, ELF_ROCK -> ROCK
        ME_PAPER, ELF_PAPER -> PAPER
        ME_SCISSORS, ELF_SCISSORS -> SCISSORS
        else -> error("unknown shape $this")
    }
}

private fun String.words(): List<String> {
    return this.split(' ')
}
