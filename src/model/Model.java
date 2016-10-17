package model;

import algorithms.mazeGenerators.Maze3D;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import presenter.Command;
import presenter.Properties;
import view.View;

/**
 * Model interface
 * <p>Defines all the functions needed to be defined in a basic Model (MVC).
 * The Model will handle all the backend activity of the program and will not be reachable by the client</p>
 *
 * @author Afik & Ohad
 * @see Command
 * @see View
 * @see Controller
 */
public interface Model {
    void solveMaze(String mazeName, String strategy);
    void generateMazeGrowingTree(String Name, int cols, int rows, int layers);
    void generateMazeSimple(String Name, int cols, int rows, int layers);
    void saveProperties(Properties properties);
    void loadProperties();
    Properties getProperties();
    void saveMaze(String mazeName, String fileName);
    void saveMazesAndSolutions(String fileName);
    void loadMaze(String mazeName, String fileName);
    void loadMazesAndSolutions(String fileName);
    void setSolution(String name, Solution<Position> solution);
    void exit() throws InterruptedException;
    Solution<Position> getSolution(String name);
    Position getClue(String name);
    Maze3D getMaze(String mazeName) throws NullPointerException;
    int [][] getCrossSection(String name, String section, int index);
}
