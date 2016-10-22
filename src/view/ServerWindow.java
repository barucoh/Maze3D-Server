package view;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
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
	
	Timer timer;
	
	
	
	//-------Inherited methods------------
	@Override
	protected void initWidgets() {
		
		users.add("Jeffery");
		users.add("Steff");
		users.add("Afik");
		users.add("Ohad");

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
	    
	    for (String user : users) { list.add(user); }
		
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
            @Override
            public void run() {
                display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                    	logMessage("User " + num + " just logged in");
                    	num++;
                    }
                });
            }
        },1500, 1000);
		
        	
		list.addSelectionListener(new SelectionListener() {
	    	
		      public void widgetSelected(SelectionEvent event) {
		    	  	String selectedUser = list.getSelection()[0];
		        text.setText("Selected User: " + selectedUser);
		        btnDisconnectUser.setEnabled(true);
		        btnDisconnectUser.setText("Disconnect: " + selectedUser);
		        btnShowDataOnUser.setEnabled(true);
		        btnShowDataOnUser.setText("Show info on: " + selectedUser);		        
		      }
		      public void widgetDefaultSelected(SelectionEvent event) {}
		 });
		
		btnShowDataOnUser.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String selectedUser = list.getSelection()[0];
				String ip = "127.0.0.1";

				String content = new String("<html><body><table><tr><td>User Name</td><td>" + selectedUser + "</td></tr><tr><td>Ip address</td><td>" + ip + "</td></tr></table></body></html>");

				browser.setText(content); 
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		btnDisconnectUser.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String selectedUser = list.getSelection()[0];
				timer.cancel();
				String message = selectedUser + " has been disconnected";
				logMessage(message);
				users.remove(selectedUser);
				list.remove(selectedUser);
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