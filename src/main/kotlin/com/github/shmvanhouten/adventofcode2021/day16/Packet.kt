package com.github.shmvanhouten.adventofcode2021.day16

abstract class Packet(open val version: Int) {
    abstract fun evaluate(): Long
    fun isGreaterThan(other: Packet): Long {
        return if(this.evaluate() > other.evaluate()) 1
        else 0
    }

    fun isLessThan(other: Packet): Long {
        return if(this.evaluate() < other.evaluate()) 1
        else 0
    }

    fun isEqualTo(other: Packet): Long {
        return if(this.evaluate() == other.evaluate()) 1
        else 0
    }
}

data class LiteralPacket(override val version: Int, val value: Long) : Packet(version) {
    override fun evaluate(): Long {
        return value
    }
}

data class OperatorPacket(
    val id: Int,
    override val version: Int,
    val subPackets : List<Packet>
) : Packet(version) {
    override fun evaluate(): Long {
        return when(id) {
            0 -> subPackets.sumOf { it.evaluate() }
            1 -> subPackets.map { it.evaluate() }.reduce(Long::times)
            2 -> subPackets.minOf { it.evaluate() }
            3 -> subPackets.maxOf { it.evaluate() }
            5 -> subPackets.first().isGreaterThan(subPackets[1])
            6 -> subPackets.first().isLessThan(subPackets[1])
            7 -> subPackets.first().isEqualTo(subPackets[1])
            else -> error("unknow version")
        }
    }
}