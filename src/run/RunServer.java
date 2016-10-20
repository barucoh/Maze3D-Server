package run;

import model.MyModelServer;
import presenter.Presenter;
import server.*;
import view.OutputToClient;

import java.io.*;

/**
 * Created by Ohad on 10/09/2016.
 */
public class RunServer {
    public static void main(String[] args) throws Exception{
		System.out.println("Server Side");
		System.out.println("type \"close the server\" to stop it");
		Maze3DHandler clientHandler = new Maze3DHandler();

		OutputToClient view = new OutputToClient(clientHandler);
		MyModelServer model = new MyModelServer();
		
		Presenter presenter = new Presenter(model, view);
		model.addObserver(presenter);
		view.addObserver(presenter);
		clientHandler.addObserver(presenter);
		
		MyServer server = new MyServer(5400, clientHandler, 10);
		
		server.start();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		while(!(in.readLine()).equals("close the server"));
		
		server.close();
		
    	
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
