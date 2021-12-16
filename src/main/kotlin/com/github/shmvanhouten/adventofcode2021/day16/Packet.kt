package com.github.shmvanhouten.adventofcode2021.day16

open class Packet(open val version: Int)

data class LiteralPacket(override val version: Int, val value: Long) : Packet(version)

data class OperatorPacket(
    override val version: Int,
    val subPackets : List<Packet>
) : Packet(version)