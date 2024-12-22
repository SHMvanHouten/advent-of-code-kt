package com.github.shmvanhouten.adventofcode2024.day22

fun findMostBananas(inputs: List<Long>): Int {
    val sequencesToBananas = inputs.map { associatedChanges(it) }
        .map { firstOccurrenceOfEachSequenceToBananasBought(it) }
    val allSequences = sequencesToBananas.flatten().map { it.first }.distinct()
    val eachInputsSequenceToBananas = sequencesToBananas.map { it.toMap() }
    return allSequences.maxOf { seq -> eachInputsSequenceToBananas.sumOf { it[seq]?:0 } }
}

fun firstOccurrenceOfEachSequenceToBananasBought(subList: List<Pair<Int, Int>>): List<Pair<List<Int>, Int>> {
    val foundSequences = mutableMapOf<List<Int>, Int>()
    subList.windowed(4).forEach {
        val changesSequence = it.map { it.second }
        if(!foundSequences.contains(changesSequence)) {
            foundSequences[changesSequence] = it.last().first
        }
    }
    return foundSequences.toList()
}

fun associatedChanges(input: Long): List<Pair<Int, Int>> {
    return calculateNumbersSequence(input)
        .map { it % 10 }
        .map { it.toInt() }
        .take(2001).windowed(2)
        .map { (old, new) -> new to (new - old) }
        .toList()
}

fun calculateNextNumber(input: Long, times: Int): Long {
    return calculateNumbersSequence(input)
        .drop(times).first()
}

private fun calculateNumbersSequence(input: Long) = generateSequence(input) { calculateNextNumber(it) }

fun calculateNextNumber(input: Long): Long {
    val secret1 = (input * 64).xor(input).prune()
    val secret2 = (secret1 / 32).xor(secret1).prune()
    val secret3 = (secret2 * 2048).xor(secret2).prune()
    return secret3
}

fun Long.prune() = this % 16777216L
