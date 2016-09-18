package view;

import controller.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ohad on 10/09/2016.
 */
public class CLI {
    BufferedReader in;
    PrintWriter out;
    HashMap<String, Command> cliMapper;
    private List<Thread> threads = new ArrayList<Thread>();

    public CLI(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    private void printMenu() {
        out.print("Choose command: (");
        for (String command : cliMapper.keySet()) {
            out.print(command + ",");
        }
        out.println(")");
        out.flush();
    }

    public void Start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String input = "";
                String cmd = "";
                printMenu();
                do
                {
                    try {
                        input = in.readLine();
                        cmd = input.split(" ")[0];
                        if (cliMapper.containsKey(cmd)) {
                            String[] args = input.substring(input.indexOf(" ") + 1).split(" ");
                            cliMapper.get(cmd).doCommand(args);
                        }
                        else if (!cmd.toUpperCase().equals("EXIT")) {
                            out.println("Bad command");
                            out.flush();
                            printMenu();
                        }
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    finally {
                        input = "";
                    }
                }while (!cmd.toUpperCase().equals("EXIT"));
            }
        });
        thread.start();
        threads.add(thread);
    }

    public void setCommands(HashMap<String, Command> cliMapper) {
        this.cliMapper = cliMapper;
    }
}
