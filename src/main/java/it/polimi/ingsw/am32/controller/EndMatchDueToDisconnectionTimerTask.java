package it.polimi.ingsw.am32.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

/**
 * This class is a timer task that is used to end a match due to disconnection.
 * The timer task is scheduled when all but one players disconnects from the match.
 * If a player remain lonely in the match for a certain amount of time, the match is ended and the sole player is declared the winner.
 */
public class EndMatchDueToDisconnectionTimerTask extends TimerTask {
    /**
     * Class logger.
     */
    private static final Logger logger = LogManager.getLogger(EndMatchDueToDisconnectionTimerTask.class);
    /**
     * The gameController on which the timer task will be executed.
     */
    private final GameController gameController;

    /**
     * Constructor.
     *
     * @param gameController the gameController on which the timer task will be executed.
     */
    public EndMatchDueToDisconnectionTimerTask(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * This method is called when the timer task is executed.
     */
    @Override
    public void run() {
        logger.debug("EndMatchDueToDisconnectionTimerTask starting. Now ending the match due to disconnection...");
        gameController.endMatchDueToDisconnection();
    }
}
