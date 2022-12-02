package com.github.shmvanhouten.adventofcode2021.day07

import com.github.shmvanhouten.adventofcode2021.average.median
import kotlin.math.abs

fun findMostFuelEfficientAlignment(positions: List<Int>): Pair<Int, Int> {
    val fuelConsumptionByPosition = (positions.minOrNull()!!..positions.maxOrNull()!!).map { alignmentPosition ->
        alignmentPosition to positions.sumOf { abs(alignmentPosition - it) }
    }
    return fuelConsumptionByPosition.minByOrNull { it.second }?: error("No Positions found!")
}

fun findFuelConsumptionOfAlignmentAtMedian(positions: List<Int>): Pair<Int, Int> {
    val median = positions.median()
    val fuelConsumption = positions.sumOf { abs(median - it) }
    return median to fuelConsumption
}

fun findMostFuelEfficientAlignmentAtIncrementalFuelConsumption(positions: List<Int>): Pair<Int, Int> {
    return listFuelConsumptionsForPossibleAlignmentPositions(positions)
        .minByOrNull { it.second }?: error("no alignments found!")
}

private fun listFuelConsumptionsForPossibleAlignmentPositions(positions: List<Int>): List<Pair<Int, Int>> {
    val fuelCosts = (1..positions.maxOrNull()!!).runningFold(0) { initial, next -> initial + next }
    return (positions.minOrNull()!!..positions.maxOrNull()!!).map { alignmentPosition ->
        alignmentPosition to positions.sumOf { fuelCosts[abs(alignmentPosition - it)] }
    }
}