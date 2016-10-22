package server;

public class ClientHandlerFactory {
	public static ClientHandler getHandler(String searcherName) {
		 if(searcherName == null){
		    return null;
		 }		
		 if(searcherName.equalsIgnoreCase("Maze3D")){
		    return new Maze3DHandler();
		 }
		  
		 return null;
	}
}
