package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Observable;

public class Maze3DHandler extends Observable implements ClientHandler {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
		try{
			this.in = new ObjectInputStream(inFromClient);
			this.out = new ObjectOutputStream(outToClient);
			Object input;
			String inputStr = "";
            //synchronized(this) {
            	do
	            {
	                input = null;
	                try {
	                	input = in.readObject();
	                	Object [] obj = (Object[]) input;
	                	inputStr = (String)obj[0];
	                	setChanged();
	                	notifyObservers(input);
	                	in.reset();
	                } catch (EOFException ex) {
	                	break;
					} catch (ClassNotFoundException ex) {
	                	ex.printStackTrace();
					} catch (SocketException ex) {
	                    break;
	                } catch (SocketTimeoutException ex) {
	                    if (Thread.interrupted())
	                    	break;
	                } catch (StreamCorruptedException ex) {
	                	ex.printStackTrace();
					} catch (IOException ex) {
	                    ex.printStackTrace();
	                }
	            } while (!inputStr.toUpperCase().equals("EXIT"));
            //}
            updateClient("EXIT");
			in.close();
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateClient(Object objToSend) {
		try {
			this.out.writeObject(objToSend);
			this.out.reset();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
