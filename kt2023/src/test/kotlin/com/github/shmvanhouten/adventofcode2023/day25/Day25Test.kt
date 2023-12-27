package com.github.shmvanhouten.adventofcode2023.day25

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.collectors.productOf
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.get
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

class Day25Test {

    private val example = """
        jqt: rhn xhk nvd
        rsh: frs pzl lsr
        xhk: hfx
        cmg: qnr nvd lhk bvb
        rhn: xhk bvb hfx
        bvb: xhk hfx
        pzl: lsr hfx nvd
        qnr: nvd
        ntq: jqt hfx bvb xhk
        nvd: lhk
        lsr: lhk
        rzs: qnr cmg lsr rsh
        frs: qnr lhk lsr
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        fun `sort nodePairs by distance descending`() {
            val result = sortNodePairsByDistanceDescending(example)
            val farthest = result.maxBy { it.value }
            expectThat(farthest.key).isEqualTo("rhn" to "frs")
            expectThat(farthest.value).isEqualTo(4)
        }

        @Test
        fun `for our example, the target nodes are hfx to pzl, bvb to cmg, nvd to jqt`() {
            val result = findNodesToCutBetween(example)
            expect {
                that(result).contains("hfx" to "pzl")
                that(result).contains("bvb" to "cmg")
                that(result).contains("nvd" to "jzt")
            }
        }

        @Test
        fun `for our input, the target nodes are bqq to rxt, btp to qxr, bgl to vfx`() {
            val result = findNodesToCutBetween(input)
            expect {
                that(result).contains("bqq" to "rxt")
                that(result).contains("btp" to "qxr")
                that(result).contains("bgl" to "vfx")
            }
        }

        @Test
        fun `for our other input, the target nodes are znv to ddj, mtq to jtr, pzq to rrz`() {
            val result = findNodesToCutBetween(readFile("/input-day25-backup.txt"))
            expect {
                that(result).contains("znv" to "ddj")
                that(result).contains("mtq" to "jtr")
                that(result).contains("pzq" to "rrz")
            }
        }

        @Test
        internal fun `example 1`() {
            val result = removeConnections(
                example,
                listOf("hfx" to "pzl", "bvb" to "cmg", "nvd" to "jqt")
            )
            expectThat(result)
                .hasSize(2)
            expect {
                that(result).hasSize(9)
                that(result).get(1).hasSize(6)
            }

        }

        @Test
        internal fun `part 1`() {
            // used dot -Kneato -Tsvg day25x.txt > day25x.svg to find the nodes that could be removed
            // todo: solve programatically...
            val result = removeConnections(
                input,
                listOf("bqq" to "rxt", "btp" to "qxr", "bgl" to "vfx")
            )
            expectThat(result).hasSize(2)
            expectThat(result.first().size * result[1].size).isEqualTo(525264)
        }

        @Test
        fun `part 1 for real`() {
            val result = findAndCutIntoTwoGroups(input)
            expectThat(result).hasSize(2)
            expectThat(result.first().size * result[1].size).isEqualTo(525264)
        }

        @Test
        @Disabled("slow")
        fun `part 1 other input for real`() {
            val result = findAndCutIntoTwoGroups(readFile("/input-day25-backup.txt"))
            expectThat(result).hasSize(2)
            expectThat(result.first().size * result[1].size.toLong()).isEqualTo(
                removeConnections(readFile("/input-day25-backup.txt"), listOf("znv" to "ddj", "mtq" to "jtr", "pzq" to "rrz")).productOf { it.size }
            )
        }
    }

    private val input by lazy { readFile("/input-day25.txt")}

}
