/*
Card.kt
Class to represent single card in a deck
Software Quality Assignment 1
Emily Goose 100743093
 */

import values.CardSuit
import values.CardValue
import java.lang.Integer.min

class Card(private val suit: CardSuit, private val value: CardValue) : Comparable<Card> {
    // Return formatted string of the card name e.g. "ACE of CLUBS"
    override fun toString(): String {
        return "${value.name} of ${suit.name}"
    }

    // Getter for the point value of the card
    fun getValue(): Int {
        // Accounts for 0-indexed enum
        // Clamp to max of 10 for face cards
        return min(this.value.ordinal + 1, 10)
    }

    // Getter for the single-char designator
    // Used for rendering
    fun getDesignator(): String {
        return if (this.value.ordinal + 1 in 2..10) {
            // Return string of the card number
            (this.value.ordinal + 1).toString()
        } else {
            // Return first letter of the face card
            this.value.toString().first().toString()
        }
    }

    // Getter for the suit
    fun getSuit(): CardSuit {
        return this.suit
    }

    // Comparison to other cards
    override fun compareTo(other: Card): Int {
        // Compare by value then suit, return difference
        return if (this.value != other.value) {
            // Return difference between values
            this.getValue() - other.getValue()
        } else {
            // Cards are same value, return zero
            0
        }
    }
}