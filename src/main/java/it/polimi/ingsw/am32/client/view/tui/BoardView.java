package it.polimi.ingsw.am32.client.view.tui;

public class BoardView {
    private final int [] limits;
    private final String[][] board;
    public BoardView(int [] limits, String[][] board) {
        this.limits = limits;
        this.board = board;
    }
    public int[] getLimits() {
        return limits;
    }
    public String[][] getBoard() {
        return board;
    }
}
