package view;

import algorithms.mazeGenerators.Maze3D;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import controller.Command;
import controller.Controller;
import model.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * MyView
 * <p>Implements all of View's functions and handling all the program's UI / UX.
 * <p>Created by Ohad on 15/09/2016.
 * @author Ohad
 * @version 1.0
 * @see Model
 * @see Controller
 */
public class MyView implements View {
    //private BufferedReader in;
    private PrintWriter out;
    private CLI cli;

    private Controller controller;
    HashMap<String, Boolean> mazesReady;
    private Map<String, Solution<Position>> solutions;

    public MyView(BufferedReader in, PrintWriter out) {
        //this.in = in;
        this.out = out;

        this.cli = new CLI(in, out);
        this.mazesReady = new HashMap<>();
        this.solutions = new HashMap<>();
    }

    @Override
    public void start() {
        mazesReady = new HashMap<>();
        this.cli.Start();
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void displaySolution(String solution) {
        out.println(solutions.get(solution));
        out.flush();
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
        mazesReady.put(name, true);
        out.println("Maze " + name + " is ready!");
        out.flush();
    }

    @Override
    public void displayMaze(Maze3D maze) {
        out.println(maze.toString());
        out.flush();
    }

    @Override
    public void setSolution(String name, Solution<Position> solution) {
        solutions.put(name, solution);
    }

    @Override
    public void setCommands(HashMap<String, Command> commands) {
        cli.setCommands(commands);
    }

    @Override
    public void displayCrossSection(String name, String section, int index) {
        int [][] arr;
        section = section.toUpperCase();
        switch (section) {
            case "X": arr = controller.getMaze(name).getCrossSectionByX(index);
                break;
            case "Y": arr = controller.getMaze(name).getCrossSectionByY(index);
                break;
            case "Z": arr = controller.getMaze(name).getCrossSectionByZ(index);
                break;
            default:
                arr = new int[0][0];
        }
        for(int i = 0; i < arr.length; i++) {
            out.println("{");
            for (int j = 0; j < arr[i].length; j++)
                out.print(arr[i][j]);
            out.println("}");
        }
        out.flush();
    }

    @Override
    public void generalNotification(String msg) {
        out.println(msg);
        out.flush();
    }
}
