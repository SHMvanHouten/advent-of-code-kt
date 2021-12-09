package com.github.shmvanhouten.adventofcode2021.day08

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode2021.day08.Segment.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day08Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            assertThat(countUniquedigits(exampleInput), equalTo(26) )
        }

        @Test
        internal fun `part 1`() {
            assertThat(countUniquedigits(input), equalTo(514) )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `if a signal contains dab and ab, then d must be connected to the top segment`() {
            // 1   7    4     5      2      3      9       6       0       8
            // ab, abd, abef, bcdef, acdfg, abcdf, abcdef, bcdefg, abcdeg, abcdefg
            val signal = toSignal("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")
            val mapping = findWireToSegmentMappings(signal.input)
            println(mapping)
            assertThat(mapping['d'], equalTo(TOP))
            assertThat(mapping['g'], equalTo(BOTTOM_LEFT))
            assertThat(mapping['e'], equalTo(TOP_LEFT))
            assertThat(mapping['f'], equalTo(MIDDLE))
            assertThat(mapping['a'], equalTo(TOP_RIGHT))
            assertThat(mapping['b'], equalTo(BOTTOM_RIGHT))
            assertThat(mapping['c'], equalTo(BOTTOM))
        }

        @Test
        internal fun `given we know the mappings, we decipher the four digit output`() {
            val signal = toSignal("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")
            assertThat(decipherOutput(signal), equalTo(5353))
        }

        @Test
        internal fun `example 2`() {
            assertThat(exampleInput.lines().map { toSignal(it) }
                .sumOf { decipherOutput(it) },
                equalTo(61229)
            )
        }

        @Test
        internal fun `part 2`() {
            assertThat(input.lines().map { toSignal(it) }
                .sumOf { decipherOutput(it) },
                equalTo(1012272)
            )
        }
    }



    private val input by lazy { readFile("/input-day08.txt")}
    private val exampleInput = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"""

}
