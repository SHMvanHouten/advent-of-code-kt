package com.github.shmvanhouten.adventofcode2023.day05

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.ranges.splitOverlapsOnAll
import com.github.shmvanhouten.adventofcode.utility.strings.blocks

fun main() {
    val toAlmanac = toAlmanac(blocks(readFile("/input-day05.txt")))
    toAlmanac.seeds.also(::println)
    toAlmanac.maps
        .onEach(::println)
}

fun blocks(input: String) = input
    .blocks()
    .map { it.lines() }

fun toAlmanac(blocks: List<List<String>>): Almanac {
    val seeds = blocks.first().first().substringAfter("seeds: ").split(" ").map { it.toLong() }
    val instructions = blocks.subList(1, blocks.size)
        .map { toInstructionMap(it) }
    return Almanac(seeds, instructions)
}

fun toAlmanacWithSeedRanges(blocks: List<List<String>>): RangeAlmanac {
    val seeds = blocks.first().first().substringAfter("seeds: ").split(" ")
        .chunked(2)
        .map { (nr, rangeSize) -> nr.toLong()..(nr.toLong() + rangeSize.toLong()) }
    val instructions = blocks.subList(1, blocks.size)
        .map { toInstructionMap(it) }
    return RangeAlmanac(seeds, instructions)
}

fun toInstructionMap(list: List<String>): List<Instructions> {
    return list.subList(1, list.size)
        .map { line -> line.split(' ').map { it.toLong() } }
        .map { (s, d, l) ->Instructions(s, d, l) }

}

data class RangeAlmanac(val seeds: List<LongRange>, val maps: List<List<Instructions>>) {

    fun minLocation(): Long = maps.fold(seeds) { acc, instructions ->
        applyInstructions(acc, instructions)
    }.minOf { it.first }

    private fun applyInstructions(
        ranges: List<LongRange>,
        instructions: List<Instructions>
    ) = ranges.map { it.splitOverlapsOnAll(instructions.sourceRanges()) }
        .map { (overlapping, notOverlapping) ->
            overlapping.map { instructions.applyToRange(it) } + notOverlapping
        }.flatten()
}

data class Almanac(val seeds: List<Long>, val maps: List<List<Instructions>>) {
    fun listLocationNrs(): List<Long> {
        return seeds.map { maps.fold(it) { nr, instructions ->
            instructions.applyTo(nr)
        } }
    }

    fun minLocation(): Long {
        return seeds.minOf {
            maps.fold(it) { nr, instructions ->
                instructions.applyTo(nr)
            }
        }
    }
}

data class Instructions(
    val destRangeStart: Long,
    val sourceRangeStart: Long,
    val rangeLength: Long
) {
    val sourceRange = sourceRangeStart.until(sourceRangeStart + rangeLength)
    private val offset = destRangeStart - sourceRangeStart

    fun applyTo(input: Long): Long {
        return input + offset
    }
}

fun List<Instructions>.applyTo(input: Long): Long {
    val instruction = this.find { input in it.sourceRange }
    return instruction?.applyTo(input) ?: input
}

private fun List<Instructions>.sourceRanges(): List<LongRange> = map { it.sourceRange }

private fun List<Instructions>.applyToRange(matchedRange: LongRange): LongRange =
    applyTo(matchedRange.first)..applyTo(matchedRange.last)