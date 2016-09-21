package view;

import java.util.HashMap;

import algorithms.mazeGenerators.Maze3D;
import presenter.Command;
import model.Model;

/**
 * View interface
 * <p>Defines all the functions needed to be defined in a basic View (MVC).
 * The View will handle all the UI\UX of the program, this is the only part accessible to the customer.
 * <p>Created by Ohad on 15/09/2016.
 * @author Ohad
 * @version 1.0
 * @see Command
 * @see Model
 * @see Controller
 */
public interface View {
    void start();
    void displaySolution(String solution);
    void displayDirectory(String path);
    void notifyMazeIsReady(String name);
    void displayMaze(Maze3D maze);
    void displayCrossSection(int [][] mazeSection);
    void displayMessage(String msg);
    void printMenu(HashMap<String, Command> cliMapper);
}
