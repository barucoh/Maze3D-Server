package presenter;

import presenter.Command;

/**
 * Created by Ohad on 10/09/2016.
 */
public abstract class CommonCommand implements Command {
    protected boolean visible;
    
    @Override
    public boolean isVisible() {
    	return this.visible;
    }

    @Override
    public void setVisibility(boolean visible) {
    	this.visible = visible;
    }
}
