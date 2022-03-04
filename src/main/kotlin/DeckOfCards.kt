/*
DeckOfCards.kt
Class to represent a deck of Card class
Software Quality Assignment 1
Emily Goose 100743093
 */

import values.CardSuit
import values.CardValue

class DeckOfCards {

    private var cards = ArrayList<Card>(52)
    private var discards = ArrayList<Card>()

    init {
        // Upon creation of a new deck, add 52 cards in order
        // Note this means a new deck is _not_ shuffled on initialization
        for (suit in CardSuit.values()) {
            for (value in CardValue.values()) {
                cards.add(Card(suit, value))
            }
        }
    }

    // Function to shuffle the deck
    fun shuffle() {
        cards.shuffle()
    }

    // Function to add to the deck discards (shuffled into the deck when the deck runs out)
    fun discard(card: Card) {
        discards.add(card)
    }

    // Function to deal a single card, removing it from the deck
    // If no cards are left, null will be returned
    fun dealCard(): Card? {
        // Check if there are cards left in the deck
        return if (cards.size > 0) {
            // Deal the top card
            cards.removeAt(0)
        } else if (discards.size > 0) {
            // Reshuffle the discards into the deck
            cards = discards
            discards = ArrayList()
            shuffle()
            // Return the new top card
            cards.removeAt(0)
        } else {
            null
        }
    }

    // Function to get remaining cards in the deck
    fun getSize(): Int {
        return cards.size
    }
}