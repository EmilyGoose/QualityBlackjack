import org.junit.jupiter.api.Test
import values.CardSuit
import values.CardValue
import kotlin.test.assertEquals

internal class CardTest {

    @Test
    fun getValue() {
        // Create new cards with values and check them
        assertEquals(1, Card(CardSuit.CLUBS, CardValue.ACE).getValue(), "Ace should be value of 1")
        assertEquals(7, Card(CardSuit.CLUBS, CardValue.SEVEN).getValue(), "Seven should be value of 7")
        assertEquals(10, Card(CardSuit.CLUBS, CardValue.JACK).getValue(), "Jack should be value of 10")
        assertEquals(10, Card(CardSuit.CLUBS, CardValue.KING).getValue(), "Kind should be value of 10")
    }

    @Test
    fun compareTo() {
        // Compare a few test cards
        assertEquals(
            0,
            Card(CardSuit.CLUBS, CardValue.JACK).compareTo(Card(CardSuit.CLUBS, CardValue.KING)),
            "Jack should be equal to King"
        )
        assertEquals(
            -9,
            Card(CardSuit.CLUBS, CardValue.ACE).compareTo(Card(CardSuit.CLUBS, CardValue.KING)),
            "Aces should be low card"
        )
        assertEquals(
            0,
            Card(CardSuit.SPADES, CardValue.SEVEN).compareTo(Card(CardSuit.CLUBS, CardValue.SEVEN)),
            "Cards of same value in different suits should be equal"
        )
    }

    @Test
    fun getDesignator() {
        // Test that the correct designator is returned
        assertEquals("K", Card(CardSuit.CLUBS, CardValue.KING).getDesignator(), "Designator for King should be K")
        assertEquals("10", Card(CardSuit.CLUBS, CardValue.TEN).getDesignator(), "Designator for 10 should be 10")
    }

    @Test
    fun getSuit() {
        // Make sure correct suits are returned
        assertEquals(CardSuit.CLUBS, Card(CardSuit.CLUBS, CardValue.KING).getSuit(), "Card suit should be clubs")
        assertEquals(CardSuit.SPADES, Card(CardSuit.SPADES, CardValue.KING).getSuit(), "Card suit should be spades")
    }
}