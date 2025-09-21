#!/bin/bash

cookie=$(cat .session.txt)

if [[ -z "$2" ]]
then
year=$(date +%Y)
else
year=$2
fi

if [[ -z "$1" ]]
then
DD=$(date +%d)
else
DD=$(printf '%02d' "$1")
fi
echo "fetching input for year $year and day $DD"

url=https://adventofcode.com/${year}/day/$((10#$DD))

source_location=kt${year}/src
source_folder=${source_location}/main/kotlin/com/github/shmvanhouten/adventofcode${year}/day${DD}
test_folder=${source_location}/test/kotlin/com/github/shmvanhouten/adventofcode${year}/day${DD}

mkdir "${source_folder}"
mkdir "${test_folder}"

curl --user-agent "https://github.com/SHMvanHouten/advent-of-code-kt/blob/master/getDayInput.sh" --cookie "session=$cookie;" "${url}"/input -o "${source_location}"/main/resources/input-day"${DD}".txt -s

echo "package com.github.shmvanhouten.adventofcode${year}.day${DD}

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile

fun main() {
    readFile(\"/input-day${DD}.txt\")
        .lines()
        .onEach(::println)
}
" > ./"${source_folder}/Placeholder${DD}.kt"

echo "package com.github.shmvanhouten.adventofcode${year}.day${DD}

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day${DD}Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun \`fixme\`() {
            expectThat(1).isEqualTo(1)
        }

        @Test
        internal fun \`part 1\`() {
            expectThat(1).isEqualTo(1)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun \`fixme\`() {
            expectThat(1).isEqualTo(1)
        }

        @Test
        internal fun \`part 2\`() {
            expectThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile(\"/input-day${DD}.txt\")}

}" > ./"${test_folder}"/Day"${DD}"Test.kt

git add .

idea ./"${test_folder}"/Day"${DD}"Test.kt
idea ./"${source_folder}/Placeholder${DD}".kt

open "${url}"