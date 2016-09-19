package model;

import algorithms.mazeGenerators.Maze2d;

public interface Model {
	void generateMaze(String name, int rows, int cols);
	Maze2d getMaze(String name);
	void exit();	
}
