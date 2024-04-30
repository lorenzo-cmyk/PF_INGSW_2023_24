package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.GameNotFoundException;
import it.polimi.ingsw.am32.controller.exceptions.VirtualViewNotFoundException;
import it.polimi.ingsw.am32.message.ServerToClient.AccessGameConfirmMessage;
import it.polimi.ingsw.am32.message.ServerToClient.GameStartedMessage;
import it.polimi.ingsw.am32.message.ServerToClient.NewGameConfirmationMessage;
import it.polimi.ingsw.am32.network.NodeInterface;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a manager for all the games that are currently being played.
 * Class is a Singleton, meaning that only one instance of it can be created.
 *
 * @author Anto
 */
public class GamesManager {
    /**
     * instance: The only instance of the class.
     */
    private static GamesManager instance;
    /**
     * games: A list of all the games that are currently being played.
     */
    private final ArrayList<GameController> games;

    private GamesManager() {
        this.games = new ArrayList<>();
    }

    /**
     * Returns the only instance of the class
     *
     * @return The only instance of the class
     */
    public static GamesManager getInstance() {
        if (instance == null) {
            instance = new GamesManager();
        }
        return instance;
    }

    /**
     * Creates a new game with the given creator name and player count
     *
     * @param creatorName The name of the player that created the game
     * @param playerCount The number of players that the game will have
     * @param node The server node associated with the given player
     * @return The GameController of the newly created game
     */
    public GameController createGame(String creatorName, int playerCount, NodeInterface node) {
        Random random = new Random();
        int rand = 0;

        boolean foundUnique = false; // Flag indicating whether a valid game id has been found
        while (!foundUnique) { // Loop until a valid game id is found
            rand = random.nextInt();
            foundUnique = true; // id available, exit loop

            for (GameController game : games) { // Scan games to see if id is available
                if (game.getId() == rand) { // id not valid as it is already taken
                   foundUnique = false;
                   break;
                }
            }
        }

        GameController game = new GameController(rand, playerCount); // Create new game instance
        try { // Notify the creator that a new game has been created
            game.submitVirtualViewMessage(creatorName, new NewGameConfirmationMessage(creatorName, rand));
        } catch (VirtualViewNotFoundException e) {
            // TODO
        }

        games.add(game); // Add game to the list of all games
        game.addPlayer(creatorName, node); // Add the creator to the newly created game

        return game;
    }

    /**
     * Adds the player with the given nickname to the game with the given code
     *
     * @param nickname The nickname of the player to be added
     * @param gameCode The code of the game to be accessed
     * @param node The server node associated with the given player
     * @return The GameController of the game with the given code
     * @throws GameNotFoundException If no game with the given code is found
     */
    public GameController accessGame(String nickname, int gameCode, NodeInterface node) throws GameNotFoundException {
        for (GameController game : games) {
            if (game.getId() == gameCode) { // Found correct GameController instance
                try { // Notify the player that he has successfully joined the game
                    game.submitVirtualViewMessage(nickname, new AccessGameConfirmMessage(nickname));
                } catch (VirtualViewNotFoundException e) {
                    // TODO
                }

                game.addPlayer(nickname, node);

                if (game.getGamePlayerCount() == game.getLobbyPlayerCount()) { // Lobby is full
                    game.startGame(); // Start the game

                    for (PlayerQuadruple playerQuadruple : game.getNodeList()) { // Notify all players that the game has started
                        if (!playerQuadruple.isConnected()) continue; // Skip any players that are not currently connected

                        try {
                            game.submitVirtualViewMessage(playerQuadruple.getNickname(), new GameStartedMessage());
                        } catch (VirtualViewNotFoundException e) {
                            // TODO
                        }
                    }
                }

                return game;
            }
        }
        throw new GameNotFoundException("No game found with code " + gameCode);
    }

    /**
     * Deletes the game with the given code
     *
     * @param gameCode The code of the game to be deleted
     * @throws GameNotFoundException If no game with the given code is found
     */
    public void deleteGame(int gameCode) throws GameNotFoundException {
        // TODO
        for (GameController game : games) {
            if (game.getId() == gameCode) {
                games.remove(game);
                break;
            }
        }
        throw new GameNotFoundException("No game found with code " + gameCode);
    }
}
