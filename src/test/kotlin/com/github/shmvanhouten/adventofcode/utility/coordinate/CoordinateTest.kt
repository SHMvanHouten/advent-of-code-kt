package com.github.shmvanhouten.adventofcode.utility.coordinate

import com.github.shmvanhouten.adventofcode.utility.coordinate.Degree.*
import com.github.shmvanhouten.adventofcode2020.coordinate.ClockDirection.CLOCKWISE
import com.github.shmvanhouten.adventofcode2020.coordinate.ClockDirection.COUNTER_CLOCKWISE
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class CoordinateTest {

    @Nested
    inner class GetSurrounding {

        @Test
        fun `gets all 8 coordinates surrounding the coordinate`() {
            val surrounding = Coordinate(1, 1).getSurrounding()
            assertThat(surrounding, hasSize(equalTo(8)))
            assertThat(surrounding, hasElement(Coordinate(0, 0)))
            assertThat(surrounding, hasElement(Coordinate(1, 0)))
            assertThat(surrounding, hasElement(Coordinate(2, 0)))
            assertThat(surrounding, hasElement(Coordinate(0, 1)))
            assertThat(surrounding, hasElement(Coordinate(2, 1)))
            assertThat(surrounding, hasElement(Coordinate(0, 2)))
            assertThat(surrounding, hasElement(Coordinate(1, 2)))
            assertThat(surrounding, hasElement(Coordinate(2, 2)))
        }

        @Test
        internal fun `gets surrounding coordinates for negative coordinates`() {
            val surrounding = Coordinate(-1, -1).getSurrounding()
            assertThat(surrounding, hasSize(equalTo(8)))
            assertThat(surrounding, hasElement(Coordinate(0, 0)))
            assertThat(surrounding, hasElement(Coordinate(-1, 0)))
            assertThat(surrounding, hasElement(Coordinate(-2, 0)))
            assertThat(surrounding, hasElement(Coordinate(0, -1)))
            assertThat(surrounding, hasElement(Coordinate(-2, -1)))
            assertThat(surrounding, hasElement(Coordinate(0, -2)))
            assertThat(surrounding, hasElement(Coordinate(-1, -2)))
            assertThat(surrounding, hasElement(Coordinate(-2, -2)))
        }
    }

    @Nested
    inner class GetNeighbour {

        @ParameterizedTest
        @CsvSource(
            value = ["NORTH, 1, 0",
                "EAST, 2, 1",
                "WEST, 0, 1",
                "SOUTH, 1, 2"]
        )
        internal fun `gets the neighbour of Coordinate(1, 1) in the specified direction`(
            direction: Direction,
            x: Int,
            y: Int
        ) {
            assertThat(
                Coordinate(1, 1).getNeighbour(direction),
                equalTo(Coordinate(x, y))
            )
        }
    }

    @Nested
    inner class Move {
        @ParameterizedTest(name = "Moving {0} from (1,1) by 1 step results in ({1},{2})")
        @CsvSource(
            value = ["NORTH, 1, 0",
                "EAST, 2, 1",
                "WEST, 0, 1",
                "SOUTH, 1, 2"]
        )
        internal fun `gets the neighbour of Coordinate(1, 1) in the specified direction`(
            direction: Direction,
            x: Int,
            y: Int
        ) {
            assertThat(
                Coordinate(1, 1).move(direction),
                equalTo(Coordinate(x, y))
            )
        }

        @ParameterizedTest(name = "Moving {0} steps {1} from (1,1) by 1 step results in ({2},{3})")
        @CsvSource(
            value = ["2, NORTH, 1, -1",
                "3, EAST, 4, 1",
                "4, WEST, -3, 1",
                "5, SOUTH, 1, 6"]
        )
        internal fun `moves from Coordinate(1, 1) in the specified direction`(
            distance: Int,
            direction: Direction,
            x: Int,
            y: Int
        ) {
            assertThat(
                Coordinate(1, 1).move(direction, distance),
                equalTo(Coordinate(x, y))
            )
        }
    }

    @Test
    fun plus() {
        assertThat(
            Coordinate(0, 0) + Coordinate(1, 1),
            equalTo(Coordinate(1, 1))
        )
        assertThat(
            Coordinate(1, 1) + Coordinate(1, 1),
            equalTo(Coordinate(2, 2))
        )
        assertThat(
            Coordinate(2, 1) + Coordinate(1, 2),
            equalTo(Coordinate(3, 3))
        )
        assertThat(
            Coordinate(-1, 1) + Coordinate(2, -2),
            equalTo(Coordinate(1, -1))
        )
    }

    @Test
    fun minus() {
        assertThat(
            Coordinate(0, 0) - Coordinate(1, 1),
            equalTo(Coordinate(-1, -1))
        )
        assertThat(
            Coordinate(1, 1) - Coordinate(-1, -1),
            equalTo(Coordinate(2, 2))
        )
        assertThat(
            Coordinate(2, 1) - Coordinate(1, 2),
            equalTo(Coordinate(1, -1))
        )
        assertThat(
            Coordinate(-1, 1) - Coordinate(2, -2),
            equalTo(Coordinate(-3, 3))
        )
    }

    val isTrue: Matcher<Boolean> = Matcher(fn = Boolean::equals, true)
    val isFalse: Matcher<Boolean> = Matcher(fn = Boolean::equals, false)

    @Test
    fun isInBounds() {
        assertThat(Coordinate(0, 0).isInBounds(0, 1, 0, 1), isTrue)
        assertThat(Coordinate(1, 1).isInBounds(0, 1, 0, 1), isTrue)
        assertThat(Coordinate(2, 0).isInBounds(0, 1, 0, 1), isFalse)
        assertThat(Coordinate(0, 2).isInBounds(0, 1, 0, 1), isFalse)
        assertThat(Coordinate(-1, 0).isInBounds(0, 1, 0, 1), isFalse)
        assertThat(Coordinate(0, -1).isInBounds(0, 1, 0, 1), isFalse)
    }

    @ParameterizedTest(name = "({0}, {1}) is {4} steps away from ({2}, {3}) ")
    @CsvSource( value = [
        "0, 0, 2, 3, 5",
        "1, 1, 2, 3, 3",
        "-1, -1, 2, 3, 7",
    ])
    fun distanceFrom(x1:Int, y1:Int, x2: Int, y2: Int, distance: Int) {
        assertThat(Coordinate(x1, y1).distanceFrom(Coordinate(x2, y2)), equalTo(distance))
    }

    @ParameterizedTest(name = "({0}, {1}) times {2} is ({3}, {4})")
    @CsvSource( value = [
        "0, 0, 2, 0, 0",
        "1, 1, 2, 2, 2",
        "-1, 3, 2, -2, 6",
        "4, -4, -2, -8, 8",
    ])
    fun times(x1:Int, y1:Int, amount: Int, resultX: Int, resultY: Int) {
        assertThat(Coordinate(x1, y1).times(amount), equalTo(Coordinate(resultX, resultY)))
    }

    @Nested
    inner class TurnRelativeToOrigin {

        @ParameterizedTest(name = "turning ({0}, {1}) 90 degrees clockwise results in ({2}, {3}) ")
        @CsvSource(
            "1, 3, -3, 1",
            "1, -3, 3, 1",
            "-1, 3, -3, -1",
            "-1, -3, 3, -1",
            "-3, 1, -1, -3",
            "10, -4, 4, 10"
        )
        internal fun `turning clockwise 90 degrees or counter clockwise 270 degrees`(
            x: Int,
            y: Int,
            expectedX: Int,
            expectedY: Int
        ) {
            assertThat(Coordinate(x, y).turnRelativeToOrigin(CLOCKWISE, D90), equalTo(Coordinate(expectedX, expectedY)))
            assertThat(Coordinate(x, y).turnRelativeToOrigin(COUNTER_CLOCKWISE, D270), equalTo(Coordinate(expectedX, expectedY)))
        }

        @ParameterizedTest
        @CsvSource(
            "1, 3, 3, -1",
            "1, -3, -3, -1",
            "-1, 3, 3, 1",
            "-1, -3, -3, 1"
        )
        internal fun `turning counter clockwise 90 or clockwise 270 degrees`(
            x: Int,
            y: Int,
            expectedX: Int,
            expectedY: Int
        ) {
            assertThat(Coordinate(x, y).turnRelativeToOrigin(COUNTER_CLOCKWISE, D90), equalTo(Coordinate(expectedX, expectedY)))
            assertThat(Coordinate(x, y).turnRelativeToOrigin(CLOCKWISE, D270), equalTo(Coordinate(expectedX, expectedY)))
        }

        @ParameterizedTest(name = "turning ({0}, {1}) 180 degrees results in ({2}, {3}) ")
        @CsvSource(value = [
            "0, 0, 0, 0",
            "1, 1, -1, -1",
            "1, 3, -1, -3",
            "1, -3, -1, 3",
            "-1, 3, 1, -3",
            "-1, -3, 1, 3"
        ])
        fun `turning 180 degrees around origin`(x1:Int, y1:Int, resultX: Int, resultY: Int) {
            assertThat(Coordinate(x1, y1).turnRelativeToOrigin(CLOCKWISE, D180), equalTo(Coordinate(resultX, resultY)))
            assertThat(Coordinate(x1, y1).turnRelativeToOrigin(COUNTER_CLOCKWISE, D180), equalTo(Coordinate(resultX, resultY)))
        }
    }

}