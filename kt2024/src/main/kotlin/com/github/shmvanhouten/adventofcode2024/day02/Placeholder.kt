package com.github.shmvanhouten.adventofcode2024.day02

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.words
import kotlin.math.abs

fun main() {
    readFile("/input-day02.txt")
        .lines()
        .onEach(::println)
}

fun String.parse(): List<Int> {
    return words().map { it.toInt() }
}

fun List<Int>.isSafe(): Boolean {
    var prev = this.first()
    var dir: Dir? = null
    for (curr in this.subList(1, this.size)) {
        if(prev == curr) {
            return false
        }
        if(dir == null) {
            dir = if(curr > prev) {
                Dir.UP
            } else {
                Dir.DOWN
            }
        }
        if(dir == Dir.UP && prev > curr) {
            return false
        } else if (dir == Dir.DOWN && prev < curr) {
            return false
        }
        if(abs(abs(prev) - abs(curr)) > 3 ) {
            return false
        }
        prev = curr
    }
    if(this.toSet().size != this.size || !(this.sorted() == this)) {
        println(this)
    }
    return true
}

fun List<Int>.isSafeDampened(): Boolean {

    return List(size) { index ->
        val mutable = toMutableList()
        mutable.removeAt(index)
        mutable
    }.any { it.isSafe() }
}

enum class Dir {
    UP,
    DOWN
}
