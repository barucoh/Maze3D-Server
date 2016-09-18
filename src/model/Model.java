package model;

import algorithms.mazeGenerators.Maze3D;
import presenter.Command;
import presenter.Presenter;
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
    void solveMaze(String mazeName);
    void generateMaze(String Name, int cols, int rows, int layers);
    Maze3D getMaze(String mazeName);
    void exit();
    void setPresenter(Presenter presenter);
    void saveMaze(String name, String fileName);
    void loadMaze(String name, String fileName);
}
