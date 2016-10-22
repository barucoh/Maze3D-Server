package view;

<<<<<<< HEAD
import java.net.Socket;
=======
import java.io.IOException;
import java.net.SocketException;
>>>>>>> 34b462d626a0e82a8aa25fa205375b2ef394485e
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import server.MyServer;

/**
 * This is the main game window.
 * All the widgets (buttons and display canvas)
 * are defined and initialized in here.
 * 
 * @author Afik & Ohad
 * @see BaseWindow
 * @see View
 */
public class ServerWindow extends BaseWindow {
	

	MyServer server;
	
	int num = 1;
	private static ArrayList<String> users = new ArrayList<String>();
	
	Button btnDisconnectUser, btnShowDataOnUser, btnCloseServer;
	Label lblName, logger;
	Browser browser;
	String currentUserSelected;
	Timer timer;
	
	
	
	//-------Inherited methods------------
	@Override
	protected void initWidgets() {
		
		

		//--------Configuring layouts-------
		shell.setLayout(new GridLayout(2, true));
		
		
		lblName = new Label(shell, SWT.NONE);
		lblName.setText("Logged in users:");
		lblName.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 1, 1));

		
	    try {
	        browser = new Browser(shell, SWT.NONE);
	        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6));
	        browser.setFocus();
	    	} catch (SWTError e) {
	         MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
	         messageBox.setMessage("Browser cannot be initialized.");
	         messageBox.setText("Exit");
	         messageBox.open();
	         System.exit(-1);
	    }
		
		List list = new List(shell, SWT.BORDER | SWT.V_SCROLL);
	    list.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 1, 1));
		
		Text text = new Text(shell, SWT.BORDER);
	    text.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 1, 1));

		
	    btnDisconnectUser = new Button(shell, SWT.PUSH);
	    btnDisconnectUser.setText("Disconnect: user");
	    btnDisconnectUser.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 1, 1));
	    btnDisconnectUser.setEnabled(false);
		
	    btnShowDataOnUser = new Button(shell, SWT.PUSH);
	    btnShowDataOnUser.setText("Show info");
	    btnShowDataOnUser.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 1, 1));
		btnShowDataOnUser.setEnabled(false);
		
		btnCloseServer = new Button(shell, SWT.PUSH);
		btnCloseServer.setText("Close the server");
		btnCloseServer.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 1, 1));
		btnCloseServer.setEnabled(true);
		
		
		logger = new Label(shell, SWT.BORDER);
		logger.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		


        timer = new Timer();
        	timer.scheduleAtFixedRate(new TimerTask() {
            @SuppressWarnings("static-access")
			@Override
            public void run() {
            	
                display.getDefault().asyncExec(new Runnable() {
                	
                    public void run() {
                    		
                    		Set<String> listUsers = new HashSet<String>(Arrays.asList(list.getItems()));
	                		
	                		Set<String> clients = MyServer.clients.keySet();
	                		
	                		if(listUsers != clients){
	                			users.clear();
		                		for (String client : clients) {
		                			users.add(client);
		                		}
		                		list.removeAll();
		                		for (String user : users) { list.add(user); }
	                		}

                    }
                });
            }
        },1000, 5000);
		
        	
		list.addSelectionListener(new SelectionListener() {
	    	
		      public void widgetSelected(SelectionEvent event) {
		    	  	String selectedUser = list.getSelection()[0];
		        text.setText("Selected User: " + selectedUser);
		        btnDisconnectUser.setEnabled(true);
		        btnDisconnectUser.setText("Disconnect: " + selectedUser);
		        btnShowDataOnUser.setEnabled(true);
		        btnShowDataOnUser.setText("Show info on: " + selectedUser);	
		        currentUserSelected = selectedUser;
		      }
		      public void widgetDefaultSelected(SelectionEvent event) {}
		 });
		
		btnShowDataOnUser.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(!currentUserSelected.isEmpty()){
					Object[] clientDetails = MyServer.clients.get(currentUserSelected);
					
					if(clientDetails != null){
						Socket clientSocket = (Socket)clientDetails[0];
						Thread clientThread = (Thread)clientDetails[1];
						
						String ip = clientSocket.getInetAddress().toString();
						int port = clientSocket.getPort();
						String threadName = clientThread.getName();
						
						String content = new String("<html><body><table style='width: 70%;font-size: 18px;margin: 0px auto;margin-top: 50px;'><tr><td>User Show name</td><td>" + ip+":"+port+ "</td></tr><tr><td>Ip address</td><td>" + ip + "</td></tr><tr><td>External Port</td><td>" + port + "</td></tr><tr><td>Thread name</td><td>" + threadName + "</td></tr></table></body></html>");

						browser.setText(content); 
					}
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		btnDisconnectUser.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(!currentUserSelected.isEmpty()){
					Object[] clientDetails = MyServer.clients.get(currentUserSelected);
					
					if(clientDetails != null){
						Socket clientSocket = (Socket)clientDetails[0];
						Thread clientThread = (Thread)clientDetails[1];
						String message = currentUserSelected + " has been disconnected";
						logMessage(message);
						list.remove(currentUserSelected);
						clientThread.interrupt();
						String content = new String("<html><body><h2 style='width: 70%;font-size: 22px;color: red;margin: 0px auto;margin-top: 50px;'>The user \"" + currentUserSelected + "\" has been disconnected successfully!</h2></body></html>");
					}
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		btnCloseServer.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						MessageBox msgBox = new MessageBox(shell);
						try {
							server.close();
						}catch (SocketException e) {
							e.printStackTrace();
						}catch (IOException e) {
							e.printStackTrace();
						}catch (Exception e) {
							e.printStackTrace();
						}
						msgBox.setMessage("Server closed successfully!");
						msgBox.open();
						timer.cancel();
						shell.dispose();
					}
				});
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});

	}
	
	private void logMessage(String message){
		logger.setText(message);
	}

	public void setServer(MyServer server){
		this.server = server;
	}
	
}