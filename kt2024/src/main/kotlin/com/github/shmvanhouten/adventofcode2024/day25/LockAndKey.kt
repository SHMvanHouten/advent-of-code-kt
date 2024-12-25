package com.github.shmvanhouten.adventofcode2024.day25

import com.github.shmvanhouten.adventofcode.utility.strings.blocks

fun countFitting(file: String): Int {
    val (locks, keys) = file
        .blocks()
        .map { parseKeyOrLock(it) }
        .partition { it.isLock }
    return keys.sumOf { key ->
        locks.count { lock -> key.fits(lock) }
    }
}

fun parseKeyOrLock(block: String): KeyOrLock {
    return KeyOrLock(countPinsPerColumn(block), block.first() == '#')
}

data class KeyOrLock(
    val columns: List<Int>,
    val isLock: Boolean
) {
    fun fits(lock: KeyOrLock): Boolean {
        assert(this.isLock != lock.isLock)
        return columns.zip(lock.columns)
            .all { (kc, lc) -> kc + lc <= 5 }
    }
}

fun countPinsPerColumn(input: String): List<Int> {
    val grid = input.lines()
    val columns = mutableMapOf<Int, Int>()
    grid.forEach { row ->
        row.forEachIndexed { x, element ->
            if (element == '#') columns.merge(x, 1, Int::plus)
        }
    }
    return columns.entries.sortedBy { it.key }.map { it.value - 1 }
}