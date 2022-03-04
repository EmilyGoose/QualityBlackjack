/*
Player.kt
Interface for human and AI blackjack players
Software Quality Assignment 1
Emily Goose 100743093
 */

interface Player {
    // List of player's cards
    // Index 0 will be the hidden one
    var cards: ArrayList<Card>

    // Gets total of player's cards
    fun getTotal(): Int {
        var total = 0
        for (card in cards) {
            total += card.getValue()
        }
        return total
    }

    // Function to check if the player has an ace
    // (Makes it easy to deal with double value of aces)
    fun hasAce(): Boolean {
        // Iterate over each card and check if it's an ace
        for (card in cards) {
            // Check if card is an ace
            if (card.getValue() == 1) {
                return true
            }
        }
        // No ace found in hand
        return false
    }

    // Give a single card to the player
    fun giveCard(card: Card?) {
        // Add the card (unless we got nothing in which case boo hoo)
        if (card != null) {
            cards.add(card)
        }
    }

    // Prompts the player to return all cards to the deck
    fun returnCards(deck: DeckOfCards) {
        // Discard card at 0th index until list is empty
        while (cards.size > 0) {
            deck.discard(cards.removeAt(0))
        }
    }

    // Gets the player's next move (Implemented in child classes)
    fun getMove(): Boolean

    // Notifies player of a bust (used for human player)
    fun notifyBust()
}