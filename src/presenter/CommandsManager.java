package presenter;

import java.util.HashMap;

import model.Model;
import view.CLI;
import view.View;


/**
 * Commands Manager
 * <p>Class based on the Command Design Pattern. Aggregates many underlying commands to be processed and sent to the
 * Presenter for further execution. The Commands are received from a CLI / GUI input.
 * <p>Created by Ohad on 15/09/2016.
 * @author Ohad
 * @version 1.0
 * @see Command
 * @see CLI
 * @see Presenter
 */
public class CommandsManager {
	private Model model;
	private View view;

    public CommandsManager(Model model, View  view) {
        this.model = model;
        this.view = view;
    }

    public HashMap<String, Command> getCommandsMap() {
        HashMap<String, Command> commands = new HashMap<String, Command>();
        commands.put("dir", new DirCommand());
        commands.put("generate_3d_maze", new Generate3DMazeCommand());
        commands.put("display", new DisplayMazeCommand());
        commands.put("display_cross_section", new DisplayCrossSectionCommand());
        commands.put("save_maze", new SaveMazeCommand());
        commands.put("load_maze", new LoadMazeCommand());
        commands.put("solve", new SolveMazeCommand());
        commands.put("display_solution", new DisplaySolutionCommand());
        commands.put("exit", new ExitCommand());

        return commands;
    }

    public class DirCommand extends CommonCommand {
        @Override
        public void doCommand(String[] args) {
        	model.displayDirectory(args[0]);
        }
    }

    public class DisplayCrossSectionCommand extends CommonCommand {

        @Override
        public void doCommand(String[] args) {
        	presenter.displayCrossSection(args[0], args[1], Integer.parseInt(args[2]));
        }
    }

    public class DisplayMazeCommand extends CommonCommand {

        @Override
        public void doCommand(String[] args) {
            presenter.displayMaze(args[0]);
        }
    }

    public class DisplaySolutionCommand extends CommonCommand {

        @Override
        public void doCommand(String[] args) {
            presenter.displaySolution(args[0]);
        }
    }

    public class ExitCommand extends CommonCommand {

        @Override
        public void doCommand(String[] args) {
            presenter.exit();
        }
    }

    public class Generate3DMazeCommand extends CommonCommand {

        @Override
        public void doCommand(String[] args) {
            String name = args[0];
            int cols = Integer.parseInt(args[1]);
            int rows = Integer.parseInt(args[2]);
            int layers = Integer.parseInt(args[3]);
            presenter.generateMaze(name, cols, rows, layers);
        }
    }

    public class LoadMazeCommand extends CommonCommand {

        @Override
        public void doCommand(String[] args) {
            presenter.loadMaze(args[0], args[1]);
        }
    }

    public class SaveMazeCommand extends CommonCommand {

        @Override
        public void doCommand(String[] args) {
            presenter.saveMaze(args[0], args[1]);
        }
    }

    public class SolveMazeCommand extends CommonCommand {

        @Override
        public void doCommand(String[] args) {
            presenter.solveMaze(args[0]);
        }
    }
}