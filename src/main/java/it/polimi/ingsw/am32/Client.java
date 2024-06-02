package it.polimi.ingsw.am32;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.client.view.gui.GraphicalUI;
import it.polimi.ingsw.am32.client.view.tui.TextUI;

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

    public static void main(String[] args){
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
                2. Graphical User Interface"""); // Print prompt only once at the start

        int uiChoice; // Stores an integer indicating the user's choice (GUI or TUI)
        while (true) { // Keep looping until the user enters a valid choice
            try {
                uiChoice = in.nextInt(); // Read the user's input

                String remainder = in.nextLine(); // Read the rest of the line (if any)
                if (!remainder.isBlank()) { // If there is any input left
                    out.println("Invalid input (must be single integer), please try again");
                    continue; // Continue the loop
                }
            } catch (InputMismatchException e) { // User entered non-integer
                out.println("Invalid input (must be integer), please try again");
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
