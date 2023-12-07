package com.github.shmvanhouten.adventofcode2023.day07

import com.github.shmvanhouten.adventofcode.utility.strings.words
import com.github.shmvanhouten.adventofcode2023.day07.HandType.*

fun totalWinnings(hands: List<String>): Int {
    return hands
        .map { toHand(it) }
        .sortedDescending()
        .mapIndexed { index, hand -> hand.bid * (index + 1) }
        .sum()
}

fun toHand(input: String): Hand {
    val (hand, bid) = input.words()
    return Hand(hand, bid.toInt())
}

class Hand(val hand: String, val bid: Int): Comparable<Hand> {
    override operator fun compareTo(other: Hand): Int {
        return if(this.type == other.type) other.hand.compareHandTo(this.hand)
        else this.type.compareTo(other.type)
    }

    private val type: HandType by lazy {
        val cardCounts = calculateCardCounts(hand)
        return@lazy when {
            cardCounts.size == 1 -> FIVE_OF_A_KIND
            cardCounts.any { it == 4 } -> FOUR_OF_A_KIND
            cardCounts.any { it == 3 } && cardCounts.any { it == 2 } -> FULL_HOUSE
            cardCounts.any { it == 3 } -> THREE_OF_A_KIND
            cardCounts.count { it == 2 } == 2 -> TWO_PAIR
            cardCounts.any { it == 2 } -> ONE_PAIR
            else -> HIGH_CARD
        }
    }

    private fun calculateCardCounts(hand: String): MutableCollection<Int> {
        val byCardType = groupCardsByCardType(hand)
        if (byCardType.containsKey('J') && byCardType['J']!! < 5) {
            val (bestChar, count) = byCardType.entries
                .filter { it.key != 'J' }
                .maxWith { e1, e2 ->
                    val countsComparison = e1.value.compareTo(e2.value)
                    if (countsComparison == 0) e1.key.compareCardTo(e2.key)
                    else countsComparison
                }
            byCardType[bestChar] = count + byCardType['J']!!
            byCardType.remove('J')
        }
        return byCardType.values
    }

    private fun groupCardsByCardType(hand: String) = hand.groupBy { it }.mapValues { it.value.size }.toMutableMap()
}

private fun String.compareHandTo(otherHand: String): Int {
    this.forEachIndexed { index, c ->
        val comparison = c.compareCardTo(otherHand[index])
        if(comparison != 0) return comparison
    }
    throw error("No functionality for draws $this, $otherHand")
}

private fun Char.compareCardTo(otherCard: Char): Int = this.rank().compareTo(otherCard.rank())

private fun Char.rank(): Int =
    when(this) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 1
        'T' -> 10
        else -> this.digitToInt()
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

