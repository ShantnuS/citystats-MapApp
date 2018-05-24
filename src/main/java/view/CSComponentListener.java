package view;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import controller.Controller;

public class CSComponentListener implements ComponentListener{

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		if(Controller.getInstance().getMainFrame()!= null)
			if(Controller.getInstance().getMainFrame().selectedTab().equals("Data"))
				Controller.getInstance().getMainFrame().getDataPanel().refresh();;
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

}
