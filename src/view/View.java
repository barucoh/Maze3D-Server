package view;

import algorithms.mazeGenerators.Maze3D;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import controller.Command;
import controller.Controller;
import model.Model;

import java.util.HashMap;


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
    void setController(Controller controller);
    void displaySolution(String solution);
    void displayDirectory(String path);
    void notifyMazeIsReady(String name);
    void displayMaze(Maze3D maze);
    void setSolution(String name, Solution<Position> solution);
    void setCommands(HashMap<String, Command> commands);
    void displayCrossSection(String name, String section, int index);
    void generalNotification(String msg);
}
