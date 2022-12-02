package com.github.shmvanhouten.adventofcode2021.day10

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day10Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `find the first illegal character on a line`() {
            assertThat(firstIllegalCharacter("(]"), equalTo(']'))
            assertThat(firstIllegalCharacter("(}"), equalTo('}'))
            assertThat(firstIllegalCharacter("[(}]"), equalTo('}'))
            assertThat(firstIllegalCharacter("{([(<{}[<>[]}>{[]{[(<()>"), equalTo('}'))
            assertThat(firstIllegalCharacter("[[<[([]))<([[{}[[()]]]"), equalTo(')'))
            assertThat(firstIllegalCharacter("[{[{({}]{}}([{[{{{}}([]"), equalTo(']'))
            assertThat(firstIllegalCharacter("[<(<(<(<{}))><([]([]()"), equalTo(')'))
            assertThat(firstIllegalCharacter("<{([([[(<>()){}]>(<<{{"), equalTo('>'))
        }

        @Test
        internal fun `example 1`() {
            assertThat(
                scoreLinesForSyntaxErrors(exampleInput.lines()),
                equalTo(26397)
            )
        }

        @Test
        internal fun `part 1`() {
            assertThat(
                scoreLinesForSyntaxErrors(input.lines()),
                equalTo(216297)
            )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `list incomplete lines`() {
            assertThat(
                listIncompleteLines(exampleInput.lines()), equalTo(
                    listOf(
                        "[({(<(())[]>[[{[]{<()<>>",
                        "[(()[<>])]({[<{<<[]>>(",
                        "(((({<>}<{<{<>}{[]{[]{}",
                        "{<[[]]>}<{[{[{[]{()[[[]",
                        "<{([{{}}[<[[[<>{}]]]>[]]"
                    )
                )
            )
        }

        @Test
        internal fun `complete the line with the correct characters in order`() {

            assertThat(completeLine("[({(<(())[]>[[{[]{<()<>>"), equalTo("}}]])})]"))
            assertThat(completeLine("[(()[<>])]({[<{<<[]>>("), equalTo(")}>]})"))
            assertThat(completeLine("(((({<>}<{<{<>}{[]{[]{}"), equalTo("}}>}>))))"))
            assertThat(completeLine(")){<[[]]>}<{[{[{[]{()[[[]"), equalTo("]]}}]}]}>"))
            assertThat(completeLine("<{([{{}}[<[[[<>{}]]]>[]]"), equalTo("])}>"))
        }

        @Test
        internal fun `example 2`() {
            assertThat(
                scoreInputForCompleteness(exampleInput.lines()),
                equalTo(288957)
            )
        }

        @Test
        internal fun `part 2`() {
            assertThat(
                scoreInputForCompleteness(input.lines()),
                equalTo(2165057169)
            )
        }
    }

    private val input by lazy { readFile("/input-day10.txt") }
    private val exampleInput = """[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]"""

}
