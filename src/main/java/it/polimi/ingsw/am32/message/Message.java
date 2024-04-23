package it.polimi.ingsw.am32.message;

import it.polimi.ingsw.am32.controller.GameController;

public interface Message {
    void elaborateMessage(GameController gameController);
}
