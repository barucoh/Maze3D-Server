package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

import view.View;

public class Maze3DHandler extends Observable implements ClientHandler, Observer {

	BufferedReader in;
	private PrintWriter out;
	
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
		try{
			this.in = new BufferedReader(new InputStreamReader(inFromClient));
			this.out = new PrintWriter(outToClient);
			String input = "";
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
			in.close();
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Observable updateFrom, Object objToSend) {
		if (objToSend instanceof View) {
			this.out.println(objToSend);
			this.out.flush();
		}
	}
}
