#!/bin/bash

cookie=$(cat .session.txt)

if [[ -z "$1" ]]
then
DD=$(date +%d)
else
DD=$1
fi
echo "fetching input for day $DD"
DAY=$((10#$DD))

mkdir src/main/kotlin/com/github/shmvanhouten/adventofcode2021/day${DD}
mkdir src/test/kotlin/com/github/shmvanhouten/adventofcode2021/day${DD}

curl --cookie "session=$cookie;" https://adventofcode.com/2021/day/${DAY}/input -o src/main/resources/input-day${DD}.txt -s

echo "package com.github.shmvanhouten.adventofcode2021.day${DD}

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day${DD}Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun \`fixme\`() {
            assertThat(1, equalTo(1) )
        }
    }

}" > ./src/test/kotlin/com/github/shmvanhouten/adventofcode2021/day${DD}/Day${DD}Test.kt

open https://adventofcode.com/2021/day/${DAY}