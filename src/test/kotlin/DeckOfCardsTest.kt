import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import values.CardSuit
import values.CardValue
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class DeckOfCardsTest {
    private lateinit var testDeck: DeckOfCards

    @BeforeEach
    fun setUp() {
        // Create a completely new deck before each test
        testDeck = DeckOfCards()
    }

    @Test
    fun testConstructor() {
        // Create a second deck of cards
        val secondDeck = DeckOfCards()

        // Make sure both decks have 52 card to start
        assertEquals(52, testDeck.getSize())
        assertEquals(52, secondDeck.getSize())

        // Draw every card from both to confirm consistent default order
        for (i in 1..52) {
            // Compare by value, not direct object comparison
            // This has the bonus effect of testing CompareTo. Which one is broken? Who knows!
            assertEquals(
                0,
                testDeck.dealCard()?.let { secondDeck.dealCard()?.compareTo(it) },
                "Both decks in the same position should be equal"
            )
        }
    }

    @Test
    fun shuffle() {
        // Shuffle the testDeck then compare to make sure it's not equivalent to a new deck
        // There is a 1 in 8 * 10^67 chance of this test randomly failing
        testDeck.shuffle()

        // Create a new deck with default order
        val defaultDeck = DeckOfCards()

        // Deal both decks out to ArrayLists to compare later
        val defaultList = ArrayList<String>(52)
        val shuffledList = ArrayList<String>(52)
        for (i in 1..52) {
            shuffledList.add(testDeck.dealCard().toString())
            defaultList.add(defaultDeck.dealCard().toString())
        }

        // Check that the two lists are not equal
        assertEquals(false, defaultList == shuffledList, "Shuffled list must not be equal to default list")
    }

    @Test
    fun dealCard() {
        // Deal each card in sequence and make sure deck size goes down by 1
        var prevSize = 52
        for (i in 1..52) {
            assertNotNull(testDeck.dealCard(), "Deck should still have cards")
            assertEquals(1, prevSize - testDeck.getSize(), "Deck size should return 1 lower")
            prevSize -= 1
        }
        // Try to deal after all cards dealt and get null
        assertNull(testDeck.dealCard(), "Dealing beyond deck should return null")
    }

    @Test
    fun getSize() {
        // Make sure removing cards from the deck changes the size accordingly
        assertEquals(52, testDeck.getSize(), "Deck size should initialize at 52")

        // Deal a random amount of cards and make sure the deck size changes accordingly
        val randomCards = nextInt(1, 52)
        for (i in 1 .. randomCards) {
            testDeck.dealCard()
        }
        assertEquals(52 - randomCards, testDeck.getSize())
    }

    @Test
    fun discard() {
        // Test discard function
        // Discard an extra ace of spades
        val knownCard = Card(CardSuit.SPADES, CardValue.ACE)
        testDeck.discard(knownCard)
        var spadeCount = 0
        // Draw every single card and count aces of spades (should be only 1)
        for (i in 0 until 52) {
            val card = testDeck.dealCard()
            if (card.toString() == knownCard.toString()) {
                spadeCount += 1
            }
        }
        assertEquals(1, spadeCount, "Should only be 1 ace of spades in the deck")

        // Try to draw 53rd card (will trigger discards to be shuffled in)
        assertEquals(knownCard.toString(), testDeck.dealCard().toString(), "Dealt card should be the shuffled in discard")

        // Try to draw 54th card (should be null now that deck and discards exhausted)
        assertNull(testDeck.dealCard(), "Drawing from empty deck with no discards should be null")
    }
}