package com.github.shmvanhouten.adventofcode2023.day07

import com.github.shmvanhouten.adventofcode.utility.strings.words
import com.github.shmvanhouten.adventofcode2023.day07.HandType.*

fun  totalWinningsWithJacks(hands: List<String>): Int {
    return totalWinnings(hands) { toJackHand(it) }
}

fun totalWinningsWithJokers(hands: List<String>): Int {
    return totalWinnings(hands) {toJokerHand(it)}
}

private fun totalWinnings(hands: List<String>, handParser: (String) -> Hand) = hands
    .map(handParser)
    .sortedDescending()
    .mapIndexed { index, hand -> hand.bid * (index + 1) }
    .sum()

fun toJokerHand(input: String): JokerHand {
    val (hand, bid) = input.words()
    return JokerHand(hand, bid.toInt())
}

fun toJackHand(input: String): JackHand {
    val (hand, bid) = input.words()
    return JackHand(hand, bid.toInt())
}

private var jValue: Int = 0

data class JackHand(val h: String, val b: Int): Hand(h, b) {
    init {
        jValue = 11
    }

    override fun calculateCardCounts(hand: String): MutableCollection<Int> =
        groupCardsByCardType(hand).values

}

data class JokerHand(val h: String, val b: Int): Hand(h,b) {
    init {
        jValue = 1
    }

    override fun calculateCardCounts(hand: String): MutableCollection<Int> {
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

}

sealed class Hand(val hand: String, val bid: Int): Comparable<Hand> {
    override operator fun compareTo(other: Hand): Int {
        return if(this.type == other.type) other.hand.compareHandTo(this.hand)
        else this.type.compareTo(other.type)
    }

    internal val type: HandType by lazy {
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

    abstract fun calculateCardCounts(hand: String): MutableCollection<Int>

    internal fun groupCardsByCardType(hand: String) = hand.groupBy { it }.mapValues { it.value.size }.toMutableMap()
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
        'J' -> jValue
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

