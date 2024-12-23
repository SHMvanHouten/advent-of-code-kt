package com.github.shmvanhouten.adventofcode2024.day23

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day23Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(listInterconnectedStartingWithT(example).size)
                .isEqualTo(7)
        }

        @Test
        internal fun `part 1`() {
            expectThat(listInterconnectedStartingWithT(input).size)
                .isEqualTo(1358)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(findPassword(example)).isEqualTo("co,de,ka,ta")
        }

        @Test
        internal fun `part 2`() {
            expectThat(findPassword(input)).isEqualTo("cl,ei,fd,hc,ib,kq,kv,ky,rv,vf,wk,yx,zf")
        }
    }

    private val input by lazy { readFile("/input-day23.txt")}
    private val example = """
        kh-tc
        qp-kh
        de-cg
        ka-co
        yn-aq
        qp-ub
        cg-tb
        vc-aq
        tb-ka
        wh-tc
        yn-cg
        kh-ub
        ta-co
        de-co
        tc-td
        tb-wq
        wh-td
        ta-ka
        td-qp
        aq-cg
        wq-ub
        ub-vc
        de-ta
        wq-aq
        wq-vc
        wh-yn
        ka-de
        kh-ta
        co-tc
        wh-qp
        tb-vc
        td-yn
    """.trimIndent()
}
