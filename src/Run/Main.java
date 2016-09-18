package Run;

import controller.*;
import model.Model;
import model.MyModel;
import view.MyView;
import view.View;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Ohad on 10/09/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException{
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
    }
}
