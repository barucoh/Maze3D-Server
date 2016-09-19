package presenter;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

public class Presenter implements Observer {
	private Model model;
	private View view;
	private CommandsManager commandsManager;
	private HashMap<String, Command> cliMapper;
	
	public Presenter(Model model, View view) {
		this.model = model;
		this.view = view;
		
		commandsManager = new CommandsManager(model, view);
		cliMapper = commandsManager.getCommandsMap();
	}

	@Override
	public void update(Observable o, Object arg) {
	    String input = (String)arg;
	    String cmdStr = input.split(" ")[0];
        String[] args = null;
	    Command command = cliMapper.get("print_menu");
	    
		if (!cliMapper.containsKey(cmdStr)) {
			view.displayMessage("Command doesn't exist");
			command = cliMapper.get("print_menu");
		}
		else {
	        args = input.substring(input.indexOf(" ") + 1).split(" ");
	        cliMapper.get(cmdStr).doCommand(args);
			command = cliMapper.get(cmdStr);
		}
		command.doCommand(args);
	}
}
