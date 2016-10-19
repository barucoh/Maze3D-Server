package view;

import java.util.Observable;

import algorithms.mazeGenerators.Maze3D;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import model.Model;
import server.Maze3DHandler;

/**
 * MyView
 * <p>Implements all of View's functions and handling all the program's UI / UX.
 * 
 * @author Afik & Ohad
 * @see Model
 * @see Controller
 */
public class OutputToClient extends Observable implements View {
    private Maze3DHandler clientHandler;
    Object [] objToSend;
    
	private String selectedMazeName;
    
    public OutputToClient(Maze3DHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void start() { }

    /* Given a new position, A command sent to 
	 * move the character there.
	 */
	@Override
	public void setNextStep(Position nextStep) {
		objToSend = new Object[5];
		objToSend[0] = "character_moved";
		objToSend[1] = selectedMazeName;
		objToSend[2] = nextStep.x;
		objToSend[3] = nextStep.y;
		objToSend[4] = nextStep.z;
		clientHandler.updateClient(objToSend);
	}
	/**
	 * This method will start a timer task and every 500 ms
	 * move the character to the next position based on the 
	 * solution
	 * 
	 * @param solution	The maze solution sent from the model.
	 * @see Solution
	 */
	@Override
	public void displaySolution(Solution<Position> solution) {
		objToSend = new Object[2];
		objToSend[0] = "display_solution";
		objToSend[1] = solution.getStates();
		clientHandler.updateClient(objToSend);
	}
	@Override
	public void displayDirectory(String path) { }
	
	/**
	 * Once the new generated maze is ready, we can enable all
	 * the related buttons and save it to the combo box
	 * 
	 * @param mazeName The name of the maze that is ready
	 */
	@Override
	public void notifyMazeIsReady(String mazeName) {
		objToSend = new Object[2];
		objToSend[0] = "maze_ready";
		objToSend[1] = mazeName;
		clientHandler.updateClient(objToSend);

		setChanged();
		notifyObservers("get_maze " + mazeName);
		
		setChanged();
		notifyObservers("display_cross_section " + mazeName + " Z 1"); //instead of 1 it's supposed to be nubmer of floor
		
//		this.goalPosition = selectedMaze.getGoalPosition();
//		this.startPosition = selectedMaze.getStartPosition();
	}
	@Override
	public void displayMaze(Maze3D maze) { }
	
	/**
	 * Set the data of the current running maze to what we got
	 * back from the model.
	 * 
	 * @param mazeSection The level which the character is currently on.
	 * @param mazeFloorUp The floor above
	 * @param mazeFloorDown The floor below
	 */
	@Override
	public void displayCrossSection(int[][] mazeSection, int [][] mazeFloorUp, int [][] mazeFloorDown) {
		objToSend = new Object[5];
		objToSend[0] = "display_cross_section";
		objToSend[1] = mazeSection;
		objToSend[2] = mazeFloorUp;
		objToSend[3] = mazeFloorDown;
		clientHandler.updateClient(objToSend);
	}
	
	/**
	 * Set the current active maze
	 * 
	 * @param Maze3D Instance of the current maze
	 */
	@Override
	public void setSelectedMaze(String name, Maze3D maze) {
		objToSend = new Object[3];
		objToSend[0] = "get_maze";
		objToSend[1] = name;
		objToSend[2] = maze;
		clientHandler.updateClient(objToSend);
	}
	
	/**
	 * A better way to interact with the user.
	 * 
	 * @param msg A string to display to the user.
	 */
	@Override
	public void displayMessage(String msg) {
		objToSend = new Object[2];
		objToSend[0] = "message";
		objToSend[1] = msg;
	}
	
	@Override
	public void setSolutionAvailable(boolean solutionAvailable) {
		//setChanged();
		//notifyObservers(solutionAvailable);
	}

	@Override
	public void moveCharacter(Position position) {
		setNextStep(position);
	}
	
    
    
    
    
    
    
    /*
    @Override
    public void displaySolution(Solution<Position> solution) {
        //this.displayMessage(solution);
    }
    
    @Override
    public void setSolutionAvailable(boolean solutionAvailable) {} ;
    
    @Override
    public void setNextStep(Position nextStep) {};

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
    	if (maze == null)
    		this.displayMessage("No such maze found");
    	else
    		this.displayMessage(maze.toString());
    }

    @Override
    public void displayCrossSection(int [][] mazeSection, int [][] floorUp, int [][] floorDown) {
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
    public void setSelectedMaze(Maze3D maze) {
    		this.selectedMaze = maze;
    }
        
    @Override
	public void update(Observable o, Object arg) {
    	if (o == this.cli) {
    		setChanged();
    		notifyObservers(arg);
    	}
	}*/
}