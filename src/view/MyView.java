package view;

import algorithms.mazeGenerators.Maze3D;
import presenter.Command;
import server.ClientHandler;
import model.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.HashMap;
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
public class MyView extends Observable implements View{
    //private BufferedReader in;
    //private PrintWriter out;
    //private CLI cli;
    //private Maze3DHandler clientHandler;
    
	public MyView() {}
	
    public MyView(BufferedReader in, PrintWriter out) {
        //this.in = in;
        //this.out = out;
        
        //this.cli = new CLI(in, out);
        //this.cli.addObserver(this);
        
        //this.clientHandler = new Maze3DHandler();
        //this.clientHandler.addObserver(this);
    }

    @Override
    public void setClientObserver(ClientHandler observer) {
    	this.addObserver((Observer) observer);
    }
    
    @Override
    public void displaySolution(String solution) {
        this.displayMessage(solution);
    }

    @Override
    public void displayDirectory(String path) {
    	StringBuilder sb = new StringBuilder();
        Path dir = FileSystems.getDefault().getPath(path);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file: stream) {
                sb.append(file.getFileName());
            }
            this.displayMessage(sb.toString());
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
    	if (maze == null)
    		this.displayMessage("No such maze found");
    	else
    		this.displayMessage(maze.toString());
    }

    @Override
    public void displayCrossSection(int [][] mazeSection) {
    	StringBuilder sb = new StringBuilder();
        for(int i = 0; i < mazeSection.length; i++) {
            sb.append("{");
            for (int j = 0; j < mazeSection[i].length; j++)
                sb.append(mazeSection[i][j]);
            sb.append("}");
        }
        this.displayMessage(sb.toString());
    }

    @Override
    public void displayMessage(String msg) {
        notifyObservers(msg);
    }
    
    @Override
	public void printMenu(HashMap<String, Command> cliMapper) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Choose command: (");
        for (String command : cliMapper.keySet()) {
        	if (cliMapper.get(command).isVisible()) {
        		sb.append(command + ",");
        	}
        }
        sb.append(sb.toString().substring(0, sb.length() - 1) + ")");
        this.displayMessage(sb.toString());
    }
        
//    @Override
//	public void update(Observable updateFrom, Object objToSend) {
//    	if (updateFrom instanceof Model) {
//    		setChanged();
//    		notifyObservers(objToSend);
//    	}
//	}
}
