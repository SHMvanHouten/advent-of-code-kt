package com.github.shmvanhouten.adventofcode2021.day16

import com.natpryce.hamkrest.Matcher

fun Packet.isALiteral(): Boolean = this is LiteralPacket
val isALiteralPacket = Matcher(Packet::isALiteral)

fun Packet.isAnOperatorPacket(): Boolean = this is OperatorPacket
val isAnOperatorPacket = Matcher(Packet::isAnOperatorPacket)