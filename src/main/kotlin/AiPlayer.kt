import kotlin.random.Random

/*
AiPlayer.kt
Computer-controlled blackjack player
Software Quality Assignment 1
Emily Goose 100743093
 */

class AiPlayer : Player {
    override var cards = ArrayList<Card>()

    // % chance of bust the AI player can tolerate (randomly assigned)
    private var riskTolerance: Int = Random.nextInt(50, 90)

    // Function for the game to ask if the AI hits or stands
    override fun getMove(): Boolean {
        val cardTotal = this.getTotal()

        // Always stand on 21 (the computer isn't stupid lol)
        if (cardTotal == 21 || (hasAce() && cardTotal == 11)) {
            return false
        }

        // Check amount needed to hit 21
        // No need to care about ace here since we'll take it at 1 in this context
        val bustDiff = 21 - cardTotal

        // If we have no chance of busting, hit
        if (bustDiff > 10) {
            return true
        }

        // Calculate approximate odds of staying safe with a hit
        // This is based on total cards in the deck, the AI doesn't count cards
        // Odds of drawing any number are 4/52, so it follows the odds of drawing lower are (n*4)/52
        val safeOdds = (4 * bustDiff) / 52 * 100

        // Hit or stand based on whether the odds are below the AI risk tolerance
        return (safeOdds >= riskTolerance)
    }

    override fun notifyBust() {
        println("$this busted! It feels nothing as it is an AI.")
    }

}