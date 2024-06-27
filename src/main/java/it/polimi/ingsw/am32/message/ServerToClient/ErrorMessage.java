package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;
/**
 * This class is used to manage the message sent by the server to the client when client's request to access the game,
 * to create a new game or to reconnect to a game is not successful.
 */
public class ErrorMessage implements StoCMessage{
    /**
     * The message that the server sends to the client to notify the failure reason of the request.
     */
    private final String message;
    /**
     * The nickname of the recipient who requested to create a new game, to access the game or to reconnect to a game
     * before and received the error message.
     */
    private final String recipientNickname;
    /**
     * The type of the error message.
     */
    private final int errorType;

    /**
     * The constructor of the class.
     * @param message the message that the server sends to the client to notify the failure reason of the request.
     * @param recipientNickname the nickname of the player who will receive the error message.
     * @param errorType the code associated with the error type.
     */
    public ErrorMessage(String message, String recipientNickname, int errorType) {
        this.message = message;
        this.recipientNickname = recipientNickname;
        this.errorType = errorType;
    }

    /**
     * This method is used to process the message to the client when the request to access the game,
     * to create a new game or to reconnect to a game is not successful,
     * updating the current event of the game and notifying the player
     * that the request is not successful.
     * @param view the view of the player who will receive the message about the failure of his request and
     *             should be updated.
     */
    @Override
    public void processMessage(View view) {
        // If the View is in the LOBBY state (or WELCOME state with JOIN_GAME as currentEvent) and the error is a
        // GAME_ALREADY_STARTED_EXCEPTION, we are going to put the View, again, in the WELCOME state with
        // RECONNECT_GAME as currentEvent to let the user choose if he wants to reconnect to the game.
        if(errorType == LobbyMessageExceptionEnumeration.GAME_ALREADY_STARTED_EXCEPTION.getValue() &&
                view.getEvent() == Event.JOIN_GAME) {
            view.updateCurrentEvent(Event.RECONNECT_GAME);
        }
        view.handleFailureCase(view.getEvent(), message);
    }

    /**
     * This method is used to get the recipient's nickname of the error message.
     * @return the nickname of the player who will receive the error message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the ErrorMessage object.
     */
    @Override
    public String toString() {
        return "ErrorMessage:{" +
                "message='" + message + '\'' +
                ", recipientNickname='" + recipientNickname + '\'' +
                ", errorType=" + errorType +
                '}';
    }
}
