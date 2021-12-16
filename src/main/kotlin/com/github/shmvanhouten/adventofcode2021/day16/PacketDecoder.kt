package com.github.shmvanhouten.adventofcode2021.day16

fun evaluatePacket(hex: String): Packet = evaluatePacketAndSize(hex.toBigInteger(16).toString(2).padZeroStart()).first

fun calculateVersionSum(packet: Packet): Int {
    return when(packet) {
        is LiteralPacket -> packet.version
        is OperatorPacket -> packet.version + packet.subPackets.sumOf { calculateVersionSum(it) }
        else -> error("unknown packet type $packet")
    }
}

fun calculateValueSum(packet: Packet): Long {
    return packet.evaluate()
}

private fun evaluatePacketAndSize(bits: String): Pair<Packet, Int> {
    return if (bits.substring(3, 6) == "100")
        evaluateLiteralPacket(bits)
    else evaluatePacketOperatorPacket(bits)
}

private fun evaluateLiteralPacket(bits: String): Pair<LiteralPacket, Int> {
    val builder = StringBuilder()
    var i = 0
    val version = bits.substring(i, i + 3).toInt(2)
    i += 6 // skip id
    var isLastByte = false
    while (!isLastByte) {
        isLastByte = bits[i++] == '0'
        builder.append(bits.substring(i, i + 4))
        i += 4
    }
    return LiteralPacket(version, builder.toString().toLong(2)) to i
}

private fun evaluatePacketOperatorPacket(bits: String): Pair<OperatorPacket, Int> {
    var i = 0
    val version = bits.substring(i, i + 3).toInt(2)
    i += 3
    val id = bits.substring(i, i + 3)
    i += 3
    val lengthTypeId = bits[i]
    i++
    if (lengthTypeId == '0') {
        val expectedSize = bits.substring(i, i + 15).toInt(2)
        i += 15
        val packets = evaluateInnerPackets(bits.substring(i, i + expectedSize))
        i += expectedSize
        return OperatorPacket(id.toInt(2), version, packets) to i
    } else {
        val expectedAmount = bits.substring(i, i + 11).toInt(2)
        i += 11
        val (packets, remaining) = evaluateInnerPackets(bits.substring(i), expectedAmount)
        i += remaining
        return OperatorPacket(id.toInt(2), version, packets) to i
    }

}

fun evaluateInnerPackets(bits: String, expectedAmount: Int): Pair<List<Packet>, Int> {
    var packetsLeft = expectedAmount
    var i = 0
    val packets = mutableListOf<Packet>()
    while (packetsLeft > 0) {
        val (packet, packetSize) = evaluatePacketAndSize(bits.substring(i))
        i += packetSize
        packets += packet
        packetsLeft--
    }
    return packets to i
}

fun evaluateInnerPackets(bits: String): MutableList<Packet> {
    var i = 0
    val packets = mutableListOf<Packet>()
    while (i < bits.length) {
//        if (bits.substring(i).toInt(2) == 0) error("put this in while!")
        val (packet, remaining) = evaluatePacketAndSize(bits.substring(i))
        packets += packet
        i += remaining
    }
    return packets
}

fun String.padZeroStart(): String {
    val remainder = this.length % 4
    return if(remainder == 0) this
    else 0.until(4 - remainder).map { '0' }.joinToString("") + this
}
