package it.polimi.ingsw.am32.model.player;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.deck.CardDeck;
import it.polimi.ingsw.am32.model.deck.CardDeckBuilder;
import it.polimi.ingsw.am32.model.deck.NonObjectiveCardDeck;
import it.polimi.ingsw.am32.model.deck.NonObjectiveCardDeckBuilder;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameSimulationTest {
    @DisplayName("Run a, partial, game simulation in order to test the game mechanics")
    @Test
    public void runGameSimulation() {
        // Initialize the builder for CardDeck and NonObjectiveCardDeck
        CardDeckBuilder cardDeckBuilder = new CardDeckBuilder();
        NonObjectiveCardDeckBuilder nonObjectiveCardDeckBuilder = new NonObjectiveCardDeckBuilder();
        // Let the builder create the decks
        CardDeck objectiveCardDeck = cardDeckBuilder.buildCardDeck(DeckType.OBJECTIVE);
        NonObjectiveCardDeck resourceCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.RESOURCE);
        NonObjectiveCardDeck goldCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.GOLD);
        NonObjectiveCardDeck startingCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.STARTING);
        // Check if the decks are not null
        assertNotNull(objectiveCardDeck);
        assertNotNull(resourceCardDeck);
        assertNotNull(goldCardDeck);
        assertNotNull(startingCardDeck);

        // Initialize the player
        Player player = new Player("SimulationPlayer");
        // Check if the player is not null
        assertNotNull(player);

        // Give the player its starting card
        NonObjectiveCard startingCard = null;
        try {
            // We will start the simulation with the card with ID 81
            startingCard = retrieveNonObjectiveCardByID(startingCardDeck, 81);
        } catch (Exception e) {
            fail("Starting card not found in the deck.");
        }
        assertTrue(player.assignStartingCard(startingCard));
        // Check if the player has the starting card
        assertEquals(startingCard, player.getHand().getFirst());

        // Let the player initialize its field -- Placed 81 on 0,0 back
        assertTrue(player.initializeGameField(false));

        // Check if the player has a field
        assertNotNull(player.getField());
        // Check if the player has placed starting card on the field. We will also check the getInitialCard method.
        assertEquals(startingCard, player.getInitialCard());
        assertEquals(startingCard, player.getField().getCardFromPosition(0, 0));
        // The player should have 0 points and 0 cards in hand. The game is not started yet.
        assertEquals(0, player.getPoints());
        assertEquals(0, player.getHand().size());
        // The player should only have the resources got from the starting card. We will check the getAllRes method.
        int[] expectedResources = {1, 0, 0, 2, 0, 0, 0};
        assertArrayEquals(expectedResources, player.getField().getAllRes());
        // We will also check the getActiveRes method for each ObjectType. The result should be the same as the expectedResources array.
        assertEquals(1, player.getField().getActiveRes(ObjectType.PLANT));
        assertEquals(0, player.getField().getActiveRes(ObjectType.FUNGI));
        assertEquals(0, player.getField().getActiveRes(ObjectType.ANIMAL));
        assertEquals(2, player.getField().getActiveRes(ObjectType.INSECT));
        assertEquals(0, player.getField().getActiveRes(ObjectType.INKWELL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.INKWELL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.MANUSCRIPT));

        // Give the player its colour
        assertTrue(player.setColour(Colour.BLUE));
        // Check if the player has got the colour
        assertEquals(Colour.BLUE, player.getColour());

        // Give the player two Resource cards and one Gold card
        NonObjectiveCard resourceCard1 = null;
        try {
            resourceCard1 = retrieveNonObjectiveCardByID(resourceCardDeck, 23);
        } catch (Exception e) {
            fail("Resource card 23 is not found in the deck.");
        }
        NonObjectiveCard resourceCard2 = null;
        try {
            resourceCard2 = retrieveNonObjectiveCardByID(resourceCardDeck, 25);
        } catch (Exception e) {
            fail("Resource card 25 is not found in the deck.");
        }
        NonObjectiveCard goldCard1 = null;
        try {
            goldCard1 = retrieveNonObjectiveCardByID(goldCardDeck, 77);
        } catch (Exception e) {
            fail("Gold card 77 is not found in the deck.");
        }
        // Give the player the cards and check if the player has them
        assertTrue(player.putCardInHand(resourceCard1));
        assertEquals(resourceCard1, player.getHand().getLast());
        assertTrue(player.putCardInHand(resourceCard2));
        assertEquals(resourceCard2, player.getHand().getLast());
        assertTrue(player.putCardInHand(goldCard1));
        assertEquals(goldCard1, player.getHand().getLast());

        // Give the player two objective cards and let the player choose one as its secret objective
        Card objectiveCard1 = null;
        try {
            objectiveCard1 = retrieveCardByID(objectiveCardDeck, 94);
        } catch (Exception e) {
            fail("Objective card 94 is not found in the deck.");
        }
        Card objectiveCard2 = null;
        try {
            objectiveCard2 = retrieveCardByID(objectiveCardDeck, 99);
        } catch (Exception e) {
            fail("Objective card 99 is not found in the deck.");
        }
        // Let the player receive the cards and choose one as its secret objective
        assertTrue(player.receiveSecretObjective(objectiveCard1, objectiveCard2));
        // The player will choose Objective card 94 as its secret objective
        assertTrue(player.secretObjectiveSelection(94));
        // Check if the player has the secret objective
        assertEquals(objectiveCard1, player.getSecretObjective());

        // The simulation will try to use Objective card 98 and Objective card 89 as common objectives
        Card commonObjective1 = null;
        try {
            commonObjective1 = retrieveCardByID(objectiveCardDeck, 98);
        } catch (Exception e) {
            fail("Objective card 98 is not found in the deck.");
        }
        Card commonObjective2 = null;
        try {
            commonObjective2 = retrieveCardByID(objectiveCardDeck, 89);
        } catch (Exception e) {
            fail("Objective card 89 is not found in the deck.");
        }

        // We are now ready to play!

        // Turn 1: Place card 23 on -1,-1 front
        assertTrue(player.performMove(23, -1, -1, true));
        // Check if the card is placed correctly
        assertEquals(player.getField().getCardFromPosition(-1, -1), resourceCard1);
        // Check if the resources are updated correctly
        assertEquals(1, player.getField().getActiveRes(ObjectType.PLANT)); // Plant on the starting card is not covered
        assertEquals(0, player.getField().getActiveRes(ObjectType.FUNGI));
        assertEquals(2, player.getField().getActiveRes(ObjectType.ANIMAL)); // Gained thanks to the resource card
        assertEquals(1, player.getField().getActiveRes(ObjectType.INSECT)); // Lost 1 due to the resource card covering the starting card bottom left angle
        assertEquals(0, player.getField().getActiveRes(ObjectType.INKWELL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.QUILL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.MANUSCRIPT));
        // Check if the player has 0 points
        assertEquals(player.getPoints(), 0);
        // Check if the player has 2 cards in hand: 25 and 77
        assertEquals(2, player.getHand().size());
        // Turn 1: Draw card 30
        NonObjectiveCard resourceCard3 = null;
        try {
            resourceCard3 = retrieveNonObjectiveCardByID(resourceCardDeck, 30);
        } catch (Exception e) {
            fail("Resource card 30 is not found in the deck.");
        }
        // Give the player the card and check if the player has it
        assertTrue(player.putCardInHand(resourceCard3));
        assertEquals(resourceCard3, player.getHand().getLast());
        assertEquals(3, player.getHand().size());

        // Turn 2: Place card 25 on -1,-1 front
        assertTrue(player.performMove(25, 0, -2, true));
        // Check if the card is placed correctly
        assertEquals(player.getField().getCardFromPosition(0, -2), resourceCard2);
        // Check if the resources are updated correctly
        assertEquals(1, player.getField().getActiveRes(ObjectType.PLANT));
        assertEquals(0, player.getField().getActiveRes(ObjectType.FUNGI));
        assertEquals(3, player.getField().getActiveRes(ObjectType.ANIMAL)); // Gained thanks to the resource card
        assertEquals(2, player.getField().getActiveRes(ObjectType.INSECT)); // Gained thanks to the resource card
        assertEquals(1, player.getField().getActiveRes(ObjectType.INKWELL)); // Gained thanks to the resource card
        assertEquals(0, player.getField().getActiveRes(ObjectType.QUILL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.MANUSCRIPT));
        // Check if the player has 0 points
        assertEquals(player.getPoints(), 0);
        // Check if the player has 2 cards in hand: 30 and 77
        assertEquals(2, player.getHand().size());
        // Turn 2: Draw card 38
        NonObjectiveCard resourceCard4 = null;
        try {
            resourceCard4 = retrieveNonObjectiveCardByID(resourceCardDeck, 38);
        } catch (Exception e) {
            fail("Resource card 38 is not found in the deck.");
        }
        // Give the player the card and check if the player has it
        assertTrue(player.putCardInHand(resourceCard4));
        assertEquals(resourceCard4, player.getHand().getLast());
        assertEquals(3, player.getHand().size());

        // Turn 3: Place card 30 on 1,-3 front
        assertTrue(player.performMove(30, 1, -3, true));
        // Check if the card is placed correctly
        assertEquals(player.getField().getCardFromPosition(1, -3), resourceCard3);
        // Check if the resources are updated correctly
        assertEquals(1, player.getField().getActiveRes(ObjectType.PLANT));
        assertEquals(0, player.getField().getActiveRes(ObjectType.FUNGI));
        assertEquals(3, player.getField().getActiveRes(ObjectType.ANIMAL)); // Lost 1 but gained 1 so it is the same
        assertEquals(2, player.getField().getActiveRes(ObjectType.INSECT));
        assertEquals(1, player.getField().getActiveRes(ObjectType.INKWELL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.QUILL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.MANUSCRIPT));
        // Check if the player has 1 points
        assertEquals(player.getPoints(), 1);
        // Check if the player has 2 cards in hand: 38 and 77
        assertEquals(2, player.getHand().size());
        // Turn 3: Draw card 31
        NonObjectiveCard resourceCard5 = null;
        try {
            resourceCard5 = retrieveNonObjectiveCardByID(resourceCardDeck, 31);
        } catch (Exception e) {
            fail("Resource card 31 is not found in the deck.");
        }
        // Give the player the card and check if the player has it
        assertTrue(player.putCardInHand(resourceCard5));
        assertEquals(resourceCard5, player.getHand().getLast());
        assertEquals(3, player.getHand().size());

        // Turn 4: Place card 38 on 1,-1 back
        assertTrue(player.performMove(38, 1, -1, false));
        // Check if the card is placed correctly
        assertEquals(player.getField().getCardFromPosition(1, -1), resourceCard4);
        // Check if the resources are updated correctly
        assertEquals(1, player.getField().getActiveRes(ObjectType.PLANT));
        assertEquals(0, player.getField().getActiveRes(ObjectType.FUNGI));
        assertEquals(3, player.getField().getActiveRes(ObjectType.ANIMAL));
        assertEquals(2, player.getField().getActiveRes(ObjectType.INSECT)); // Lost 1 but gained 1 so it is the same
        assertEquals(1, player.getField().getActiveRes(ObjectType.INKWELL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.QUILL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.MANUSCRIPT));
        // Check if the player has 1 points
        assertEquals(player.getPoints(), 1);
        // Check if the player has 2 cards in hand: 31 and 77
        assertEquals(2, player.getHand().size());
        // Turn 4: Draw card 62
        NonObjectiveCard goldCard2 = null;
        try {
            goldCard2 = retrieveNonObjectiveCardByID(goldCardDeck, 62);
        } catch (Exception e) {
            fail("Resource card 62 is not found in the deck.");
        }
        // Give the player the card and check if the player has it
        assertTrue(player.putCardInHand(goldCard2));
        assertEquals(goldCard2, player.getHand().getLast());
        assertEquals(3, player.getHand().size());

        // Turn 5: Place card 31 on 2,-2 front
        assertTrue(player.performMove(31, 2, -2, true));
        // Check if the card is placed correctly
        assertEquals(player.getField().getCardFromPosition(2, -2), resourceCard5);
        // Check if the resources are updated correctly
        assertEquals(1, player.getField().getActiveRes(ObjectType.PLANT));
        assertEquals(0, player.getField().getActiveRes(ObjectType.FUNGI));
        assertEquals(2, player.getField().getActiveRes(ObjectType.ANIMAL)); // We lost 1
        assertEquals(4, player.getField().getActiveRes(ObjectType.INSECT)); // We gained 2
        assertEquals(1, player.getField().getActiveRes(ObjectType.INKWELL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.QUILL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.MANUSCRIPT));
        // Check if the player has 1 points
        assertEquals(player.getPoints(), 1);
        // Check if the player has 2 cards in hand: 62 and 77
        assertEquals(2, player.getHand().size());
        // Turn 5: Draw card 55 -- We are not going to use it in this simulation
        NonObjectiveCard goldCard3 = null;
        try {
            goldCard3 = retrieveNonObjectiveCardByID(goldCardDeck, 55);
        } catch (Exception e) {
            fail("Resource card 55 is not found in the deck.");
        }
        // Give the player the card and check if the player has it
        assertTrue(player.putCardInHand(goldCard3));
        assertEquals(goldCard3, player.getHand().getLast());
        assertEquals(3, player.getHand().size());

        // Turn 6: Place card 77 on 2,0 front
        assertTrue(player.performMove(77, 2, 0, true));
        // Check if the card is placed correctly
        assertEquals(player.getField().getCardFromPosition(2, 0), goldCard1);
        // Check if the resources are updated correctly
        assertEquals(1, player.getField().getActiveRes(ObjectType.PLANT));
        assertEquals(0, player.getField().getActiveRes(ObjectType.FUNGI));
        assertEquals(2, player.getField().getActiveRes(ObjectType.ANIMAL));
        assertEquals(4, player.getField().getActiveRes(ObjectType.INSECT));
        assertEquals(2, player.getField().getActiveRes(ObjectType.INKWELL)); // We gained 1
        assertEquals(0, player.getField().getActiveRes(ObjectType.QUILL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.MANUSCRIPT));
        // Check if the player has 4 points
        assertEquals(player.getPoints(), 4);
        // Check if the player has 2 cards in hand: 62 and 55
        assertEquals(2, player.getHand().size());
        // Turn 6: Draw card 54 -- We are not going to use it in this simulation
        NonObjectiveCard goldCard4 = null;
        try {
            goldCard4 = retrieveNonObjectiveCardByID(goldCardDeck, 54);
        } catch (Exception e) {
            fail("Resource card 54 is not found in the deck.");
        }
        // Give the player the card and check if the player has it
        assertTrue(player.putCardInHand(goldCard4));
        assertEquals(goldCard4, player.getHand().getLast());
        assertEquals(3, player.getHand().size());

        // Turn 7: Place card 62 on 1,1 front
        assertTrue(player.performMove(62, 1, 1, true));
        // Check if the card is placed correctly
        assertEquals(player.getField().getCardFromPosition(1, 1), goldCard2);
        // Check if the resources are updated correctly
        assertEquals(0, player.getField().getActiveRes(ObjectType.PLANT)); // We lost 1
        assertEquals(0, player.getField().getActiveRes(ObjectType.FUNGI));
        assertEquals(2, player.getField().getActiveRes(ObjectType.ANIMAL));
        assertEquals(4, player.getField().getActiveRes(ObjectType.INSECT));
        assertEquals(1, player.getField().getActiveRes(ObjectType.INKWELL)); // We lost 1
        assertEquals(0, player.getField().getActiveRes(ObjectType.QUILL));
        assertEquals(1, player.getField().getActiveRes(ObjectType.MANUSCRIPT)); // We gained 1
        // Check if the player has 5 points -- We gained a point thanks to the card: point calculation is done after the card is placed
        assertEquals(player.getPoints(), 5);
        // Check if the player has 2 cards in hand: 54 and 55
        assertEquals(2, player.getHand().size());
        // Turn 6: Draw card 53 -- We are not going to use it in this simulation
        NonObjectiveCard goldCard5 = null;
        try {
            goldCard5 = retrieveNonObjectiveCardByID(goldCardDeck, 53);
        } catch (Exception e) {
            fail("Resource card 53 is not found in the deck.");
        }
        // Give the player the card and check if the player has it
        assertTrue(player.putCardInHand(goldCard5));
        assertEquals(goldCard5, player.getHand().getLast());
        assertEquals(3, player.getHand().size());



    }

    public NonObjectiveCard retrieveNonObjectiveCardByID(NonObjectiveCardDeck deck, int id) throws Exception {
        for (NonObjectiveCard card : deck.getCards()) {
            if (card.getId() == id) {
                return card;
            }
        }
        // If the card is not found, throw an exception CardNotFoundException
        throw new Exception("CardNotFoundException: Card with ID " + id + " not found in the deck.");
    }

    public Card retrieveCardByID(CardDeck deck, int id) throws Exception {
        for (Card card : deck.getCards()) {
            if (card.getId() == id) {
                return card;
            }
        }
        // If the card is not found, throw an exception CardNotFoundException
        throw new Exception("CardNotFoundException: Card with ID " + id + " not found in the deck.");
    }
}
