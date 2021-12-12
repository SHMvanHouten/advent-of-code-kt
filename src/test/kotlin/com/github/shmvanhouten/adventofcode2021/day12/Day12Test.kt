package com.github.shmvanhouten.adventofcode2021.day12

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day12Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val input = """start-A
start-b
A-c
A-b
b-d
A-end
b-end""".lines()
            val paths = findAllPathsThroughCave(input)
            assertThat(paths.size, equalTo(36))
        }

        @Test
        internal fun `example 2`() {
            val input = """dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc""".lines()
            val findAllPathsThroughCave = findAllPathsThroughCave(input)
            println(findAllPathsThroughCave)
            assertThat(findAllPathsThroughCave.size, equalTo(103))
        }

        @Test
        internal fun `example 3`() {
            val input = """fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW""".lines()
            assertThat(findAllPathsThroughCave(input).size, equalTo(3509))
        }

        @Test
        internal fun `part 1`() {
            val input = input.lines()
            assertThat(findAllPathsThroughCave(input).size, equalTo(152480))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1, equalTo(1) )
        }

        @Test
        internal fun `part 2`() {
            assertThat(1, equalTo(1) )
        }
    }

    private val input by lazy { readFile("/input-day12.txt")}

}
