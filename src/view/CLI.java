package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;

/**
 * Created by Ohad on 10/09/2016.
 */
public class CLI extends Observable{
    BufferedReader in;
    PrintWriter out;

    public CLI() {
        this.in = null;
        this.out = null;
    }
    
    public CLI(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    public void Start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String input = "";
                setChanged();
                notifyObservers("print_menu");
                do
                {
                    input = "";
                    try {
                    	input = in.readLine();
                    	setChanged();
                    	notifyObservers(input);
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }while (!input.split(" ")[0].toUpperCase().equals("EXIT"));
            }
        });
        thread.start();
    }
}
