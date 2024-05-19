package it.polimi.ingsw.am32;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.client.view.gui.GraphicalUI;
import it.polimi.ingsw.am32.client.view.tui.TextUI;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The main class on the client side of the application.
 * Prompts the user to choose whether to play the game in GUI or TUI mode.
 * After the user has chosen, calls the appropriate view object.
 *
 * @author Jie
 */
public class Client {
    /**
     * Used to read the user's input
     */
    private static final Scanner in = new Scanner(System.in);
    /**
     * Used to display output text on the user's terminal
     */
    private static final PrintStream out= new PrintStream(System.out);
    /**
     * Used for logging purposes
     */
    private static final Logger logger = LogManager.getLogger("ClientLogger");

    public static void main(String[] args){
        logger.info("---Client ---");
        out.println("Welcome to Codex Naturalis");
        chooseUI();
    }

    /**
     * Prompts the user to choose whether to play the game in GUI or TUI mode
     */
    public static void chooseUI(){
        View view;

        out.println("""
                Choose the User Interface:
                1. Text User Interface
                2. Graphical User Interface""");

        int uiChoice; // Stores an integer indicating the user's choice (GUI or TUI)
        while (true) { // Keep looping until the user enters a valid choice
            try {
                uiChoice = in.nextInt(); // Read the user's input
            } catch (InputMismatchException e) { // User entered non-integer
                out.println("! Invalid input, please try again");
                in.nextLine(); // Clear the input buffer (otherwise the same input will be read again)
                continue; // Continue the loop
            }

            switch (uiChoice) { // Check the user's choice
                case 1: {
                    view = new TextUI();
                    break;
                }
                case 2: {
                    view = new GraphicalUI();
                    break;
                }
                default: {
                    out.println("Invalid choice, please try again");
                    continue; // Continue the loop
                }
            }

            // If we get to this point, the user has entered a valid choice
            break;
        }

        view.launch(); // Start the game using the chosen UI
    }
}
