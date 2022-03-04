import values.CardSuit
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.math.floor
import kotlin.math.max

/*
GameRenderer.kt
Renders all the players and their cards
Software Quality Assignment 1
Emily Goose 100743093
 */

class GameRenderer(game: Game) : JFrame("Blackjack") {


    // Constructor to set up graphics device and such
    init {
        // Set up the window properties
        this.isUndecorated = false
        this.isResizable = false
        this.isFocusable = true
        validate()

        // Grab focus from other applications
        this.isFocusable = true
        this.requestFocusInWindow()
        this.requestFocus()
        focusTraversalKeysEnabled = false

        // Add and pack the gamePanel
        contentPane.add(GamePanel(game))
        pack()

        // Centers window
        this.setLocationRelativeTo(null)

        this.isVisible = true
    }


    //Inner class GamePanel
    internal class GamePanel(game: Game) : JPanel(), KeyListener, MouseListener {

        private var game: Game

        init {
            // Bind game to panel
            this.game = game
            this.preferredSize = Dimension(1280, 720)
        }

        // Function to draw the back of a card (Red rectangle)
        private fun renderCardBack(g: Graphics, x: Int, y: Int, height: Int) {
            // Determine height based on standard playing card ratio
            val width = floor(height * (2.5 / 3.5)).toInt()

            g.color = Color.red
            g.fillRoundRect(x, y, width, height, width / 10, height / 10)
        }

        // Function to draw a card consisting of white background, black outline, and the card value
        private fun renderCard(g: Graphics, card: Card, x: Int, y: Int, height: Int) {
            // Determine height based on standard playing card ratio
            val width = floor(height * (2.5 / 3.5)).toInt()

            // Height for drawing text
            val fontHeight = height / 5

            // White background
            g.color = Color.white
            g.fillRoundRect(x, y, width, height, width / 10, height / 10)

            // Black border
            g.color = Color.black
            g.drawRoundRect(x, y, width, height, width / 10, height / 10)

            // Set up font to draw the card text
            g.font = Font("", Font.PLAIN, fontHeight)

            // Determine a suit icon and color
            var suitIcon = ""
            when (card.getSuit()) {
                CardSuit.CLUBS -> {
                    g.color = Color.black
                    suitIcon = "\u2663"
                }
                CardSuit.SPADES -> {
                    g.color = Color.black
                    suitIcon = "\u2660"
                }
                CardSuit.HEARTS -> {
                    g.color = Color.red
                    suitIcon = "\u2665"
                }
                CardSuit.DIAMONDS -> {
                    g.color = Color.red
                    suitIcon = "\u2666"
                }
            }

            // Draw the icon and designator in the corner
            g.drawString("$suitIcon${card.getDesignator()}", x + width / 10, y + height / 10 + fontHeight / 2)

            // Get FontMetrics to determine width of suit icon
            g.font = Font("", Font.PLAIN, fontHeight * 2)
            val metrics = g.getFontMetrics(g.font)
            val iconX = (x + width / 2 - (metrics.stringWidth(suitIcon) / 2))
            val iconY = (y + height / 2 + fontHeight)

            // Draw big icon in the middle
            g.drawString(suitIcon, iconX, iconY)
        }

        public override fun paintComponent(g: Graphics?) {
            super.paintComponent(g)

            // Wrap the whole thing in a null assertion so that I don't have to put question marks everywhere
            if (g != null) {
                // Split the game's players into human and AI
                val computers = ArrayList<AiPlayer>()
                var humanPlayer: HumanPlayer? = null
                for (player in game.players) {
                    if (player is AiPlayer) {
                        computers.add(player)
                    } else if (player is HumanPlayer) {
                        // Assuming single human player for now
                        humanPlayer = player
                    }
                }

                // Draw boxes for all the AI and render their cards
                // Make it at least 1 so we don't get div by zero errors
                val aiAmount = max(computers.size, 1)
                // Horizontal space to draw each AI's cards
                val spacePerAi = 1180 / aiAmount

                // Iterate over AI and draw their cards
                for (i in 0 until computers.size) {
                    val currentAi = computers[i]

                    g.color = Color.black
                    val leftBound = i * spacePerAi

                    // Set up font and draw the AI name
                    g.font = Font("", Font.PLAIN, 20)
                    val detailString: String = if (game.showAll) {
                        // Decide whether to show total, total with ace, or bust
                        val total = currentAi.getTotal()
                        if (total > 21) {
                            " - Bust!"
                        } else if (currentAi.hasAce() && total + 10 <= 21) {
                            " - Total ${total + 10}"
                        } else {
                            " - Total $total"
                        }
                    } else {
                        ""
                    }
                    g.drawString("AI ${i + 1}${detailString}", leftBound + 50, 50)

                    val cardHeight = 180
                    val cardWidth = floor(cardHeight * (2.5 / 3.5)).toInt()

                    for (c in 0 until currentAi.cards.size) {
                        if (c == 0 && !game.showAll) {
                            // Draw the first card hidden (if required)
                            renderCardBack(g, leftBound + 50, 60, cardHeight)
                        } else {
                            // Draw the next card half-covering the previous
                            renderCard(g, currentAi.cards[c], leftBound + 50 + (c * cardWidth / 2), 60, cardHeight)
                        }
                    }

                    // Draw text to indicate current player (if AI)
                    if (game.currentPlayer == currentAi) {
                        g.color = Color.black
                        g.drawString("AI ${i + 1} is thinking...", 50, 720 / 2)
                    }
                }

                // Render the player's cards
                g.color = Color.black
                val sectionTop = 480

                // Set up font and tell the player to draw
                g.font = Font("", Font.PLAIN, 20)
                g.drawString("Your cards (Total ${humanPlayer?.getTotal()})", 50, sectionTop - 30)

                // Render the player's cards
                val cardHeight = 180
                val cardWidth = floor(cardHeight * (2.5 / 3.5)).toInt()
                // Wrap in null-safe call
                if (humanPlayer?.cards != null) {
                    // Loop through player's cards and render
                    for (i in 0 until humanPlayer.cards.size) {
                        renderCard(g, humanPlayer.cards[i], 50 + (i * (20 + cardWidth)), sectionTop, cardHeight)
                    }
                }

            }
        }

        // Mouse and keyboard events follow: We don't need them at all
        // Have to implement them though. Inheritance moment
        override fun keyTyped(e: KeyEvent?) {}
        override fun keyPressed(e: KeyEvent?) {}
        override fun keyReleased(e: KeyEvent?) {}
        override fun mouseClicked(e: MouseEvent?) {}
        override fun mousePressed(e: MouseEvent?) {}
        override fun mouseReleased(e: MouseEvent?) {}
        override fun mouseEntered(e: MouseEvent?) {}
        override fun mouseExited(e: MouseEvent?) {}
    }
}