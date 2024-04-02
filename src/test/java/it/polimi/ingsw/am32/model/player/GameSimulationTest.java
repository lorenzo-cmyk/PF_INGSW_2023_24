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
    @DisplayName("Run a, partial, game simulation to test the game mechanics.")
    @Test
    public void runGameSimulation() {
        // Initialize the decks for Resources, Gold, Objective and Starting cards
        CardDeckBuilder cardDeckBuilder = new CardDeckBuilder();
        assertNotNull(cardDeckBuilder);
        NonObjectiveCardDeckBuilder nonObjectiveCardDeckBuilder = new NonObjectiveCardDeckBuilder();
        assertNotNull(nonObjectiveCardDeckBuilder);

        CardDeck objectiveCardDeck = cardDeckBuilder.buildCardDeck(DeckType.OBJECTIVE);
        assertNotNull(objectiveCardDeck);
        NonObjectiveCardDeck resourceCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.RESOURCE);
        assertNotNull(resourceCardDeck);
        NonObjectiveCardDeck goldCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.GOLD);
        assertNotNull(goldCardDeck);
        NonObjectiveCardDeck startingCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.STARTING);
        assertNotNull(startingCardDeck);

        // Initialize the player
        Player player = new Player("SimulationPlayer");
        assertNotNull(player);

        // Give the player its starting card
        NonObjectiveCard startingCard = retrieveNonObjectiveCardByID(startingCardDeck, 81);
        assertNotNull(startingCard);

        assertTrue(player.assignStartingCard(startingCard));
        assertEquals(startingCard, player.getHand().getFirst());

        // Let the player initialize its field
        assertTrue(player.initializeGameField(false));
        assertNotNull(player.getField());

        assertEquals(startingCard, player.getInitialCard());
        assertEquals(startingCard, player.getField().getCardFromPosition(0, 0));

        assertEquals(0, player.getPoints());
        assertEquals(0, player.getHand().size());

        int[] expectedResources = {1, 0, 0, 2, 0, 0, 0};
        assertArrayEquals(expectedResources, player.getField().getAllRes());

        assertEquals(1, player.getField().getActiveRes(ObjectType.PLANT));
        assertEquals(0, player.getField().getActiveRes(ObjectType.FUNGI));
        assertEquals(0, player.getField().getActiveRes(ObjectType.ANIMAL));
        assertEquals(2, player.getField().getActiveRes(ObjectType.INSECT));
        assertEquals(0, player.getField().getActiveRes(ObjectType.INKWELL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.INKWELL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.MANUSCRIPT));

        // Give the player its colour
        assertTrue(player.setColour(Colour.BLUE));
        assertEquals(Colour.BLUE, player.getColour());

        // Give the player two Resource cards and one Gold card
        NonObjectiveCard resourceCard1 = retrieveNonObjectiveCardByID(resourceCardDeck, 23);
        assertNotNull(resourceCard1);
        NonObjectiveCard resourceCard2 = retrieveNonObjectiveCardByID(resourceCardDeck, 25);
        assertNotNull(resourceCard2);
        NonObjectiveCard goldCard1 = retrieveNonObjectiveCardByID(goldCardDeck, 77);
        assertNotNull(goldCard1);

        assertTrue(player.putCardInHand(resourceCard1));
        assertEquals(resourceCard1, player.getHand().getLast());
        assertTrue(player.putCardInHand(resourceCard2));
        assertEquals(resourceCard2, player.getHand().getLast());
        assertTrue(player.putCardInHand(goldCard1));
        assertEquals(goldCard1, player.getHand().getLast());

        // Give the player two objective cards and let the player choose one as secret objective
        Card objectiveCard1 = retrieveCardByID(objectiveCardDeck, 94);
        assertNotNull(objectiveCard1);
        Card objectiveCard2 = retrieveCardByID(objectiveCardDeck, 99);
        assertNotNull(objectiveCard2);

        assertTrue(player.receiveSecretObjective(objectiveCard1, objectiveCard2));
        assertTrue(player.secretObjectiveSelection(objectiveCard1.getId()));

        assertEquals(objectiveCard1, player.getSecretObjective());

        // We are now ready to play:

        // Turn 1: Place a card
        assertTrue(player.performMove(resourceCard1.getId(), -1, -1, true));
        assertEquals(2, player.getHand().size());

        assertEquals(player.getField().getCardFromPosition(-1, -1), resourceCard1);

        assertEquals(1, player.getField().getActiveRes(ObjectType.PLANT)); // Plant on the starting card is not covered
        assertEquals(0, player.getField().getActiveRes(ObjectType.FUNGI));
        assertEquals(2, player.getField().getActiveRes(ObjectType.ANIMAL)); // Gained thanks to the resource card
        assertEquals(1, player.getField().getActiveRes(ObjectType.INSECT)); // Lost 1 due to the resource card covering the starting card bottom left angle
        assertEquals(0, player.getField().getActiveRes(ObjectType.INKWELL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.INKWELL));
        assertEquals(0, player.getField().getActiveRes(ObjectType.MANUSCRIPT));
        assertEquals(player.getPoints(), 0);

        // TODO: Continue the simulation after this part is working
    }

    public NonObjectiveCard retrieveNonObjectiveCardByID(NonObjectiveCardDeck deck, int id) {
        for (NonObjectiveCard card : deck.getCards()) {
            if (card.getId() == id) {
                return card;
            }
        }
        return null;
    }

    public Card retrieveCardByID(CardDeck deck, int id) {
        for (Card card : deck.getCards()) {
            if (card.getId() == id) {
                return card;
            }
        }
        return null;
    }

}
