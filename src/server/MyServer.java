package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import model.MyModelServer;
import presenter.Presenter;
import view.OutputToClient;

public class MyServer {

	int port;
	public static ServerSocket server;
	
	int numOfClients;
	public static HashMap<String, Object[]> clients;
	ExecutorService threadpool;
	
	volatile boolean stop;
	
	Thread mainServerThread;
	
	int clientsHandled = 0;
	
	public MyServer(int port, int numOfClients) {
		this.port = port;
		this.numOfClients = numOfClients;
		clients = new HashMap<String, Object[]>();
	}
	
	
	public void start() throws Exception{
		server = new ServerSocket(port);
		server.setSoTimeout(10*1000);
		threadpool = Executors.newFixedThreadPool(numOfClients);
		
		mainServerThread = new Thread(new Runnable() {			
			@Override
			public void run() {
				while(!stop){
					try {
						final Socket someClient = server.accept();
						if(someClient!=null){
							someClient.setSoTimeout(10 * 1000);
							threadpool.execute(new Runnable() {									
								@Override
								public void run() {
									try {
										clientsHandled++;
										System.out.println("\thandling client " + clientsHandled);
										Object[] clientsValues = new Object[2];
										clientsValues[0] = someClient;
										clientsValues[1] = Thread.currentThread();
										clients.put(someClient.getInetAddress() + ":" + someClient.getPort(), clientsValues);
										Maze3DHandler clientHandler = new Maze3DHandler();
										
										OutputToClient view = new OutputToClient(clientHandler);
										MyModelServer model = new MyModelServer();
										
										Presenter presenter = new Presenter(model, view);
										model.addObserver(presenter);
										view.addObserver(presenter);
										
										clientHandler.addObserver(presenter);
										clientHandler.handleClient(someClient.getInputStream(), someClient.getOutputStream());
									} catch(IOException e) {
										e.printStackTrace();
									} finally {
										try {
										someClient.close();
										clients.remove(someClient);
										System.out.println("\tdone handling client " + clientsHandled);
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
							});
						}
					}
					catch (SocketTimeoutException e){
						System.out.println("no client connected...");
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println("done accepting new clients.");
			} // end of the mainServerThread task
		});
		mainServerThread.start();
	}
	
	public void close() throws Exception{
		stop=true;	
		// do not execute jobs in queue, continue to execute running threads
		System.out.println("shutting down");
		threadpool.shutdownNow();
		// wait 10 seconds over and over again until all running jobs have finished
		/*boolean allTasksCompleted=false;*/
		while(!(/*allTasksCompleted=*/threadpool.awaitTermination(10, TimeUnit.SECONDS)));
		
		System.out.println("all the tasks have finished");

		mainServerThread.join();		
		System.out.println("main server thread is done");
		
		server.close();
		System.out.println("server is safely closed");
	}
}
