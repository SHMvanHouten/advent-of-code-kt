package com.github.shmvanhouten.adventofcode2022.day02

import com.github.shmvanhouten.adventofcode2022.day02.Result.*
import com.github.shmvanhouten.adventofcode2022.day02.Shape.*

class RockPaperScissors(private val input: String) {
    fun scorep1(): Long = input.lines()
        .map { it.words() }
        .sumOf { (opponent, me ) ->
            score(
                opponent.toShape(),
                me.toShape()
            )
        }

    fun scorep2(): Long = input.lines()
        .map { it.words() }
        .sumOf { (opponent, requiredResult) ->
            score(opponent.toShape(), requiredResult.toResult())
        }

    private fun score(opponent: Shape, me: Shape): Long {
        return me.value() + me.versus(opponent).score()
    }

    private fun score(elf: Shape, requiredResult: Result): Long {
        return requiredResult.score() + findRequiredShapeForMe(elf, requiredResult).value()
    }

    private fun findRequiredShapeForMe(opponentsShape: Shape, requiredResult: Result): Shape {
        return when (requiredResult) {
            DRAW -> opponentsShape
            WIN -> opponentsShape.superiorShape()
            LOSS -> opponentsShape.inferiorShape()
        }
    }

}

enum class Result {
    LOSS,
    DRAW,
    WIN;

    fun score(): Long {
        return when(this) {
            LOSS -> 0
            DRAW -> 3
            WIN -> 6
        }
    }
}

private enum class Shape {
    ROCK,
    PAPER,
    SCISSORS;

    fun isSuperiorTo(other: Shape): Boolean {
        return this.inferiorShape() == other
    }
    fun superiorShape(): Shape {
        return when(this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }
    }

    fun inferiorShape(): Shape {
        return when(this) {
            PAPER -> ROCK
            SCISSORS -> PAPER
            ROCK -> SCISSORS
        }
    }

    fun value(): Long {
        return this.ordinal.toLong() + 1
    }

    fun versus(opponent: Shape): Result {
        return if(opponent == this) DRAW
        else if(opponent.isSuperiorTo(this)) LOSS
        else WIN
    }
}

private fun String.toResult(): Result {
    return when(this) {
        "X" -> LOSS
        "Y" -> DRAW
        "Z" -> WIN
        else -> error("unknown $this")
    }
}

private fun String.toShape(): Shape {
    return when (this) {
        "X", "A" -> ROCK
        "Y", "B" -> PAPER
        "Z", "C" -> SCISSORS
        else -> error("unknown shape $this")
    }
}

private fun String.words(): List<String> {
    return this.split(' ')
}
