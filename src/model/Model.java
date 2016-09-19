package model;

import algorithms.mazeGenerators.Maze3D;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import presenter.Command;
import view.View;

/**
 * Model interface
 * <p>Defines all the functions needed to be defined in a basic Model (MVC).
 * The Model will handle all the backend activity of the program and will not be reachable by the client
 * <p>Created by Ohad on 15/09/2016.
 * @author Ohad
 * @version 1.0
 * @see Command
 * @see View
 * @see Controller
 */
public interface Model {
    Solution<Position> solveMaze(String mazeName, String strategy);
    void generateMaze(String Name, int cols, int rows, int layers);
    void saveMaze(String mazeName, String fileName);
    void saveMazeSolutionsMap(String fileName);
    void loadMaze(String mazeName, String fileName);
    public void setSolution(String name, Solution<Position> solution);
    public Solution<Position> getSolution(String name);
    void exit();
    Maze3D getMaze(String mazeName) throws NullPointerException;
    int [][] getCrossSection(String name, String section, int index);
}
