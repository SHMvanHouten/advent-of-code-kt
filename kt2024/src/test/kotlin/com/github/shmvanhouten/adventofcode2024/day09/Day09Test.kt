package com.github.shmvanhouten.adventofcode2024.day09

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

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
            expectThat(checkSum(merged)).isEqualTo(1928)
        }

        @Test
        internal fun `part 1`() {
            val merged = mergeToSingleBlock(toFileBlocks(input))
            val checkSum = checkSum(merged)
            expectThat(checkSum).isEqualTo(6288599492129)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            expectThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            expectThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day09.txt")}

}
