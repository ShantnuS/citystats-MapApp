package view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import controller.Controller;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Controller controller; 
	StatusPanel statusPanel;
	JTabbedPane tabs; 
	MapPanel mapPanel;
	DataPanel dataPanel;
	
	
	public MainFrame() {
		controller = Controller.getInstance();
		
		this.setTitle("MapApp - CityStats");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setSize(1920/2, 1080/2);
		this.setLayout(new BorderLayout());
		this.init();
	}
	
	public void init(){
		//Top status panel
		statusPanel = new StatusPanel();
		
		tabs = new JTabbedPane();
		mapPanel = new MapPanel();
		dataPanel = new DataPanel();
		dataPanel.setBackground(Color.black);
		tabs.add("Map", mapPanel);
		tabs.add("Data", dataPanel);
		
		this.add(tabs, BorderLayout.CENTER);
		this.add(statusPanel, BorderLayout.NORTH);
		
		this.addWindowListener(new CSWindowListener());
		this.addComponentListener(new CSComponentListener());
		
		this.setVisible(true);
		this.repaint();
	}
	
	public DataPanel getDataPanel(){
		return this.dataPanel;
	}
	
	public MapPanel getMapPanel(){
		return this.mapPanel;
	}
	
	public StatusPanel getStatusPanel(){
		return this.statusPanel;
	}
	
	public String selectedTab(){
		return this.tabs.getTitleAt(tabs.getSelectedIndex());
	}
}
