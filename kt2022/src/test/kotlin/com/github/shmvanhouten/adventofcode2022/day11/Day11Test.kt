package com.github.shmvanhouten.adventofcode2022.day11

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day11Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun parse() {
            val monkeys = parse(example)

            assertThat(monkeys).hasSize(4)
            val firstMonkey = monkeys.first()
            assertThat(firstMonkey.items).hasSize(2)
            assertThat(firstMonkey.items).contains(79, 98)
            assertThat(firstMonkey.inspect(22)/22).isEqualTo(19)
            assertThat(monkeys[2].inspect(5)).isEqualTo(5 * 5)
            assertThat(firstMonkey.test).isEqualTo(23L)
            assertThat(firstMonkey.trueMonkeyIndex).isEqualTo(2)
            assertThat(firstMonkey.falseMonkeyIndex).isEqualTo(3)
        }

        @Test
        internal fun `after one round first monkey has 4 items`() {
            val monkeys = parse(example)

            val playingField = PlayingField(monkeys)
            playingField.playSimpleRounds(1)

            assertThat(playingField.monkeys.first().items).hasSize(4)
            assertThat(playingField.monkeys.first().items).contains(20, 23, 27, 26)
        }

        @Test
        internal fun example() {
            val monkeys = parse(example)

            val playingField = PlayingField(monkeys)
            playingField.playSimpleRounds(20)
            assertThat(playingField.monkeyBusiness()).isEqualTo(10605L)
        }

        @Test
        internal fun `part 1`() {
            val monkeys = parse(input)

            val playingField = PlayingField(monkeys)
            playingField.playSimpleRounds(20)
            assertThat(playingField.monkeyBusiness()).isEqualTo(117624L)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val monkeys = parse(example)

            val playingField = PlayingField(monkeys)
            playingField.playRounds(10000)
            assertThat(playingField.monkeyBusiness()).isEqualTo(2713310158L)
        }

        @Test
        internal fun `part 2`() {
            val monkeys = parse(input)

            val playingField = PlayingField(monkeys)
            playingField.playRounds(10000)
            assertThat(playingField.monkeyBusiness()).isEqualTo(16792940265L)
        }
    }

    private val input by lazy { readFile("/input-day11.txt")}
    private val example = """
        Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
    """.trimIndent()

}
