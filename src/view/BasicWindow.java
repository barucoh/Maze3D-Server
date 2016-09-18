package view;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class BasicWindow extends Observable implements Runnable{
	Display display;
	Shell shell;
	
	abstract void initWidgets();
	
	public BasicWindow(int width, int height) {
		this.display = new Display();
		this.shell = new Shell(display);
		
		this.shell.setSize(width, height);
		this.shell.setText("Maze 3D!");
	}
	
	void Run() {
		initWidgets();
		this.shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		display.dispose();
	}
}
