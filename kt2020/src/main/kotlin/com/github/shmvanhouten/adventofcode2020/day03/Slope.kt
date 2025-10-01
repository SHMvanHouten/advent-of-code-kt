package com.github.shmvanhouten.adventofcode2020.day03

import com.github.shmvanhouten.adventofcode.utility.collectors.productOf
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.boolGridFromPicture

data class Slope(val grid: Grid<Boolean>) {
    fun traverseDown(moveRightPerTick: Int, moveDownPerTick: Int = 1): TraverseResult {
        return grid.rows().withIndex()
            .count { (i, row) ->
                (i % moveDownPerTick == 0) && !row[(i * moveRightPerTick / moveDownPerTick) % grid.width]
            }
            .let { TraverseResult(it) }

    }

    fun multiplyTraversalCombinations(): Long {
        return listOf(
            1 to 1,
            3 to 1,
            5 to 1,
            7 to 1,
            1 to 2
        ).productOf { (right, down) -> traverseDown(right, down).encounteredTrees }
    }

    constructor(input: String): this(boolGridFromPicture(input, '.'))
}

data class TraverseResult(val encounteredTrees: Int)
