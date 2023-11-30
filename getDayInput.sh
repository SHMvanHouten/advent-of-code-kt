#!/bin/bash

cookie=$(cat .session.txt)

year=$(date +%Y)

if [[ -z "$1" ]]
then
DD=$(date +%d)
else
DD=$1
fi
echo "fetching input for day $DD"
DAY=$((10#$DD))

source_location=kt${year}/src

mkdir ${source_location}/main/kotlin/com/github/shmvanhouten/adventofcode${year}/day${DD}
mkdir ${source_location}/test/kotlin/com/github/shmvanhouten/adventofcode${year}/day${DD}

curl --user-agent "https://github.com/SHMvanHouten/advent-of-code-kt/blob/master/getDayInput.sh" --cookie "session=$cookie;" https://adventofcode.com/${year}/day/${DAY}/input -o ${source_location}/main/resources/input-day${DD}.txt -s
touch ./${source_location}/main/kotlin/com/github/shmvanhouten/adventofcode${year}/day${DD}/Placeholder.kt

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

}" > ./${source_location}/test/kotlin/com/github/shmvanhouten/adventofcode${year}/day${DD}/Day${DD}Test.kt

open https://adventofcode.com/${year}/day/${DAY}