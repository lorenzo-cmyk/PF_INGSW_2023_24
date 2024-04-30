package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.NoGameFoundException;
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

        GameController game = new GameController(rand, playerCount);
        games.add(game);
        game.addPlayer(creatorName, node);
        return game;
    }

    /**
     * Adds the player with the given nickname to the game with the given code
     *
     * @param gameCode The code of the game to be accessed
     * @return The GameController of the game with the given code
     * @throws NoGameFoundException If no game with the given code is found
     */
    public GameController accessGame(String nickname, int gameCode, NodeInterface node) throws NoGameFoundException {
        for (GameController game : games) {
            if (game.getId() == gameCode) { // Found correct GameController instance
                game.addPlayer(nickname, node);

                if (game.getGamePlayerCount() == game.getLobbyPlayerCount()) { // Lobby is full
                    game.startGame();
                }

                return game;
            }
        }
        throw new NoGameFoundException("No game found with code " + gameCode);
    }

    /**
     * Deletes the game with the given code
     *
     * @param gameCode The code of the game to be deleted
     * @throws NoGameFoundException If no game with the given code is found
     */
    public void deleteGame(int gameCode) throws NoGameFoundException {
        for (GameController game : games) {
            if (game.getId() == gameCode) {
                games.remove(game);
                break;
            }
        }
        throw new NoGameFoundException("No game found with code " + gameCode);
    }
    // TODO
}
