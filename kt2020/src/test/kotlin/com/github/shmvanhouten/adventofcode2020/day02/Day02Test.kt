package com.github.shmvanhouten.adventofcode2020.day02

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode2020.day02.PasswordValidation.Companion.parse
import com.github.shmvanhouten.adventofcode2020.day02.PasswordValidation.Companion.validate
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class Day02Test {

    @Nested
    inner class Part1 {

        @Test
        fun `parses a line into a PasswordRule and password`() {
            val (rule, pw) = parse("1-3 a: abcde")
            expect {
                that(rule.nrAllowed).isEqualTo(1..3)
                that(rule.char).isEqualTo('a')
                that(pw).isEqualTo("abcde")
            }
        }

        @Test
        fun `a password is valid if it has the minimum amount of characters set in the policy`() {
            expectThat(validate(PasswordRule('a', 1..2), "abbbbbbbb"))
                .isTrue()
        }

        @Test
        fun `a password is invalid if it has less than the minimum of the character set in the policy`() {
            expectThat(validate(PasswordRule('a', 1..2), "bbbbbbbb"))
                .isFalse()
        }

        @Test
        fun `a password is invalid if it has more than the minimum of the character set in the policy`() {
            expectThat(validate(PasswordRule('a', 1..2), "aaa")).isFalse()
        }

        @Test
        internal fun `part 1`() {
            expectThat(input.lines().map { parse(it) }.count { (rule, pw) -> validate(rule, pw) }).isEqualTo(458)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        fun `a password is invalid if no characters match at either position`() {
            expectThat(NewPasswordValidation.validate(NewPasswordRule('a', 1, 2), "bbbaaaaaa"))
                .isFalse()
        }

        @Test
        fun `a password is valid if character matches at only first position`() {
            expectThat(NewPasswordValidation.validate(NewPasswordRule('a', 2, 4), "babbbaaaaa"))
                .isTrue()
        }

        @Test
        fun `a password is valid if character matches at only second position`() {
            expectThat(NewPasswordValidation.validate(NewPasswordRule('a', 2, 4), "bbbabbbbb"))
                .isTrue()
        }

        @Test
        fun `a password is invalid if character matches at both positions`() {
            expectThat(NewPasswordValidation.validate(NewPasswordRule('a', 2, 4), "bababbbbb"))
                .isFalse()
        }

        @Test
        internal fun `part 2`() {
            expectThat(input.lines()
                .map { NewPasswordValidation.parse(it) }
                .count { (r, pw) -> NewPasswordValidation.validate(r, pw) }).isEqualTo(342)
        }
    }

    private val input by lazy { readFile("/input-day02.txt")}

}
