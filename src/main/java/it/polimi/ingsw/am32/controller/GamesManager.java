package it.polimi.ingsw.am32.controller;

import java.util.ArrayList;

public class GamesManager {
    private static GamesManager instance;

    private final ArrayList<GameController> games;
    private GamesManager() {
        this.games = new ArrayList<>();
    }

    public static GamesManager getInstance() {
        if (instance == null) {
            instance = new GamesManager();
        }
        return instance;
    }

    public GameController createGame(String creatorName) {
        GameController game = new GameController(creatorName);
        games.add(game);
        return game;
    }

    public GameController accessGame(int gameCode) {
        for (GameController game : games) {
            if (game.getId() == gameCode) {
                return game;
            }
        }
        return null; // TODO: throw exception
    }

    public void deleteGame(int gameCode) {
        for (GameController game : games) {
            if (game.getId() == gameCode) {
                games.remove(game);
                break;
            }
        } // TODO: throw exception
    }
}
