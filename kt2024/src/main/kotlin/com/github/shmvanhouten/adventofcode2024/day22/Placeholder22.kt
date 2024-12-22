package com.github.shmvanhouten.adventofcode2024.day22

fun calculateNextNumber(input: Long, times: Int): Long {
    return generateSequence(input) { calculateNextNumber(it) }
        .drop(times).first()
}

fun calculateNextNumber(input: Long): Long {
    val secret1 = (input * 64).xor(input).prune()
    val secret2 = (secret1 / 32).xor(secret1).prune()
    val secret3 = (secret2 * 2048).xor(secret2).prune()
    return secret3
}

fun Long.prune() = this % 16777216L
