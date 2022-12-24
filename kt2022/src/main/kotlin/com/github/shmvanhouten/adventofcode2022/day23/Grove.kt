package com.github.shmvanhouten.adventofcode2022.day23

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.RelativePosition
import com.github.shmvanhouten.adventofcode.utility.coordinate.RelativePosition.*
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap

data class Grove(
    val elves: Set<Coordinate>,
    val moves: List<MoveProposal> = listOf(
        MoveProposal(NORTH, listOf(WEST, EAST)),
        MoveProposal(SOUTH, listOf(WEST, EAST)),
        MoveProposal(WEST, listOf(NORTH, SOUTH)),
        MoveProposal(EAST, listOf(NORTH, SOUTH))
    )
) {
    constructor(input: String) : this(input.toCoordinateMap('#'))

    fun tick(times: Int): Grove {
        return generateSequence(this, Grove::tick)
            .take(times + 1).last()
    }

    fun tick(): Grove {
        val (newElves, newProposals) = moveElves()
        return this.copy(
            elves = newElves,
            moves = newProposals
        )
    }

    fun tickUntilDone(): Int {
        var grove = this
        var i = 0
        while (true) {
            i++
            val newGrove = grove.tick()
            if(newGrove.elves == grove.elves) return i
            grove = newGrove
        }
    }

    private fun moveElves(): Pair<Set<Coordinate>, List<MoveProposal>> {
        val elvesAsList = elves.toList()
        val proposals = elves.map { proposeMove(it) }
        val validProposals = proposals.mapIndexed { i, newPos ->
            if (proposals.count { it.first == newPos.first } < 2) newPos
            else elvesAsList[i] to null
        }
        val usedMoves = validProposals.mapNotNull { it.second }.toSet()
        return validProposals.map { it.first }.toSet() to nextTurnsMoves(usedMoves)
    }

    private fun nextTurnsMoves(usedMoves: Set<RelativePosition>): List<MoveProposal> {
        val theMoves = moves.toMutableList()
        moves.forEach {
            if (usedMoves.contains(it.direction)) {
                theMoves.removeIf { proposal -> proposal.direction == it.direction }
                theMoves += it
                return theMoves
            }
        }
        return emptyList()
    }

    private fun proposeMove(elf: Coordinate): Pair<Coordinate, RelativePosition?> {
        if (elf.getSurrounding().none { elves.contains(it) }) return elf to null
        return moves.map { elf.move(it.direction) to it }
            .find { (newLoc, proposal) ->
                !elves.contains(newLoc) && proposal.directionsToCheck
                    .none { elves.contains(newLoc.move(it)) }

            }?.let { it.first to it.second.direction }
            ?: (elf to null)
    }
}

data class MoveProposal(val direction: RelativePosition, val directionsToCheck: List<RelativePosition>)