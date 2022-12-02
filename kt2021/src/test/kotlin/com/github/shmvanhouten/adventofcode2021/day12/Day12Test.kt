package com.github.shmvanhouten.adventofcode2021.day12

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode2021.day12.RuleSet.PART_2
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
            assertThat(findAllPathsThroughCave(input).size, equalTo(10))
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
            assertThat(findAllPathsThroughCave(input).size, equalTo(19))
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
            assertThat(findAllPathsThroughCave(input).size, equalTo(226))
        }

        @Test
        internal fun `part 1`() {
            val input = input.lines()
            assertThat(findAllPathsThroughCave(input).size, equalTo(4775))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 1`() {
            val input = """start-A
start-b
A-c
A-b
b-d
A-end
b-end""".lines()
            val paths = findAllPathsThroughCave(input, PART_2)
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
            val findAllPathsThroughCave = findAllPathsThroughCave(input, PART_2)
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
            assertThat(findAllPathsThroughCave(input, PART_2).size, equalTo(3509))
        }

        @Test
        internal fun `part 1`() {
            val input = input.lines()
            assertThat(findAllPathsThroughCave(input, PART_2).size, equalTo(152480))
        }
    }

    private val input by lazy { readFile("/input-day12.txt")}

}
