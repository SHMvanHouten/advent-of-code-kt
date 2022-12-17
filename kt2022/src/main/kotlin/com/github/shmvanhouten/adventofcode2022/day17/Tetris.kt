package com.github.shmvanhouten.adventofcode2022.day17

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

// Y is descending! so -y means going down, +y means going up!

enum class Rock(val coordinates: List<Coordinate>) {
    BEAM(listOf(Coordinate(2, 0),Coordinate(3, 0), Coordinate(4, 0), Coordinate(5, 0))),
    PLUS(listOf(Coordinate(3, 0),Coordinate(2, 1), Coordinate(3, 1), Coordinate(4, 1), Coordinate(3,2))),
    BACKL(listOf(Coordinate(2, 0),Coordinate(3, 0), Coordinate(4, 0), Coordinate(4, 1), Coordinate(4,2))),
    POST(listOf(Coordinate(2,0), Coordinate(2,1), Coordinate(2,2), Coordinate(2,3))),
    BLOCK(listOf(Coordinate(2,0), Coordinate(3,0), Coordinate(2,1), Coordinate(3,1)));

    fun next(): Rock {
        val values = Rock.values()
        val nextOrdinal = (this.ordinal + 1) % values.size
        return values[nextOrdinal]
    }

}
class Cavern {
    private val cavern = mutableSetOf<Coordinate>()
    fun simulate(gasJets: String, nrOfRocks: Int): Set<Coordinate> {
        var remainingJets = gasJets

        var currentShape = Rock.BEAM
        repeat(nrOfRocks) {
            val (rock, jets) = dropRock(currentShape.coordinates, remainingJets)
            remainingJets = jets
            if(remainingJets.length < gasJets.length) remainingJets += gasJets
            cavern += rock
            currentShape = currentShape.next()
        }
        return cavern
    }

    private fun dropRock(
        nextRock: List<Coordinate>,
        gasJets: String
    ): Pair<List<Coordinate>, String> {
        var remainingJets = gasJets
        val startingY = getHighestBlock(cavern) + 4
        var rock = nextRock
        rock = rock.moveUp(startingY)
        rock = freeFall(remainingJets.substring(0, 4), rock)
        remainingJets = remainingJets.substring(4)

        return dropUntilBottomIsHit(rock, remainingJets)
    }

    private fun freeFall(
        gasJets: String,
        rock: List<Coordinate>
    ): List<Coordinate> {
        var rock1 = rock
        for (gasJet in gasJets.substring(0, gasJets.lastIndex)) {
            rock1 = rock1.move(gasJet)
            rock1 = rock1.fall()
        }
        return rock1.move(gasJets.last())
    }

    private fun dropUntilBottomIsHit(
        startingRock: List<Coordinate>,
        jets: String
    ): Pair<List<Coordinate>, String> {
        var rock = startingRock
        var remainingJets = jets
        while (true) {
            val newRock = rock.fall()
            if (newRock.any { it.y == 0 || cavern.contains(it) }) return rock to remainingJets
            else rock = newRock

            rock = rock.move(remainingJets.first())
            remainingJets = remainingJets.substring(1)
        }
    }

    private fun List<Coordinate>.move(gasJet: Char): List<Coordinate> {
        return when (gasJet) {
            '<' -> this.moveLeft()
            '>' -> this.moveRight()
            else -> error("unknown direction $gasJet")
        }
    }

    private fun List<Coordinate>.moveRight(): List<Coordinate> {
        if (maxOf { it.x } >= 6) return this
        val moved = map { it.copy(x = it.x + 1) }
        return if(moved.any { cavern.contains(it) }) this
        else moved
    }

    private fun List<Coordinate>.moveLeft(): List<Coordinate> {
        if (minOf { it.x } == 0) return this
        val moved = map { it.copy(x = it.x - 1) }
        return if(moved.any { cavern.contains(it) }) this
        else moved
    }

    private fun List<Coordinate>.moveUp(upBy: Int): List<Coordinate> {
        return map { it.copy(y = it.y + upBy) }.toList()
    }

    private fun List<Coordinate>.fall(): List<Coordinate> {
        return map { it.copy(y = it.y - 1) }
    }

    private fun getHighestBlock(cavern: MutableSet<Coordinate>): Int {
        return if (cavern.isEmpty()) 0
        else cavern.maxOf { it.y }
    }
}
