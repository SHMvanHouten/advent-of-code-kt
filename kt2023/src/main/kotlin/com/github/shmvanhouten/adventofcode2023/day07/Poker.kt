package com.github.shmvanhouten.adventofcode2023.day07

import com.github.shmvanhouten.adventofcode.utility.coordinate.negate
import com.github.shmvanhouten.adventofcode.utility.strings.words
import com.github.shmvanhouten.adventofcode2023.day07.HandType.*

fun totalWinnings(hands: List<String>): Int {
    return hands
        .map { toHand(it) }
        .sortedWith(::compareHandsReverse)
        .mapIndexed { index, hand -> hand.bid * (index + 1) }
        .sum()
}

fun compareHands(hand1: Hand, hand2: Hand): Int {
    return if(hand1.type == hand2.type) hand2.hand.compareHandTo(hand1.hand)
    else hand1.type.compareTo(hand2.type)
}

fun compareHandsReverse(hand1: Hand, hand2: Hand): Int {
    return compareHands(hand1, hand2).negate()
}

private fun String.compareHandTo(otherHand: String): Int {
    this.forEachIndexed { index, c ->
        val comparison = c.compareCardTo(otherHand[index])
        if(comparison != 0) return comparison
    }
    throw error("could not compare hands $this, $otherHand")
}

private fun Char.compareCardTo(otherCard: Char): Int = this.rank().compareTo(otherCard.rank())

private fun Char.rank(): Int =
    when(this) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 11
        'T' -> 10
        else -> this.digitToInt();
    }


fun toHand(input: String): Hand {
    val (hand, bid) = input.words()
    return Hand(hand, bid.toInt())
}

data class Hand(val hand: String, val bid: Int) {
    val type: HandType by lazy {
        val byChar = hand.groupBy { it }.mapValues { it.value.size }
        return@lazy when {
            byChar.size == 1 -> FIVE_OF_A_KIND
            byChar.values.any { it == 4 } -> FOUR_OF_A_KIND
            byChar.values.any { it == 3 } && byChar.values.any { it == 2 } -> FULL_HOUSE
            byChar.values.any { it == 3 } -> THREE_OF_A_KIND
            byChar.values.count { it == 2 } == 2 -> TWO_PAIR
            byChar.values.any { it == 2 } -> ONE_PAIR
            else -> HIGH_CARD
        }
    }
}

enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

