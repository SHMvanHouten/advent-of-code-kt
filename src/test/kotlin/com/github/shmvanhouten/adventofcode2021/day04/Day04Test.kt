package com.github.shmvanhouten.adventofcode2021.day04

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2020.util.FileReader.readFile
import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day04Test {

    @Nested
    inner class Part1 {

        @Nested
        inner class BingoBoard {
            @Test
            internal fun `parse a bingo board`() {
                val input = """22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19"""
                val bingoBoard = toBingoBoard(input)
                assertThat(bingoBoard.cells[22]?.location, equalTo(Coordinate(0,0)))
                assertThat(bingoBoard.cells[19]?.location, equalTo(Coordinate(4,4)))
                assertThat(bingoBoard.cells.values.map { it.isMarked }, allElements(equalTo(false)))
            }

            @Test
            internal fun `flip a cell`() {
                val input = """22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19"""
                val bingoBoard = toBingoBoard(input)
                bingoBoard.flip(22)
                assertThat(bingoBoard.cells[22]?.isMarked, equalTo(true))
            }
        }

        @Nested
        inner class GameOfBingo {
            @Test
            internal fun `parse a game of bingo`() {
                val (winningNumbers, bingoGame) = toBingoGame(testInput)
                assertThat(winningNumbers.first(), equalTo(7))
                assertThat(bingoGame.boards, hasSize(equalTo(3)))
                assertThat(bingoGame.boards[0].cells[22]?.location, equalTo(Coordinate(0,0)))
            }

            @Test
            internal fun `testinput 3rd board wins`() {
                val (winningNumbers, bingoGame) = toBingoGame(testInput)
                val (winningBoard, _) = bingoGame.findWinner(winningNumbers)

                assertThat(
                    winningBoard.cells[14]?.location,
                    equalTo(Coordinate(0,0))
                )
            }

            @Test
            internal fun `the score for the winning board is 4512`() {
                val (winningNumbers, bingoGame) = toBingoGame(testInput)
                val (winningBoard, winningNumber) = bingoGame.findWinner(winningNumbers)

                assertThat(
                    winningBoard.score(winningNumber),
                    equalTo(4512L)
                )
            }
        }

        @Test
        internal fun `part 1`() {
            val (winningNumbers, bingoGame) = toBingoGame(input)
            val (winningBoard, winningNumber) = bingoGame.findWinner(winningNumbers)

            assertThat(
                winningBoard.score(winningNumber),
                equalTo(31424L)
            )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1, equalTo(1) )
        }

        @Test
        internal fun `part 2`() {
            assertThat(1, equalTo(1) )
        }
    }

    private val input by lazy {readFile("/input-day04.txt")}

    private val testInput = """7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7"""
}
