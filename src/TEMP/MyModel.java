package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.Maze2d;

public class MyModel extends Observable implements Model {
	
	private ExecutorService executor;
	
	public MyModel() {
		executor = Executors.newFixedThreadPool(50);
	}		
		
	private Map<String, Maze2d> mazes = new ConcurrentHashMap<String, Maze2d>();
			
	@Override
	public void generateMaze(String name, int rows, int cols) {
		executor.submit(new Callable<Maze2d>() {

			@Override
			public Maze2d call() throws Exception {
				GrowingTreeGenerator generator = new GrowingTreeGenerator();
				Maze2d maze = generator.generate(rows, cols);
				mazes.put(name, maze);
				
				setChanged();
				notifyObservers("maze_ready " + name);		
				return maze;
			}
			
		});
			
	}

	@Override
	public Maze2d getMaze(String name) {
		return mazes.get(name);
	}
	
	public void exit() {
		executor.shutdownNow();
	}
}
