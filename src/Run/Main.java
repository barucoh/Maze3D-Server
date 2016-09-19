package Run;

import model.MyModel;
import presenter.Presenter;
import view.MyView;

import java.io.*;

/**
 * Created by Ohad on 10/09/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException{
        
    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(System.out);
				
		MyView view = new MyView(in, out);
		MyModel model = new MyModel();
		
		Presenter presenter = new Presenter(model, view);
		model.addObserver(presenter);
		view.addObserver(presenter);
				
		view.start();
    	
    	
    	
    	
    	
    	
    	/*
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        Controller ctrl = new MyController();
        View view = new MyView(in, out);
        Model model = new MyModel();

        CommandsManager cmdMgr = new CommandsManager(ctrl);
        HashMap<String, Command> cliMapper = cmdMgr.getCommandsMap();

        view.setCommands(cliMapper);

        ctrl.setView(view);
        ctrl.setModel(model);

        view.setPresenter(ctrl);
        model.setPresenter(ctrl);

        view.start();
        */
    }
}
