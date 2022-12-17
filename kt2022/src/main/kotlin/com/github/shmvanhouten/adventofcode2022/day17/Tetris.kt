package com.github.shmvanhouten.adventofcode2022.day17

import com.github.shmvanhouten.adventofcode.utility.collectors.extremes
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

// Y is descending! so -y means going down, +y means going up!

enum class Shape(val rock: Rock) {
    BEAM(Rock(Coordinate(2, 0),Coordinate(3, 0), Coordinate(4, 0), Coordinate(5, 0))),
    PLUS(Rock(Coordinate(3, 0),Coordinate(2, 1), Coordinate(3, 1), Coordinate(4, 1), Coordinate(3,2))),
    BACKL(Rock(Coordinate(2, 0),Coordinate(3, 0), Coordinate(4, 0), Coordinate(4, 1), Coordinate(4,2))),
    POST(Rock(Coordinate(2,0), Coordinate(2,1), Coordinate(2,2), Coordinate(2,3))),
    BLOCK(Rock(Coordinate(2,0), Coordinate(3,0), Coordinate(2,1), Coordinate(3,1)));

    fun next(): Shape {
        val values = Shape.values()
        val nextOrdinal = (this.ordinal + 1) % values.size
        return values[nextOrdinal]
    }

}

class Cavern {
    private val cavern = mutableSetOf<Coordinate>()
    fun simulate(gasJets: String, nrOfRocks: Int, untilRepeats: String? = null): Set<Coordinate> {
        var remainingJets = gasJets

        var currentShape = Shape.BEAM
        var hasPrinted = false
        repeat(nrOfRocks) {i ->
            val (rock, jets) = dropRock(currentShape.rock, remainingJets)
            remainingJets = jets
            if(remainingJets.length < gasJets.length) remainingJets += gasJets
            cavern += rock.rock
            currentShape = currentShape.next()

            if(untilRepeats != null) {
                val drawing = draw(cavern)
                if (!hasPrinted && drawing.contains(untilRepeats)) {
                    hasPrinted = true
                    println("first time block contains: ${i + 1}")
                }
                if (drawing.containsTwice(untilRepeats)) {
                    println("finished at rock ${i + 1}")
                    return cavern
                }
            }
        }
        return cavern
    }

    private fun dropRock(
        nextRock: Rock,
        gasJets: String
    ): Pair<Rock, String> {
        val startingY = getHighestBlock() + 4

        val rock = nextRock
            .moveUp(startingY)
             .freeFall(gasJets.substring(0, 4), cavern)

        return dropUntilBottomIsHit(rock, gasJets.substring(4))
    }

    private fun dropUntilBottomIsHit(
        startingRock: Rock,
        jets: String
    ): Pair<Rock, String> {
        var rock = startingRock
        var remainingJets = jets
        while (true) {
            val newRock = rock.fall()
            if (newRock.rock.any { it.y == 0 || cavern.contains(it) }) return rock to remainingJets
            else rock = newRock

            rock = rock.move(remainingJets.first(), cavern)
            remainingJets = remainingJets.substring(1)
        }
    }

    private fun getHighestBlock(): Int {
        return if (cavern.isEmpty()) 0
        else cavern.maxOf { it.y }
    }
}

private fun String.containsTwice(untilRepeats: String): Boolean {
    val firstTimeRepeat = indexOf(untilRepeats)
    return if(firstTimeRepeat != -1) {
        substring(firstTimeRepeat + 1).contains(untilRepeats)
    } else false
}

fun draw(coordinates: Collection<Coordinate>, hit: Char = '#', miss: Char = '.'): String {
    val (minY, maxY) = coordinates.map { it.y }.extremes() ?: error("empty collection $coordinates")
    val (minX, maxX) = 0 to 6
    return maxY.downTo(minY).joinToString("\n") { y ->
        (minX..maxX).map { x ->
            if (coordinates.contains(Coordinate(x, y))) hit
            else miss
        }.joinToString("", prefix = "|", postfix = "|")
    }
}

data class Rock(
    val rock: List<Coordinate>
) {
    constructor(vararg coordinates: Coordinate): this(coordinates.toList())

    fun move(gasJet: Char, cavern: Set<Coordinate>): Rock {
        return when (gasJet) {
            '<' -> this.moveLeft(cavern)
            '>' -> this.moveRight(cavern)
            else -> error("unknown direction $gasJet")
        }
    }

    fun freeFall(
        gasJets: String,
        cavern: MutableSet<Coordinate>
    ): Rock {
        var rock = this
        for (gasJet in gasJets.substring(0, gasJets.lastIndex)) {
            rock = rock.move(gasJet, cavern)
            rock = rock.fall()
        }
        return rock.move(gasJets.last(), cavern)
    }

    fun moveUp(upBy: Int): Rock {
        return Rock(rock.map { it.copy(y = it.y + upBy) })
    }

    fun fall(): Rock {
        return Rock(rock.map { it.copy(y = it.y - 1) })
    }

    private fun moveRight(cavern: Set<Coordinate>): Rock {
        if (rock.maxOf { it.x } >= 6) return this
        val moved = rock.map { it.copy(x = it.x + 1) }
        return if(moved.any { cavern.contains(it) }) this
        else Rock(moved)
    }

    private fun moveLeft(cavern: Set<Coordinate>): Rock {
        if (rock.minOf { it.x } == 0) return this
        val moved = rock.map { it.copy(x = it.x - 1) }
        return if(moved.any { cavern.contains(it) }) this
        else Rock(moved)
    }
}
