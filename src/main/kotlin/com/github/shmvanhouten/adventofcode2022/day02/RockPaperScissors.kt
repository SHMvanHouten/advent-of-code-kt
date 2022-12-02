package com.github.shmvanhouten.adventofcode2022.day02

import com.github.shmvanhouten.adventofcode2022.day02.SHAPE.*

private const val ELF_ROCK = "A"
private const val ELF_PAPER = "B"
private const val ELF_SCISSORS = "C"

private const val ME_ROCK = "X"
private const val ME_PAPER = "Y"
private const val ME_SCISSORS = "Z"


class RockPaperScissors(private val input: String) {
    fun score(): Long {
        return input.lines()
            .map { it.words() }
            .sumOf { score(it[0].toShape(), it[1].toShape()) }
    }
    private fun score(elf: SHAPE, me: SHAPE): Long {
        return me.value() + result(elf, me)
    }

    private fun result(elf: SHAPE, me: SHAPE): Long {
        return if(elf == me) 3
        else if(elf.ordinal == me.next().ordinal) 0
        else 6
    }

}

private enum class SHAPE {
    ROCK,
    PAPER,
    SCISSORS;

    fun next(): SHAPE {
        return when(this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }
    }
}

private fun SHAPE.value(): Long {
    // todo ordinal + 1
    return when(this) {
        ROCK -> 1
        PAPER -> 2
        SCISSORS -> 3
    }
}

private fun String.toShape(): SHAPE {
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
