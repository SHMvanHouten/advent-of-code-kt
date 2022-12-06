package com.github.shmvanhouten.adventofcode2022.day06

fun whenIsStarterPacketProcessed(input: String, packetSize: Int): Int {
    return input.windowed(packetSize)
        .indexOfFirst { it.toSet().size == it.length } + packetSize
}