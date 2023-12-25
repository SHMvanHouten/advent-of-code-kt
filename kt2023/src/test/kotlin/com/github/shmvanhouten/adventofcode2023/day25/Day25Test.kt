package com.github.shmvanhouten.adventofcode2023.day25

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
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
    }

    private val input by lazy { readFile("/input-day25.txt")}

}
