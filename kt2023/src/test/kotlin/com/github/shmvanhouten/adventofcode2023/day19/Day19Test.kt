package com.github.shmvanhouten.adventofcode2023.day19

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class Day19Test {

    val example = """
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}

        {x=787,m=2655,a=1222,s=2876}
        {x=1679,m=44,a=2067,s=496}
        {x=2036,m=264,a=79,s=2244}
        {x=2461,m=1339,a=466,s=291}
        {x=2127,m=1623,a=2188,s=1013}
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        internal fun `to workflow`() {
            expectThat(toWorkFlow("in{s<1351:px,qqz}")).isEqualTo(WorkFlow("in", "s<1351:px,qqz"))
        }

        @Test
        fun `to part`() {
            expectThat(toPart("{x=787,m=2655,a=1222,s=2876}")).isEqualTo(Part(787, 2655, 1222, 2876))
        }

        @Test
        fun `s matching gets sent to first option`() {
            val workFlow = WorkFlow("in", "s<2:px,qqz")
            val part = Part(x = 0, m = 0, a = 0, s = 1)
            expectThat(workFlow.applyTo(part))
                .isEqualTo("px")
        }

        @Test
        fun `a not matching gets sent to first option`() {
            val workFlow = WorkFlow("in", "a<2:px,qqz")
            val part = Part(x = 0, m = 0, a = 5, s = 0)
            expectThat(workFlow.applyTo(part))
                .isEqualTo("qqz")
        }

        @Test
        fun `chained comparison`() {
            val workFlow = toWorkFlow("px{a<4:qkq,m>2:A,rfg}")
            val part = Part(x = 0, m = 3, a = 5, s = 0)
            expectThat(workFlow.applyTo(part))
                .isEqualTo("A")
        }

        @Test
        fun `chained comparison first true`() {
            val workFlow = toWorkFlow("px{a<4:qkq,m>2:A,rfg}")
            val part = Part(x = 0, m = 3, a = 3, s = 0)
            expectThat(workFlow.applyTo(part))
                .isEqualTo("qkq")
        }

        @Test
        fun `longer chained comparison `() {
            val workFlow = toWorkFlow("bcs{s>4:lxs,x<2:R,m<2:slv,A}")
            val part = Part(x = 3, m = 1, a = 3, s = 3)
            expectThat(workFlow.applyTo(part))
                .isEqualTo("slv")
        }

        @Test
        fun `Accepted parts are 0, 2, 4`() {
            val (workFlows, parts) = parse(example)

            expect {
                that(workFlows.applyTo(parts[0])).isTrue()
                that(workFlows.applyTo(parts[1])).isFalse()
                that(workFlows.applyTo(parts[2])).isTrue()
                that(workFlows.applyTo(parts[3])).isFalse()
                that(workFlows.applyTo(parts[4])).isTrue()
            }

        }

        @Test
        fun `example 1`() {
            val (workFlows, parts) = parse(example)

            expectThat(parts.filter { workFlows.applyTo(it) }.sumOf { it.value() })
                .isEqualTo(19114)
        }

        @Test
        internal fun `part 1`() {
            val (workFlows, parts) = parse(input)
            expectThat(parts.filter { workFlows.applyTo(it) }.sumOf { it.value() })
                .isEqualTo(374873)
        }
    }

    @Nested
    inner class Part2 {

        private val exampleWorkFlows = """
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}
        """.trimIndent().lines().let { toWorkFlows(it) }.associateBy { it.id }.mapValues { it.value.workFlow }
        // s < 1351 && a >= 2006 && m > 2090 -> accepted (x = 1..4000)
        //    =  1350 * (4000 - 2006) * (4000 - 2091) * 3999
        // s < 1351 && a >= 2006 && m <= 2090 && s >= 537 && x <= 2440 -> accepted

        @Test
        internal fun `example 1`() {
            val ranges = permuteThrough(startingRange(), exampleWorkFlows, exampleWorkFlows["in"]!!)
            expectThat(ranges.filter { it.accepted!! }.sumOf { it.count() }).isEqualTo(167409079868000)
        }

        @Test
        internal fun `part 2`() {
            val workFlows = toWorkFlows(input.blocks().first().lines()).associateBy { it.id }.mapValues { it.value.workFlow }
            val ranges = permuteThrough(startingRange(), workFlows, workFlows["in"]!!)
            expectThat(ranges.filter { it.accepted!! }.sumOf { it.count() }).isEqualTo(122112157518711)
        }
    }

    private val input by lazy { readFile("/input-day19.txt")}

}
