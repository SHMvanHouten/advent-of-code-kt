package com.github.shmvanhouten.adventofcode2020.day02

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.words

fun main() {
    readFile("/input-day02.txt")
        .lines()
        .onEach(::println)
}

class PasswordValidation {
    companion object {

        fun validate(line: PasswordRule, password: String): Boolean {
            return password.count { it == line.char } in line.nrAllowed;
        }

        fun parse(input: String): Pair<PasswordRule, String> {
            val (rawRange, rawChar, pw) = input.words()
            val (min, max) = rawRange.split('-').map(String::toInt)
            return PasswordRule(rawChar.first(), min..max) to pw
        }

    }
}

class NewPasswordValidation {
    companion object {
        fun parse(input: String): Pair<NewPasswordRule, String> {
            val (rawRange, rawChar, pw) = input.words()
            val (min, max) = rawRange.split('-').map(String::toInt)
            return NewPasswordRule(rawChar.first(), min, max) to pw
        }

        fun validate(rule: NewPasswordRule, password: String): Boolean {
            return (password.getOrNull(rule.firstPosition - 1) == rule.char)
                .xor(password.getOrNull(rule.secondPosition - 1) == rule.char)
        }
    }
}

data class PasswordRule(val char: Char, val nrAllowed: IntRange)
data class NewPasswordRule(val char: Char, val firstPosition: Int, val secondPosition: Int)
