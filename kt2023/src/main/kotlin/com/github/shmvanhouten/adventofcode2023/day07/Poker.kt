package com.github.shmvanhouten.adventofcode2023.day07

import com.github.shmvanhouten.adventofcode.utility.strings.words
import com.github.shmvanhouten.adventofcode2023.day07.HandType.*

fun  totalWinningsWithJacks(hands: List<String>): Int {
    return totalWinnings(hands) { JackHand(it) }
}

fun totalWinningsWithJokers(hands: List<String>): Int {
    return totalWinnings(hands) { JokerHand(it) }
}

private fun totalWinnings(hands: List<String>, toHand: (String) -> Hand) = hands
    .map(toHand)
    .sortedDescending()
    .mapIndexed { index, hand -> hand.bid * (index + 1) }
    .sum()

class JackHand(input: String): Hand(input) {
    override val jValue: Int = 11
    override fun calculateCardCounts(hand: String): Collection<Int> =
        groupCardsByCardType(hand).values
}

class JokerHand(input: String): Hand(input) {

    override val jValue: Int = 1

    override fun calculateCardCounts(hand: String): Collection<Int> {
        val cardsByType = groupCardsByCardType(hand).toMutableMap()

        val replaceableJokers = cardsByType.remove('J')

        if(replaceableJokers == 5) return listOf(5)
        if (replaceableJokers != null) {
            val (bestCard, count) = cardsByType.entries.maxWith(::mostOrHighest)
            cardsByType[bestCard] = count + replaceableJokers
        }

        return cardsByType.values
    }

    private fun mostOrHighest(
        thisEntry: MutableMap.MutableEntry<Char, Int>,
        otherEntry: MutableMap.MutableEntry<Char, Int>
    ): Int {
        val countsComparison = thisEntry.value.compareTo(otherEntry.value)
        return if (countsComparison == 0) thisEntry.key.compareCardTo(otherEntry.key)
        else countsComparison
    }

}

sealed class Hand(input: String) : Comparable<Hand> {
    private val hand: String
    val bid: Int

    init {
        val (hand, bid) = input.words()
        this.hand = hand
        this.bid = bid.toInt()
    }

    abstract val jValue: Int
    abstract fun calculateCardCounts(hand: String): Collection<Int>

    override operator fun compareTo(other: Hand): Int {
        return if(this.type == other.type) compareHandTo(this.hand, other.hand)
        else this.type.compareTo(other.type)
    }

    private val type: HandType by lazy {
        val cardCounts = calculateCardCounts(hand)
        when {
            cardCounts.size == 1 -> FIVE_OF_A_KIND
            cardCounts.any { it == 4 } -> FOUR_OF_A_KIND
            cardCounts.any { it == 3 } && cardCounts.any { it == 2 } -> FULL_HOUSE
            cardCounts.any { it == 3 } -> THREE_OF_A_KIND
            cardCounts.count { it == 2 } == 2 -> TWO_PAIR
            cardCounts.any { it == 2 } -> ONE_PAIR
            else -> HIGH_CARD
        }
    }

    internal fun groupCardsByCardType(hand: String) = hand.groupBy { it }.mapValues { it.value.size }
    internal fun Char.compareCardTo(otherCard: Char): Int = this.rank().compareTo(otherCard.rank())

    private fun compareHandTo(thisHand: String, otherHand: String): Int {
        thisHand.forEachIndexed { index, c ->
            val comparison = - c.compareCardTo(otherHand[index])
            if(comparison != 0) return comparison
        }
        throw NotImplementedError("We are not ready for ties $thisHand, $otherHand")
    }
    private fun Char.rank(): Int =
        when(this) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> jValue
            'T' -> 10
            else -> this.digitToInt()
        }

    override fun equals(other: Any?) = this === other
            || other is Hand
            && hand == other.hand && bid == other.bid

    override fun hashCode() = hand.hashCode() + bid
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

