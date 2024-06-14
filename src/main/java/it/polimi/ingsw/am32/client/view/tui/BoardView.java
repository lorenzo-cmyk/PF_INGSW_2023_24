package it.polimi.ingsw.am32.client.view.tui;

/**
 * This class is used to store the components of the board that are used to display the board in the terminal.
 * The class is used by the {@link TextUI} class to display and update the board.
 */
public class BoardView {
    /**
     * The array of integers that stores the limits of the board which are used to set the dimensions of the board
     * that is printed on the terminal.
     */
    private final int [] limits;
    /**
     * Used to create the board that is printed on the terminal and store the information of the board.
     */
    private final String[][] board;

    /**
     * The constructor of the class that initializes the board view with the given parameters.
     * @param limits The array of integers that stores the limits of the board
     *               which are used to set the dimensions of the board that is printed on the terminal.
     * @param board the array of strings that stores the information of the board that is printed on the terminal.
     */
    public BoardView(int [] limits, String[][] board) {
        this.limits = limits;
        this.board = board;
    }

    /**
     * The getter method for the array of integers that stores the limits of the board.
     * @return the reference to the limits of the board
     */
    public int[] getLimits() {
        return limits;
    }

    /**
     * The getter method for the array of strings that stores the information of the board.
     * @return the reference to the board
     */
    public String[][] getBoard() {
        return board;
    }
}
