package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.FullLobbyException;
import it.polimi.ingsw.am32.controller.exceptions.GameNotFoundException;
import it.polimi.ingsw.am32.controller.exceptions.VirtualViewNotFoundException;
import it.polimi.ingsw.am32.message.ServerToClient.AccessGameConfirmMessage;
import it.polimi.ingsw.am32.message.ServerToClient.LobbyPlayerListMessage;
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
            rand = random.nextInt(); // Generate random id for the game
            foundUnique = true;

            for (GameController game : games) { // Scan all games to check that no other game has the same id
                if (game.getId() == rand) { // Id is not unique
                   foundUnique = false;
                   break;
                }
            }
            // If we reach this point, the id is unique
        }

        GameController game = new GameController(rand, playerCount); // Create a new game instance
        games.add(game); // Add game to the list of all games

        try {
            game.addPlayer(creatorName, node); // Add the creator to the newly created game
            game.submitVirtualViewMessage(new NewGameConfirmationMessage(creatorName, rand));
        } catch (FullLobbyException e) { // It should never happen that the lobby is full when the creator joins
            // TODO
        } catch (VirtualViewNotFoundException e) {
            // TODO
        }

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
                try {
                    game.addPlayer(nickname, node);
                    game.submitVirtualViewMessage(new AccessGameConfirmMessage(nickname)); // Notify the player that he has joined the game

                    // Notify all players in the lobby of the new player
                    ArrayList<String> allPlayerNicknames = (ArrayList<String>)game.getNodeList().stream().map(PlayerQuadruple::getNickname).toList(); // Get the nicknames of all players in the game (connected and not)
                    for (PlayerQuadruple playerQuadruple : game.getNodeList()) {
                        game.submitVirtualViewMessage(new LobbyPlayerListMessage(playerQuadruple.getNickname(), allPlayerNicknames));
                        // TODO Should players be notified of the status of a given player in the lobby (connected or disconnected)?
                    }
                } catch (VirtualViewNotFoundException e) {
                    // TODO
                } catch (FullLobbyException e) { // Lobby was full when tried to join
                    // TODO
                }

                if (game.getGameSize() == game.getLobbyPlayerCount()) { // Lobby is now full
                    game.enterPreparationPhase(); // Start the game
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
