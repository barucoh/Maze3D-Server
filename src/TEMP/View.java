package view;

import java.util.HashMap;

import algorithms.mazeGenerators.Maze2d;

public interface View {
	void notifyMazeIsReady(String name);
	void displayMaze(Maze2d maze);
	void displayMessage(String msg);	
	void start();	
}
