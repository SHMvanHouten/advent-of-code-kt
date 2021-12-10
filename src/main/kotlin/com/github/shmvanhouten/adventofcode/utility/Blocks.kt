package com.github.shmvanhouten.adventofcode.utility

fun String.blocks(): List<String> {
    return this.split("\n\n")
}