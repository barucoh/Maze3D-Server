package view;

import algorithms.mazeGenerators.Maze3D;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import presenter.Command;
import model.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * MyView
 * <p>Implements all of View's functions and handling all the program's UI / UX.
 * <p>Created by Ohad on 15/09/2016.
 * @author Ohad
 * @version 1.0
 * @see Model
 * @see Controller
 */
public class MyView extends Observable implements View, Observer{
    //private BufferedReader in;
    private PrintWriter out;
    private CLI cli;

    private Map<String, Solution<Position>> solutions;
    
    public MyView(BufferedReader in, PrintWriter out) {
        //this.in = in;
        this.out = out;
        
        this.solutions = new HashMap<>();
        
        this.cli = new CLI(in, out);
        this.cli.addObserver(this);
    }

    @Override
    public void start() {
        this.cli.Start();
    }

    @Override
    public void displaySolution(String solution) {
        this.displayMessage(solutions.get(solution).toString());
    }

    @Override
    public void displayDirectory(String path) {
        Path dir = FileSystems.getDefault().getPath(path);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file: stream) {
                out.println(file.getFileName());
            }
            out.flush();
        } catch (IOException | DirectoryIteratorException x) {
            x.printStackTrace();
        }
    }

    @Override
    public void notifyMazeIsReady(String name) {
        this.displayMessage("Maze " + name + " is ready!");
    }

    @Override
    public void displayMaze(Maze3D maze) {
        this.displayMessage(maze.toString());
    }

    @Override
    public void setSolution(String name, Solution<Position> solution) {
        solutions.put(name, solution);
    }

    @Override
    public void displayCrossSection(int [][] mazeSection) {
        for(int i = 0; i < mazeSection.length; i++) {
            out.println("{");
            for (int j = 0; j < mazeSection[i].length; j++)
                out.print(mazeSection[i][j]);
            out.println("}");
        }
        out.flush();
    }

    @Override
    public void displayMessage(String msg) {
        out.println(msg);
        out.flush();
    }

    @Override
	public void printMenu(HashMap<String, Command> cliMapper) {
        out.print("Choose command: (");
        int count = 0;
        for (String command : cliMapper.keySet()) {
        	if (count != cliMapper.keySet().size() - 1) {
        		out.print(command + ",");
        	}
        	else
        		out.print(command);
        	count++;
        }
        out.println(")");
        out.flush();
    }
        
    @Override
	public void update(Observable o, Object arg) {
    	if (o == this.cli) {
    		setChanged();
    		notifyObservers(arg);
    	}
	}
}
