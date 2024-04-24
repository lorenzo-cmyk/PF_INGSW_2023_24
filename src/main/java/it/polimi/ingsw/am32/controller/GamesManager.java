package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.NoGameFoundException;
import java.util.ArrayList;

/**
 * This class represents a manager for all the games that are currently being played.
 * Class is a Singleton, meaning that only one instance of it can be created.
 *
 * @author anto
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
     * Creates a new game and adds it to the list of games
     *
     * @param creatorName A string indicating the name of the player that created the game
     * @return The game that was created
     */
    public GameController createGame(String creatorName) {
        GameController game = new GameController(creatorName);
        games.add(game);
        return game;
    }

    /**
     * Returns the GameController of the game with the given code
     *
     * @param gameCode The code of the game
     * @return The GameController of the game with the given code
     * @throws NoGameFoundException If no game with the given code is found
     */
    public GameController accessGame(int gameCode) throws NoGameFoundException {
        for (GameController game : games) {
            if (game.getId() == gameCode) {
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
}
