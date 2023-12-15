package com.github.shmvanhouten.adventofcode2023.day15

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day15Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `part 1`() {
            expectThat(input.split(',')
                .sumOf { hash(it) }.also { println(it) }).isEqualTo(510013)
        }
    }

    @Nested
    inner class Part2 {

        val example = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"

        @Test
        internal fun example() {
            val result = putInBoxes(example.split(','))
                .mapIndexed { boxIndex, box ->
                    box.mapIndexed { slot, step ->
                        (boxIndex + 1) * (slot + 1) * step.focalLength
                    }.sum()
                }.sum()
            expectThat(result).isEqualTo(145)
        }

        @Test
        internal fun `part 2`() {
            val result = putInBoxes(input.split(','))
                .mapIndexed { boxIndex, box ->
                    box.mapIndexed { slot, step ->
                        (boxIndex + 1) * (slot + 1) * step.focalLength
                    }.sum()
                }.sum()
            expectThat(result).isEqualTo(268497)
        }
    }

    private val input by lazy { readFile("/input-day15.txt")}

}
