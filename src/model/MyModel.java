package model;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

import algorithms.IO.MyCompressorOutputStream;
import algorithms.IO.MyDecompressorInputStream;
import algorithms.demo.Maze3DSearchable;
import algorithms.mazeGenerators.ChooseRandomNode;
import algorithms.mazeGenerators.GrowingTreeGenerator3D;
import algorithms.mazeGenerators.Maze3D;
import algorithms.mazeGenerators.Position;
import algorithms.search.BFS;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import presenter.Presenter;
import view.View;

/**
 * MyModel
 * <p>Implements all of Model's functions and handling all the program's backend tasks and IO using multi threading.
 * <p>Created by Ohad on 15/09/2016.
 * @author Ohad
 * @version 1.0
 * @see View
 * @see Controller
 */
public class MyModel extends Observable implements Model {
    Presenter presenter;
    private Map<String, Maze3DSearchable> mazes;
    private List<GenerateMazeRunnable> generateMazeTasks = new ArrayList<>();
    private List<SaveMazeRunnable> saveMazeTasks = new ArrayList<>();
    private List<LoadMazeRunnable> loadMazeTasks = new ArrayList<>();
    private List<Thread> threads = new ArrayList<>();
    

    public MyModel() {
        this.mazes = new ConcurrentHashMap<>();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    class GenerateMazeRunnable implements Runnable {

        private int cols, rows, layers;
        private String name;
        private GrowingTreeGenerator3D gen;

        public GenerateMazeRunnable(int cols, int rows, int layers, String name) {
            this.cols = cols;
            this.rows = rows;
            this.layers = layers;
            this.name = name;
        }

        @Override
        public void run() {
            gen = new GrowingTreeGenerator3D(new ChooseRandomNode());
            Maze3D maze = gen.generate(cols, rows, layers);
            mazes.put(name, new Maze3DSearchable(maze));
            presenter.notifyMazeIsReady(name);
        }

        public void terminate() {
            gen.setDone(true);
        }
    }
    class SolveMazeRunnable implements Runnable {

        private int cols, rows, layers;
        private String name;
        private Searcher<Position> strategy;

        public SolveMazeRunnable(int cols, int rows, int layers, String name, String strategy) {
            this.cols = cols;
            this.rows = rows;
            this.layers = layers;
            this.name = name;
            this.strategy = strategy;
        }

        @Override
        public void run() {
            Maze3DSearchable maze = mazes.get(name);
            BFS<Position> bfs = new BFS<>();
            Solution<Position> sol = bfs.search(maze);
            presenter.setSolution(mazeName, sol);
            presenter.generalNotification("Maze " + mazeName + " has been solved!");
        }

        public void terminate() {
            gen.setDone(true);
        }
    }
    @Override
    public Solution<Position> solveMaze(String mazeName) {
        Maze3DSearchable maze = this.mazes.get(mazeName);
        BFS<Position> bfs = new BFS<>();
        Solution<Position> sol = bfs.search(maze);
        presenter.setSolution(mazeName, sol);
        presenter.generalNotification("Maze " + mazeName + " has been solved!");
    }

    @Override
    public void generateMaze(String name, int cols, int rows, int layers) {
        GenerateMazeRunnable generateMazeTask = new GenerateMazeRunnable(cols, rows, layers, name);
        generateMazeTasks.add(generateMazeTask);
        Thread thread = new Thread(generateMazeTask);
        thread.start();
        threads.add(thread);
    }

    @Override
    public Maze3D getMaze(String name) {
        return mazes.get(name).getMaze();
    }

    @Override
    public void exit() {
        for (GenerateMazeRunnable task : generateMazeTasks) {
            task.terminate();
        }
        for (SaveMazeRunnable task : saveMazeTasks) {
            task.terminate();
        }
        for (LoadMazeRunnable task : loadMazeTasks) {
            task.terminate();
        }
    }

    class SaveMazeRunnable implements Runnable {

        private String name;
        private String fileName;
        MyCompressorOutputStream out = null;

        public SaveMazeRunnable(String name, String fileName) {
            this.name = name;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            if (mazes.containsKey(name)) {
                try {
                    out = new MyCompressorOutputStream(new FileOutputStream(fileName));
                    byte[] arr = mazes.get(name).getMaze().toByteArray();
                    out.write(arr.length / 255);
                    out.write(arr.length % 255);
                    out.write(arr);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    close(out);
                }
                presenter.generalNotification("Maze " + name + " saved successfully to " + fileName);
            } else
                presenter.generalNotification("Maze " + name + " not found");
        }

        public void terminate() {
            out.setDone(true);
        }
    }

    @Override
    public void saveMaze(String name, String fileName) {
        SaveMazeRunnable saveMazeRunnable = new SaveMazeRunnable(name, fileName);
        saveMazeTasks.add(saveMazeRunnable);
        Thread thread = new Thread(saveMazeRunnable);
        thread.start();
        threads.add(thread);
    }

    class LoadMazeRunnable implements Runnable {

        private String name;
        private String fileName;
        MyDecompressorInputStream in = null;

        public LoadMazeRunnable(String name, String fileName) {
            this.name = name;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            if (!mazes.containsKey(name)) {
                try {
                    in = new MyDecompressorInputStream(new FileInputStream(fileName));
                    int size = in.read() * 255;
                    size += in.read();
                    byte[] mazeInBytes = new byte[size];
                    in.read(mazeInBytes);
                    mazes.put(name, new Maze3DSearchable(new Maze3D(mazeInBytes)));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    close(in);
                }
                presenter.generalNotification("Maze " + name + " loaded successfully from " + fileName);
            } else
                presenter.generalNotification("Maze " + name + " already loaded");
        }

        public void terminate() {
            in.setDone(true);
        }
    }

    @Override
    public void loadMaze(String name, String fileName) {
        LoadMazeRunnable loadMazeRunnable = new LoadMazeRunnable(name, fileName);
        loadMazeTasks.add(loadMazeRunnable);
        Thread thread = new Thread(loadMazeRunnable);
        thread.start();
        threads.add(thread);
    }

    public int [][] getCrossSection(String name, String section, int index) {
        int [][] arr;
        section = section.toUpperCase();
        switch (section) {
            case "X": arr = mazes.get(name).getMaze().getCrossSectionByX(index);
                break;
            case "Y": arr = mazes.get(name).getMaze().getCrossSectionByY(index);
                break;
            case "Z": arr = mazes.get(name).getMaze().getCrossSectionByZ(index);
                break;
            default:
                arr = new int[0][0];
        }
        return arr;
    }
    
    private static void close(Closeable c) {
    if (c == null) return;
    try {
        c.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}