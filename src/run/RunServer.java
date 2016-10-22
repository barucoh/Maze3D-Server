package run;

import server.MyServer;
import view.ServerWindow;

/**
 * Created by Ohad on 10/09/2016.
 */
public class RunServer {
    public static void main(String[] args) throws Exception{
		System.out.println("Server Side");
		System.out.println("type \"close the server\" to stop it");
		
		MyServer server = new MyServer(5400, 10);
		ServerWindow window = new ServerWindow();
		window.setServer(server);
		
		server.start();
		window.start();		
    	
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
