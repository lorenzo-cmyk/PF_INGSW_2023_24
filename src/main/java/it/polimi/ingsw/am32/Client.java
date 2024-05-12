package it.polimi.ingsw.am32;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.client.view.gui.GraphicalUI;
import it.polimi.ingsw.am32.client.view.tui.TextUI;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    private static final Scanner in = new Scanner(System.in);
    private static final PrintStream out= new PrintStream(System.out);
    private static final Logger logger = LogManager.getLogger("ClientLogger");

    public static void main(String[] args){
        logger.info("---Client ---");
        out.println("Welcome to Codex Naturalis");
        chooseUI();
    }

    public static void chooseUI(){
        View view = null;
        out.println("""
                Choose the User Interface:
                1. Text User Interface
                2. Graphical User Interface""");
        int uiChoice;
        while (true) {
            try {
                uiChoice = in.nextInt();
                break;
            } catch (InputMismatchException e) {
                out.println("! Invalid input, please try again");
                in.nextLine();
            }
        }
        switch (uiChoice) {
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
                chooseUI();
                break;
            }
        }
        if (view != null) {
            view.launch();
        }
    }
}
