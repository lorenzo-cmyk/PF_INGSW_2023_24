package it.polimi.ingsw.am32.model.player;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.Empty;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    /**
     * This method is used to generate a random NonObjectiveCard
     */
    private NonObjectiveCard generateRandomNonObjectiveCard(){
        Random rand = new Random();
        int[] permRes = new int[]{0, 0, 0, 0, 0, 0, 0};
        int[] conditionCount = new int[]{0, 0, 0, 0, 0, 0, 0};
        return new NonObjectiveCard(
                // NonObjectiveCard ID goes from 1 to 86 (inclusive)
                rand.nextInt(86) + 1,
                // NonObjectiveCard Value goes from 0 to 5 (inclusive)
                rand.nextInt(6),
                new Empty(),
                CornerType.PLANT,
                CornerType.ANIMAL,
                CornerType.FUNGI,
                CornerType.INSECT,
                CornerType.QUILL,
                CornerType.INKWELL,
                CornerType.MANUSCRIPT,
                CornerType.NON_COVERABLE,
                permRes,
                conditionCount,
                ObjectType.INSECT
        );
    }

    /**
     * This method is used to generate a random Card
     */
    private Card generateRandomCard(){
        Random rand = new Random();
        // Card ID goes from 87 to 102 (inclusive)
        // Card Value goes from 0 to 5 (inclusive)
        return new Card(rand.nextInt(16) + 87, rand.nextInt(6), new Empty());
    }

    @DisplayName("assignStartingCard should return true when hand is null and should initialize the hand with the card")
    @Test
    void assignStartingCardShouldReturnTrueWhenHandIsNull() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(card));
        assertEquals(card, player.getHand().getFirst());
    }

    @DisplayName("assignStartingCard should return false when hand is not null and should not put the card in the hand")
    @Test
    void assignStartingCardShouldReturnFalseWhenHandIsNotNull() {
        Player player = new Player("test");
        NonObjectiveCard card1 = generateRandomNonObjectiveCard();
        assertNotNull(card1);
        NonObjectiveCard card2 = generateRandomNonObjectiveCard();
        assertNotNull(card2);
        assertTrue(player.assignStartingCard(card1));
        assertFalse(player.assignStartingCard(card2));
        assertNotEquals(card2, player.getHand().getFirst());
    }

    @DisplayName("initializeGameField should return true, when game field is null and hand is not null, and should initialize the game field with the card")
    @Test
    void initializeGameFieldShouldReturnTrueWhenGameFieldIsNullAndHandIsNotNull() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(card));
        assertTrue(player.initializeGameField(true));
        assertEquals(card, player.getField().getCardFromPosition(0, 0));
    }

    @DisplayName("initializeGameField should return false when game field is not null")
    @Test
    void initializeGameFieldShouldReturnFalseWhenGameFieldIsNotNull() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(card));
        assertTrue(player.initializeGameField(true));
        assertFalse(player.initializeGameField(true));
    }

    @DisplayName("initializeGameField should return false when hand is null")
    @Test
    void initializeGameFieldShouldReturnFalseWhenHandIsNull() {
        Player player = new Player("test");
        assertFalse(player.initializeGameField(true));
    }

    @DisplayName("secretObjectiveSelection should return true when ID matches one of the cards and should set the secretObjective accordingly - Card 1")
    @Test
    void secretObjectiveSelectionShouldReturnTrueWhenIdMatchesCardOne() {
        Player player = new Player("test");
        Card card1 = generateRandomCard();
        assertNotNull(card1);
        Card card2 = generateRandomCard();
        assertNotNull(card2);
        assertTrue(player.receiveSecretObjective(card1, card2));
        assertTrue(player.secretObjectiveSelection(card1.getId()));
        assertEquals(card1, player.getSecretObjective());
    }

    @DisplayName("secretObjectiveSelection should return true when ID matches one of the cards and should set the secretObjective accordingly - Card 2")
    @Test
    void secretObjectiveSelectionShouldReturnTrueWhenIdMatchesCardTwo() {
        Player player = new Player("test");
        Card card1 = generateRandomCard();
        assertNotNull(card1);
        Card card2 = generateRandomCard();
        assertNotNull(card2);
        assertTrue(player.receiveSecretObjective(card1, card2));
        assertTrue(player.secretObjectiveSelection(card2.getId()));
        assertEquals(card2, player.getSecretObjective());
    }

    @DisplayName("secretObjectiveSelection should return false when id does not match any of the cards")
    @Test
    void secretObjectiveSelectionShouldReturnFalseWhenIdDoesNotMatch() {
        Player player = new Player("test");
        Card card1 = generateRandomCard();
        assertNotNull(card1);
        Card card2 = generateRandomCard();
        assertNotNull(card2);
        assertTrue(player.receiveSecretObjective(card1, card2));
        assertFalse(player.secretObjectiveSelection(-1));
    }

    @DisplayName("receiveSecretObjective should return true when tmpSecretObj is null")
    @Test
    void receiveSecretObjectiveShouldReturnTrueWhenTmpSecretObjIsNull() {
        Player player = new Player("test");
        Card card1 = generateRandomCard();
        assertNotNull(card1);
        Card card2 = generateRandomCard();
        assertNotNull(card2);
        assertTrue(player.receiveSecretObjective(card1, card2));
    }

    @DisplayName("receiveSecretObjective should return false when tmpSecretObj is not null")
    @Test
    void receiveSecretObjectiveShouldReturnFalseWhenTmpSecretObjIsNotNull() {
        Player player = new Player("test");
        Card card1 = generateRandomCard();
        assertNotNull(card1);
        Card card2 = generateRandomCard();
        assertNotNull(card2);
        Card card3 = generateRandomCard();
        assertNotNull(card3);
        Card card4 = generateRandomCard();
        assertNotNull(card4);
        assertTrue(player.receiveSecretObjective(card1, card2));
        assertFalse(player.receiveSecretObjective(card3, card4));
    }

    @DisplayName("putCardInHand should return true when hand size is less than 3 and hand is not null. Also, the card should be added to the hand.")
    @Test
    void putCardInHandShouldReturnTrueWhenHandSizeIsLessThanThreeAndHandIsNotNullAndCheckInsertion() {
        Player player = new Player("test");
        NonObjectiveCard startingCard = generateRandomNonObjectiveCard();
        assertNotNull(startingCard);
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(startingCard)); // Hand should not be null
        assertTrue(player.putCardInHand(card));
        assertEquals(card, player.getHand().getLast());
    }

    @DisplayName("Putting card in hand should return false when hand size is 3 or more. The card should not be added to the hand.")
    @Test
    void putCardInHandShouldReturnFalseWhenHandSizeIsThreeOrMoreAndCheckCardNotInHand() {
        Player player = new Player("test");
        NonObjectiveCard card1 = generateRandomNonObjectiveCard();
        assertNotNull(card1);
        NonObjectiveCard card2 = generateRandomNonObjectiveCard();
        assertNotNull(card2);
        NonObjectiveCard card3 = generateRandomNonObjectiveCard();
        assertNotNull(card3);
        NonObjectiveCard card4 = generateRandomNonObjectiveCard();
        assertNotNull(card4);
        assertTrue(player.assignStartingCard(card1)); // Hand should not be null
        assertEquals(card1, player.getHand().getLast());
        assertTrue(player.putCardInHand(card2));
        assertEquals(card2, player.getHand().getLast());
        assertTrue(player.putCardInHand(card3));
        assertEquals(card3, player.getHand().getLast());
        assertFalse(player.putCardInHand(card4));
        assertNotEquals(card4, player.getHand().getLast());
    }

    @DisplayName("putCardInHand should return false when hand is null")
    @Test
    void putCardInHandShouldReturnFalseWhenHandIsNull() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertFalse(player.putCardInHand(card));
    }

    @DisplayName("getNickname should return the nickname of the player")
    @Test
    void getNicknameShouldReturnTheNicknameOfThePlayer() {
        Player player = new Player("test");
        assertEquals("test", player.getNickname());
    }

    @DisplayName("getField should return the game field of the player")
    @Test
    void getFieldShouldReturnTheGameFieldOfThePlayer() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(card));
        assertTrue(player.initializeGameField(true));
        assertNotNull(player.getField());
    }

    @DisplayName("getSecretObjective should return the secret objective of the player")
    @Test
    void getSecretObjectiveShouldReturnTheSecretObjectiveOfThePlayer() {
        Player player = new Player("test");
        Card card1 = generateRandomCard();
        assertNotNull(card1);
        Card card2 = generateRandomCard();
        assertNotNull(card2);
        assertTrue(player.receiveSecretObjective(card1, card2));
        assertTrue(player.secretObjectiveSelection(card1.getId()));
        assertEquals(card1, player.getSecretObjective());
    }

    @DisplayName("getColour should return the colour of the player")
    @Test
    void getColourShouldReturnTheColourOfThePlayer() {
        Player player = new Player("test");
        assertTrue(player.setColour(Colour.RED));
        assertEquals(Colour.RED, player.getColour());
    }

    @DisplayName("getPoints should return the points of the player")
    @Test
    void getPointsShouldReturnThePointsOfThePlayer() {
        Player player = new Player("test");
        assertTrue(player.setPoints(10));
        assertEquals(10, player.getPoints());
    }

    @DisplayName("getHand should return the hand of the player")
    @Test
    void getHandShouldReturnTheHandOfThePlayer() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(card));
        assertNotNull(player.getHand());
        assertEquals(card, player.getHand().getFirst());
    }

    @DisplayName("getInitialCard should return the initial card of the player. If the game field is not initialized, it should return the first card in the hand.")
    @Test
    void getInitialCardShouldReturnTheInitialCardOfThePlayer() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(card));
        assertEquals(card, player.getInitialCard());
    }

    @DisplayName("getInitialCard should return the initial card of the player. If the game field is initialized, it should return the card at position (0, 0).")
    @Test
    void getInitialCardShouldReturnTheInitialCardOfThePlayerWhenGameFieldIsInitialized() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(card));
        assertTrue(player.initializeGameField(true));
        assertEquals(card, player.getInitialCard());
        assertEquals(card, player.getField().getCardFromPosition(0, 0));
    }

    @DisplayName("setPoints should set the points of the player and return true when done")
    @Test
    void setPointsShouldSetThePointsOfThePlayer() {
        Player player = new Player("test");
        assertTrue(player.setPoints(10));
        assertEquals(10, player.getPoints());
    }

    @DisplayName("setColour should set the colour of the player, and return true, if the colour is not set yet")
    @Test
    void setColourShouldSetTheColourOfThePlayerReturnTrue() {
        Player player = new Player("test");
        assertNull(player.getColour());
        assertTrue(player.setColour(Colour.RED));
        assertEquals(Colour.RED, player.getColour());
    }

    @DisplayName("setColour should not set the colour of the player, and return false, if the colour is already set")
    @Test
    void setColourShouldNotSetTheColourOfThePlayerIfAlreadySet() {
        Player player = new Player("test");
        assertTrue(player.setColour(Colour.RED));
        assertEquals(Colour.RED, player.getColour());
        assertFalse(player.setColour(Colour.BLUE));
    }

    @DisplayName("performMove should return false when Field is null")
    @Test
    void performMoveShouldReturnFalseWhenGameFieldIsNull() {
        Player player = new Player("test");
        assertFalse(player.performMove(1, 0, 0, true));
    }

    @DisplayName("performMove should return false when card with given ID is not in hand")
    @Test
    void performMoveShouldReturnFalseWhenCardWithGivenIdIsNotInHand() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(card));
        assertTrue(player.initializeGameField(true));
        NonObjectiveCard card1 = generateRandomNonObjectiveCard();
        assertTrue(player.putCardInHand(card1));
        assertFalse(player.performMove(999, 1, 1, true));
    }

    @DisplayName("performMove should return false when placing card in field fails")
    @Test
    void performMoveShouldReturnFalseWhenPlacingCardInFieldFails() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(card));
        assertTrue(player.initializeGameField(true));
        NonObjectiveCard card1 = generateRandomNonObjectiveCard();
        assertTrue(player.putCardInHand(card1));
        assertFalse(player.performMove(card1.getId(), 5, 5, true)); // Assuming 5,5 is an invalid position
    }

    @DisplayName("performMove should return true when move is valid and update points accordingly")
    @Test
    void performMoveShouldReturnTrueWhenMoveIsValidAndUpdatePointsAccordingly() {
        Player player = new Player("test");
        NonObjectiveCard card = generateRandomNonObjectiveCard();
        assertNotNull(card);
        assertTrue(player.assignStartingCard(card));
        assertTrue(player.initializeGameField(true));
        NonObjectiveCard card1 = generateRandomNonObjectiveCard();
        assertTrue(player.putCardInHand(card1));
        int initialPoints = player.getPoints();
        assertTrue(player.performMove(card1.getId(), 1, 1, true));
        assertTrue(player.getPoints() > initialPoints);
    }

    @DisplayName("updatePointsForObjectives should return false when the pointStrategy of any card is null")
    @Test
    void updatePointsForObjectivesShouldReturnFalseWhenPointStrategyOfAnyCardIsNull() {
        Player player = new Player("test");
        Card card1 = new Card(1, 1, null);
        assertNotNull(card1);
        Card card2 = generateRandomCard();
        assertNotNull(card2);
        Card[] cards = {card1, card2};
        assertFalse(player.updatePointsForObjectives(cards));
    }

}
