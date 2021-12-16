package com.github.shmvanhouten.adventofcode2021.day16

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day16Test {

    @Nested
    inner class PadZeroStart {

    }

    @Nested
    inner class Part1 {

        @Test
        internal fun `packets with ID 4 represent a literal value`() {
            val packet = evaluatePacket("D2FE28")

            // todo: how to write Matcher?
            assertThat(packet is LiteralPacket, equalTo(true))
        }

        @Test
        internal fun `packets with an ID other than 4 represent an operator packet`() {
            val packet = evaluatePacket("38006F45291200")

            assertThat(packet is LiteralPacket, equalTo(false))
            assertThat(packet is OperatorPacket, equalTo(true))
        }

        @Nested
        inner class `All packets` {
            @Test
            internal fun `have the version in the first 3 bits`() {
                assertThat(evaluatePacket("D2FE28").version, equalTo(6))
                assertThat(evaluatePacket("38006F45291200").version, equalTo(1))
            }
        }

        @Nested
        inner class `A literal packet` {
            @Test
            internal fun `contains only one byte of information if the first data byte starts with a 0`() {
                val hex = "101-100-01010-0".filter { it != '-' }.toInt(2).toString(16)
                //         VVV-III-aAAAA-
                // 0xb14
                val packet = evaluatePacket(hex) as LiteralPacket
                assertThat(packet.value, equalTo(10))
                val hex2 = "101-100-00111-0".filter { it != '-' }.toInt(2).toString(16)
                val packet2 = evaluatePacket(hex2) as LiteralPacket
                assertThat(packet2.value, equalTo(7))
            }

            @Test
            internal fun `contains multiple bytes of data as long as the starting bit of each each segment is 1`() {
                val hex = "101-100-11010-00101".filter { it != '-' }.toInt(2).toString(16)
                val packet = evaluatePacket(hex) as LiteralPacket
                assertThat(packet.value, equalTo("10100101".toLong(2)))

                val example1 = "D2FE28"
                val packet2 = evaluatePacket(example1) as LiteralPacket
                assertThat(packet2.value, equalTo(2021))
            }
        }

        @Nested
        inner class An_operator_packet {
            @Test
            internal fun `if the length type id = 0, contains multiple packets of the length indicated`() {
                val example2 = "38006F45291200"
                val packet = evaluatePacket(example2) as OperatorPacket
                assertThat(packet.subPackets, !isEmpty)
                assertThat(packet.subPackets.size, equalTo(2))
                assertThat((packet.subPackets[0] as LiteralPacket).value, equalTo(10))
                assertThat((packet.subPackets[1] as LiteralPacket).value, equalTo(20))
            }

            @Test
            internal fun `if the length type id = 1, contains multiple packets of the amount indicated`() {
                val example3 = "EE00D40C823060"
                val packet = evaluatePacket(example3) as OperatorPacket
                assertThat(packet.subPackets, !isEmpty)
                assertThat(packet.subPackets.size, equalTo(3))
                assertThat((packet.subPackets[0] as LiteralPacket).value, equalTo(1))
                assertThat((packet.subPackets[1] as LiteralPacket).value, equalTo(2))
                assertThat((packet.subPackets[2] as LiteralPacket).value, equalTo(3))
            }
        }

        @Test
        internal fun `example 4`() {
            val hex = "8A004A801A8002F478"
            val packet = evaluatePacket(hex) as OperatorPacket
            assertThat(packet.subPackets.size, equalTo(1))
            val innerPacket1 = packet.subPackets.first() as OperatorPacket
            assertThat(innerPacket1.subPackets.size, equalTo(1))
            val innerPacket2 = innerPacket1.subPackets.first() as OperatorPacket
            assertThat(innerPacket2.subPackets.size, equalTo(1))
            val innerPacket3 = innerPacket2.subPackets.first() as LiteralPacket
            assertThat(innerPacket3.version, equalTo(6))
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "8A004A801A8002F478, 16",
                "620080001611562C8802118E34, 12",
                "C0015000016115A2E0802F182340, 23",
                "A0016C880162017C3686B18A3D4780, 31"
            ]
        )
        internal fun `examples values equate to version sums`(
            hex: String,
            expectedVersionSum: Int
        ) {
            val packet = evaluatePacket(hex)
            assertThat(calculateVersionSum(packet), equalTo(expectedVersionSum))
        }

        @Test
        internal fun `part 1`() {
            val packet = evaluatePacket(input)
            assertThat(calculateVersionSum(packet), equalTo(875))
        }
    }

    @Nested
    inner class Part2 {

        @ParameterizedTest
        @CsvSource(
            value = [
                "C200B40A82, 3",
                "04005AC33890, 54",
                "880086C3E88112, 7",
                "CE00C43D881120, 9",
                "D8005AC2A8F0, 1",
                "F600BC2D8F, 0",
                "9C005AC2F8F0, 0",
                "9C0141080250320F1802104A08, 1"
            ]
        )
        internal fun `examples values equate to value sums`(
            hex: String,
            expectedValueSum: Long
        ) {
            val packet = evaluatePacket(hex)
            assertThat(packet.evaluate(), equalTo(expectedValueSum))
        }

        @Test
        internal fun `part 2`() {
            val packet = evaluatePacket(input)
            assertThat(packet.evaluate(), equalTo(1264857437203))
        }
    }

    private val input by lazy { readFile("/input-day16.txt") }

}
