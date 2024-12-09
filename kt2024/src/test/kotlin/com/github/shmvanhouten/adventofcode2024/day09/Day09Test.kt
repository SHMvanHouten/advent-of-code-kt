package com.github.shmvanhouten.adventofcode2024.day09

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan

class Day09Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 0`() {
            val fileBlocks = toFileBlocks("12345")
            expectThat(fileBlocks.joinToString(""))
                .isEqualTo("0..111....22222")
            val merged = mergeToSingleBlock(fileBlocks)
            expectThat(merged.joinToString("")).isEqualTo("022111222")
        }

        @Test
        internal fun `example 1`() {
            val fileBlocks = toFileBlocks("2333133121414131402")
            expectThat(fileBlocks.joinToString(""))
                .isEqualTo("00...111...2...333.44.5555.6666.777.888899")
            val merged = mergeToSingleBlock(fileBlocks)
            expectThat(merged.joinToString("")).isEqualTo("0099811188827773336446555566")
            expectThat(checkSumIds(merged)).isEqualTo(1928)
        }

        @Test
        internal fun `part 1`() {
            val merged = mergeToSingleBlock(toFileBlocks(input))
            val checkSum = checkSumIds(merged)
            expectThat(checkSum).isEqualTo(6288599492129)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 1`() {
            val fileBlocks = toFileBlocks("2333133121414131402")
            val blocks = defrag(fileBlocks)
            expectThat(blocks.joinToString("")).isEqualTo("00992111777.44.333....5555.6666.....8888..")
            expectThat(checkSum(blocks)).isEqualTo(2858)
        }

        @Test
        internal fun `part 2`() {
            val fileBlocks = toFileBlocks(input)
            val blocks = defrag(fileBlocks)
            println(blocks.joinToString(""))
            expectThat(checkSum(blocks)).isGreaterThan(5235269539255)
            expectThat(checkSum(blocks)).isEqualTo(0)
        }
    }

    private val input by lazy { readFile("/input-day09.txt")}

}
