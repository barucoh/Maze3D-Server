package presenter;

/**
 * Command interface
 * <p>An interface to implement in case a new Command needs to be programmed
 * <p>Created by Ohad on 15/09/2016.
 * @author Ohad
 * @version 1.0
 * @see Command
 */
public interface Command {
    void doCommand(String[] args);
}