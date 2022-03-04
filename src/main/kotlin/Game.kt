import javax.swing.JFrame
import javax.swing.JOptionPane
import kotlin.system.exitProcess

/*
Game.kt
Main game loop and logic
Software Quality Assignment 1
Emily Goose 100743093
 */

class Game {
    var players = ArrayList<Player>()

    // How long to wait between moves
    private val gameDelay: Long = 1000

    // Tells renderer whether to show all cards
    var showAll = false

    // Current player (renderer purposes)
    var currentPlayer: Player? = null

    fun startGame() {

        // Initialize the renderer
        val renderer = GameRenderer(this)

        // Initialize the human player
        val humanPlayer = HumanPlayer()
        var humanWonHands = 0

        // Initialize AI opponents
        for (i in 1..3) {
            players.add(AiPlayer())
        }

        // Add the human player to the list of players
        players.add(humanPlayer)

        // Main game loop
        val gameDeck = DeckOfCards()
        var endCondition: Boolean
        do {
            // Hide first cards
            showAll = false
            // Shuffle the deck
            gameDeck.shuffle()
            // Deal 2 cards to each player
            for (player in players) {
                // Deal the player 2 cards (wrapped in null-safe)
                player.giveCard(gameDeck.dealCard())
                player.giveCard(gameDeck.dealCard())
            }
            renderer.repaint()

            // Ask each player what their move is until all players have stood or busted
            for (player in players) {
                // Set current player
                currentPlayer = player
                renderer.repaint()
                // Ask the player for their move until they stand or bust
                var playerMove: Boolean
                do {
                    // Ask player for their move
                    playerMove = player.getMove()
                    if (playerMove) {
                        player.giveCard(gameDeck.dealCard())
                        println("$player has chosen to hit, new total is ${player.getTotal()}")
                        renderer.repaint()

                        // Tell the player (Human or AI) they've gone bust
                        if (player.getTotal() > 21) {
                            player.notifyBust()
                        }
                    } else {
                        println("$player has chosen to stand on ${player.getTotal()}")
                    }
                    // Sleep the thread so AI isn't instant
                    if (player is AiPlayer) {
                        Thread.sleep(gameDelay)
                    }
                    // Continue as long as player hasn't stood or busted
                } while (playerMove && player.getTotal() <= 21)
            }
            renderer.repaint()
            currentPlayer = null

            // Go over each player and figure out who the highest player is
            // Not handling ties because I'm rushing to finish last minute
            var maxScore = 0
            var winningPlayer: Player? = null
            for (player in players) {
                // Checking >= maxScore because human player is last and should be winner
                if (player.getTotal() in maxScore..21) {
                    maxScore = player.getTotal()
                    winningPlayer = player
                } else if (player.hasAce() && player.getTotal() + 10 in maxScore..21) {
                    maxScore = player.getTotal() + 10
                    winningPlayer = player
                }
            }

            // If the winning player is the human, give them a point
            // Otherwise, tell them the AI won
            // (robots don't remember their wins, L for them)
            var finalMessage: String
            if (winningPlayer == humanPlayer) {
                humanWonHands += 1
                finalMessage = "You won with $maxScore! You've won $humanWonHands so far."
            } else if (winningPlayer != null) {
                finalMessage = "One of the AI won with $maxScore. You've won $humanWonHands so far."
            } else {
                finalMessage = "All players went bust! No one wins! You've won $humanWonHands so far."
            }

            // Show all first cards
            showAll = true
            renderer.repaint()

            // Ask user if they'd like to keep playing
            val options = arrayOf<Any>(
                "Keep playing",
                "Quit"
            )
            // Display the option dialog
            val userChoice = JOptionPane.showOptionDialog(
                JFrame("Blackjack prompt"),
                finalMessage,
                "Blackjack prompt",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
            )

            // Take all the cards back
            for (player in players) {
                player.returnCards(gameDeck)
            }

            // Keep playing or end game based on user choice
            endCondition = userChoice == 1
        } while (!endCondition)

        // Exit the process with normal status code
        // Otherwise it'll keep running lol
        // Pretty sure I forgot to kill JOptionPane or something but this is my "fix"
        exitProcess(0)
    }
}

// Main function (Can't be in a class in Kotlin)
fun main() {
    // Initialize the game instance
    val game = Game()
    // Start the game
    game.startGame()
}