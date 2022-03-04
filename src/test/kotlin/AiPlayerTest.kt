import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import values.CardSuit
import values.CardValue

// AiPlayerTest also serves to test the Player interface functions
internal class AiPlayerTest {

    lateinit var player: AiPlayer

    @BeforeEach
    fun setUp() {
        // Set up brand-new player between tests
        player = AiPlayer()
    }

    @Test
    fun getMove() {
        // AI risk tolerance is random but we can always test them for basic scenarios
        // Make sure AI always hits on a 1 (why wouldn't you)
        player.giveCard(Card(CardSuit.SPADES, CardValue.ACE))
        assertEquals(true, player.getMove(), "AI should always hit on a 1")
        // Make sure AI stands on 21 (with ace)
        player.giveCard(Card(CardSuit.SPADES, CardValue.KING))
        assertEquals(false, player.getMove(), "AI should never hit on 21")
        // Take back all cards and try again with different combo to 21 with ace
        player.returnCards(DeckOfCards())
        player.giveCard(Card(CardSuit.SPADES, CardValue.KING))
        player.giveCard(Card(CardSuit.SPADES, CardValue.QUEEN))
        player.giveCard(Card(CardSuit.SPADES, CardValue.ACE))
        assertEquals(false, player.getMove(), "AI should never hit on 21")

    }

    @Test
    fun getTotal() {
        // Make sure player with no cards has total of zero
        assertEquals(0, player.getTotal(), "Player with no cards should have 0 total")
        // Give the player a face card and check the total
        player.giveCard(Card(CardSuit.CLUBS, CardValue.QUEEN))
        assertEquals(10, player.getTotal(), "Player with queen should have 10 total")
        // Give the player a numeric card and check the total
        player.giveCard(Card(CardSuit.CLUBS, CardValue.SEVEN))
        assertEquals(17, player.getTotal(), "Player with queen and seven should have 17 total")
    }

    @Test
    fun hasAce() {
        // Make sure player with no cards has no ace
        assertEquals(false, player.hasAce(), "Player with no cards should have no ace")
        // Give player a non-ace card and check
        player.giveCard(Card(CardSuit.CLUBS, CardValue.QUEEN))
        assertEquals(false, player.hasAce(), "Player with non-ace cards should have no ace")
        // Give player an ace and check
        player.giveCard(Card(CardSuit.CLUBS, CardValue.ACE))
        assertEquals(true, player.hasAce(), "Player with ace cards should have ace")
    }

    @Test
    fun giveCard() {
        // Make sure newly instantiated player has no cards
        assertEquals(0, player.cards.size, "Player with no cards should have size 0")
        // Give the player a bunch of cards and make sure size changes accordingly
        for (i in 1 .. 20) {
            // 20 of the same card is fine, we're testing size
            player.giveCard(Card(CardSuit.SPADES, CardValue.ACE))
            assertEquals(i, player.cards.size, "Player card size should scale with cards given")
        }
    }

    @Test
    fun returnCards() {
        // Initialize a new deck for testing
        val testDeck = DeckOfCards()
        // Check that returning non-existent cards to the deck doesn't error
        player.returnCards(testDeck)
        // Give player multiple cards (Doesn't matter which ones)
        player.giveCard(Card(CardSuit.CLUBS, CardValue.ACE))
        player.giveCard(Card(CardSuit.CLUBS, CardValue.ACE))
        player.giveCard(Card(CardSuit.CLUBS, CardValue.ACE))
        // Return cards right away (no need to assert size, we tested it above in giveCard)
        player.returnCards(testDeck)
        // Make sure player has no cards left after returning
        // We don't need to check the deck as we test discard separately
        assertEquals(0, player.cards.size, "Player should have no cards after returning them all")
    }
}