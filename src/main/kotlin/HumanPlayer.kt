import javax.swing.JFrame
import javax.swing.JOptionPane


/*
HumanPlayer.kt
Class to handle input from the human player
Software Quality Assignment 1
Emily Goose 100743093
 */

class HumanPlayer : Player {
    override var cards = ArrayList<Card>()

    override fun getMove(): Boolean {
        // Pop up a dialog asking user to choose a move
        val options = arrayOf<Any>(
            "Hit",
            "Stand"
        )
        var message = "Your cards add to ${this.getTotal()}"
        // Tell the user the alternative value if they have an ace
        if (this.hasAce()) {
            message = "$message (${this.getTotal() + 10} with ace)"
        }
        val userChoice = JOptionPane.showOptionDialog(
            JFrame("Blackjack prompt"),
            "$message. Hit or stand?",
            "Blackjack prompt",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]
        )

        // If user chooses to hit, return true, otherwise false
        return userChoice == 0
    }

    override fun notifyBust() {
        JOptionPane.showMessageDialog(
            JFrame("Blackjack prompt"),
            "You went bust! Better luck next time!",
            "Busted!",
            JOptionPane.ERROR_MESSAGE
        )
    }
}
