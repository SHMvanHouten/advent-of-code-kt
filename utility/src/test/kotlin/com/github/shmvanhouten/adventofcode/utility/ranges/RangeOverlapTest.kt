package com.github.shmvanhouten.adventofcode.utility.ranges

import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.*

class RangeOverlapTest {

    @Test
    fun `1-3 does not overlap with 4-5`() {
        val (overlapping, notOverlapping) = (1L..3).splitOverlapsOn(4L..5)
        expect {
            that(overlapping).isEmpty()
            that(notOverlapping)
                .hasSize(1)
                .first().isEqualTo(1L..3)

        }
    }

    @Test
    fun `5-6 does not overlap with 3-4`() {
        val (overlapping, notOverlapping) = (5L..6).splitOverlapsOn(3L..4)
        expect {
            that(overlapping).isEmpty()
            that(notOverlapping)
                .hasSize(1)
                .first().isEqualTo(5L..6)

        }
    }

    @Test
    fun `1-4 overlaps with 4-5 on 4`() {
        // 1--4..
        // ...45.
        val (overlapping, notOverlapping) = (1L..4).splitOverlapsOn(4L..5)
        expect {
            that(overlapping).hasSize(1)
                .first().isEqualTo(4L..4)
            that(notOverlapping).hasSize(1)
                .first().isEqualTo(1L..3)
        }
    }

    @Test
    fun `5-6 overlaps with 3-5 on 5`() {
        // ....56...
        // ..3-5....
        val (overlapping, notOverlapping) = (5L..6).splitOverlapsOn(3L..5)
        expect {
            that(overlapping).hasSize(1)
                .first().isEqualTo(5L..5)
            that(notOverlapping).hasSize(1)
                .first().isEqualTo(6L..6)
        }
    }

    @Test
    fun `5-9 overlaps with 3-6 on 5-6`() {
        // ....5---9
        // ..3--6...
        // ....56...
        // ......7-9
        val (overlapping, notOverlapping) = (5L..9).splitOverlapsOn(3L..6)
        expect {
            that(overlapping).hasSize(1)
                .first().isEqualTo(5L..6)
            that(notOverlapping).hasSize(1)
                .first().isEqualTo(7L..9)
        }
    }

    @Test
    fun `1-9 completely contains 4-5`() {
        val (overlapping, notOverlapping) = (1L..9).splitOverlapsOn(4L..5)
        expect {
            that(overlapping).hasSize(1)
                .first().isEqualTo(4L..5)
            that(notOverlapping).hasSize(2)
                .and {
                   first().isEqualTo(1L..3)
                   get(1).isEqualTo(6L..9)
                }
        }
    }



    @Test
    fun `1--9 overlaps with 4-4 and 5-7 fully`() {
        val (overlapping, notOverlapping) = (1L..9)
            .splitOverlapsOnAll(listOf(4L..4, 5L..7))

        expect {
            that(overlapping)
                .hasSize(2)
                .and {
                    first().isEqualTo(4L..4)
                    get(1).isEqualTo(5L..7)
                }
            that(notOverlapping)
                .hasSize(2)
                .and {
                    first().isEqualTo(1L..3)
                    get(1).isEqualTo(8L..9)
                }
        }
    }

    @Test
    fun `overlaps on 5-7 and not on 8-9`() {
        val (overlapping, notOverlapping) = (5L..9)
            .splitOverlapsOnAll(listOf(4L..4, 5L..7))
        expect {
            that(overlapping).hasSize(1)
                .first().isEqualTo(5L..7)
            that(notOverlapping).hasSize(1)
                .and { first().isEqualTo(8L..9) }
        }
    }

    @Test
    fun `3--7 overlaps with 1-4 partially and 5-5 fully and 7-8 partially`() {
        val (overlapping, notOverlapping) = (3L..7)
            .splitOverlapsOnAll(listOf(1L..4, 5L..5, 7L..8))

        expect {
            that(overlapping).hasSize(3)
                .and {
                    first().isEqualTo(3L..4)
                    get(1).isEqualTo(5L..5)
                    get(2).isEqualTo(7L..7)
                }

            that(notOverlapping).hasSize(1)
                .and { first().isEqualTo(6L..6) }
        }
    }

    @Test
    fun `reverse list example`() {
        val (overlapping, notOverlapping) = (79L..92)
            .splitOverlapsOnAll(listOf(98L..99, 50L..97))

        expect {
            that(overlapping).hasSize(1).and { first().isEqualTo(79L..92) }
            that(notOverlapping).isEmpty()
        }
    }

}