package com.github.shmvanhouten.adventofcode2023.day15

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile

fun main() {
    readFile("/input-day15.txt")
        .split(',')
        .onEach(::println)
        .sumOf { hash(it) }.also { println(it) }
}

fun hash(input: String): Int {
    return input.map { it.code }
        .fold(0){acc, n ->
            (acc + n) * 17 % 256
        }
}
