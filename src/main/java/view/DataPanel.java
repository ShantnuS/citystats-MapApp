package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import controller.Controller;
import controller.ETCHelper;
import controller.SQLManager;
import model.CSData;
import model.TTNDevice;

public class DataPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	String[] views = {"Bar graph", "Line graph", "Pie chart", "Table view", "All views"};

	JComboBox<String> viewBox;
	JComboBox<String> deviceBox;
	JComboBox<String> variableBox;
	JButton saveButton;
	
	JPanel settingsPanel;
	JPanel graphPanel;

	JPanel panel1;
	JPanel panel3;
	JPanel panel2;
	JPanel panel4;
	
	JFreeChart lineChart;
	ChartPanel barChartPanel;
	ChartPanel pieChartPanel;
	ChartPanel lineChartPanel;
	JScrollPane scrollPane;
	
	JPanel allPanel;

	boolean initialised = false;

	public DataPanel(){
		deviceBox = new JComboBox<String>(Controller.getInstance().getDeviceIDs());
		variableBox = new JComboBox<String>(Controller.getInstance().getVariables());
		viewBox = new JComboBox<String>(views);
		saveButton = new JButton("Save as image...");
		settingsPanel = new JPanel();
		graphPanel = new JPanel();

		deviceBox.setSelectedIndex(Controller.getInstance().getDeviceIDs().length-1);
		
		deviceBox.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				TTNDevice device = Controller.getInstance().getDevice((String) deviceBox.getSelectedItem());
				try {
					getSingleDeviceData(device);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				refresh();
				//generateGraphPanel(device, (String) variableBox.getSelectedItem(),(String) viewBox.getSelectedItem());
			}
		});

		variableBox.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				generateGraphCharts((String) viewBox.getSelectedItem());
			}
		});

		viewBox.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				generateGraphCharts((String) viewBox.getSelectedItem());
			}
		});	
		
		saveButton.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				new Thread(){
					public void run() {
						try {
							saveImage();
						} catch (IOException e) {
							System.err.println("Could not save as image!");
							e.printStackTrace();
						}
					}
				}.start();
			}
		});	
		
		settingsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingsPanel.add(deviceBox);
		settingsPanel.add(variableBox);
		settingsPanel.add(viewBox);
		settingsPanel.add(saveButton);

		graphPanel.setBackground(Color.orange);
		graphPanel.setLayout(new BorderLayout());

		this.setLayout(new BorderLayout());
		this.add(settingsPanel, BorderLayout.NORTH);
		this.add(graphPanel, BorderLayout.CENTER);

		TTNDevice device = Controller.getInstance().getDevice((String) deviceBox.getSelectedItem());
		generateGraphPanel(device, (String) variableBox.getSelectedItem(),(String) viewBox.getSelectedItem());

		this.repaint();
		initialised = true;
	}

	//Readies the dataset
	public void generateGraphPanel(TTNDevice device, String variable, String view){
		System.out.println("Generating from device, variable and view");

		//Fill data history from SQL database
		new Thread(){
			public void run() {
				try {
					//SQLManager.getDeviceData(device);
					getAllDeviceData();
					generateGraphCharts(view);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void getAllDeviceData() throws IOException{
		TTNDevice d;
		for(String id: Controller.getInstance().getAllDevices().keySet()){
			d = Controller.getInstance().getAllDevices().get(id);
			SQLManager.getDeviceData(d);
		}
	}
	
	public void getSingleDeviceData(String id) throws IOException{
		TTNDevice d;
		d = Controller.getInstance().getAllDevices().get(id);
		SQLManager.getDeviceData(d);
	}
	
	public void getSingleDeviceData(TTNDevice d) throws IOException{
		SQLManager.getDeviceData(d);
	}

	//Displays the dataset in graph
	public void generateGraphCharts(String view){
		System.out.println("Generating from view");
		graphPanel.removeAll();
		
		if(view.equals(views[0])){
			//BAR CHART
			barChartPanel = new ChartPanel(createBarChart());    
			graphPanel.add(barChartPanel, BorderLayout.CENTER);
			barChartPanel.setPreferredSize(graphPanel.getSize());  
			graphPanel.setBackground(Color.black);
			Controller.getInstance().getMainFrame().repaint();
		}

		else if(view.equals(views[1])){
			//LINE CHART
			lineChart = createLineChart();
			
			XYPlot plot = (XYPlot)lineChart.getPlot();
			plot.setBackgroundPaint(new Color(255,228,196));

			lineChartPanel = new ChartPanel(lineChart);

			graphPanel.add(lineChartPanel, BorderLayout.CENTER);
			lineChartPanel.setPreferredSize(graphPanel.getSize());  
			graphPanel.setBackground(Color.black);
			Controller.getInstance().getMainFrame().repaint();
		}

		else if(view.equals(views[2])){
			//PIE CHART
			pieChartPanel = new ChartPanel(createPieChart());    
			graphPanel.add(pieChartPanel, BorderLayout.CENTER);
			pieChartPanel.setPreferredSize(graphPanel.getSize());  
			graphPanel.setBackground(Color.pink);
			Controller.getInstance().getMainFrame().repaint();
		}

		else if(view.equals(views[3])){
			//TABLE VIEW
			scrollPane = new JScrollPane(createTable()); 
			graphPanel.add(scrollPane, BorderLayout.CENTER);
			scrollPane.setPreferredSize(graphPanel.getSize());  
			graphPanel.setBackground(Color.green);
			Controller.getInstance().getMainFrame().repaint();
		}

		else if(view.equals(views[4])){
			System.err.println("Show all graphs");
			graphPanel.setBackground(Color.black);
			allPanel = new JPanel();
			allPanel.setLayout(new GridLayout(2,2));

			panel1 = new JPanel();
			panel2 = new JPanel();
			panel3 = new JPanel();
			panel4 = new JPanel();

			panel1.setLayout(new BorderLayout());
			panel2.setLayout(new BorderLayout());
			panel3.setLayout(new BorderLayout());
			panel4.setLayout(new BorderLayout());

			panel1.setBackground(Color.BLUE);
			panel2.setBackground(Color.yellow);
			panel3.setBackground(Color.red);
			panel4.setBackground(Color.GREEN);

			lineChart = createLineChart();
			
			XYPlot plot = (XYPlot)lineChart.getPlot();
			plot.setBackgroundPaint(new Color(255,228,196));
			
			barChartPanel = new ChartPanel(createBarChart());
			pieChartPanel = new ChartPanel(createPieChart());
			lineChartPanel = new ChartPanel(lineChart);
			scrollPane = new JScrollPane(createTable()); 
			

			panel1.add(barChartPanel, BorderLayout.CENTER);
			panel2.add(pieChartPanel, BorderLayout.CENTER);
			panel3.add(lineChartPanel, BorderLayout.CENTER);
			panel4.add(scrollPane,BorderLayout.CENTER);	

			allPanel.add(panel1);
			allPanel.add(panel2);
			allPanel.add(panel3);
			allPanel.add(panel4);

			allPanel.setPreferredSize(graphPanel.getSize());
			graphPanel.add(allPanel, BorderLayout.CENTER);
			Controller.getInstance().getMainFrame().repaint();
		}
	}
	
	private JFreeChart createBarChart(){
		System.err.println("Creating bar chart");
		return ChartFactory.createBarChart(
				"Average vs Latest",           
				(String) variableBox.getSelectedItem(),            
				(String) deviceBox.getSelectedItem(),            
				createBarDataset(),          
				PlotOrientation.VERTICAL,           
				true, true, false);
	}
	
	private JFreeChart createLineChart(){
		System.err.println("Creating line graph");

		return ChartFactory.createTimeSeriesChart(
				"Time Series Graph - " + (String) variableBox.getSelectedItem(), // Chart
				"Date", // X-Axis Label
				(String) variableBox.getSelectedItem(), // Y-Axis Label
				createLineDataset());
	}
	
	private JFreeChart createPieChart(){
		System.err.println("Creating pie chart");
		
		return ChartFactory.createPieChart(      
				"Amount of data received",   
				createPieDataset(),             
				true,               
				true, 
				false);
	}

	private JTable createTable(){
		System.err.println("Creating table view ");
		TTNDevice device = Controller.getInstance().getDevice((String) deviceBox.getSelectedItem());
		String[][] data = new String[device.getAllData().size()][];
		//String[] titles = {"Date and Time", "Temperature", "Light", "Humidity", "Pressure", "Altitude", "Tilt", "Voltage"};
		String[] titles = {"Date and Time", "Temperature", "Light", "Humidity", "Pressure", "Tilt", "Voltage"};
		int count = 0;
		for(CSData d: device.getAllData()){
			data[count] = d.getAsStringArray();
			count++;
		}
		return new JTable(data,titles);    
	}
	
	private CategoryDataset createBarDataset( ) {
		final String average = "Average";        
		final String latest = "Latest";  
		final String avg = "";        
		final String ltst = ""; 
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 

		String variable = (String) variableBox.getSelectedItem();           
		TTNDevice device = Controller.getInstance().getDevice((String) deviceBox.getSelectedItem());

		double latestDouble = 0;
		dataset.addValue(ETCHelper.getAverageValue(device, variable), average, avg);

		if(device.getLatestData()!=null)
			latestDouble = ETCHelper.getValueFromName(device, variable);

		dataset.addValue(latestDouble, latest, ltst);
		return dataset;
	}

	private PieDataset createPieDataset(){
		DefaultPieDataset dataset = new DefaultPieDataset( );
		HashMap<String, TTNDevice> devices = Controller.getInstance().getAllDevices();

		for(String e:devices.keySet()){
			dataset.setValue(e, devices.get(e).getAllData().size());
		}

		return dataset;
	}

	private XYDataset createLineDataset() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		String variable = (String) variableBox.getSelectedItem();           
		TTNDevice device = Controller.getInstance().getDevice((String) deviceBox.getSelectedItem());
		TimeSeries series = new TimeSeries(variable);
		int second,minute,hour,day,month,year;
		String[] parts;
		double value;
		for(CSData d: device.getAllData()){
			//Extract time and date in correct format
			parts = d.getDate().split("@");
			second = Integer.parseInt(parts[0].split(":")[2]);
			minute = Integer.parseInt(parts[0].split(":")[1]);
			hour = Integer.parseInt(parts[0].split(":")[0]);
			day = Integer.parseInt(parts[1].split("-")[2]);
			month = Integer.parseInt(parts[1].split("-")[1]);
			year = Integer.parseInt(parts[1].split("-")[0]);

			value = ETCHelper.getValueFromName(d, variable);
			if(value != 0)
				series.add(new Second(second,minute,hour,day,month,year), value);
		}

		dataset.addSeries(series);
		return dataset;
	}

	public void saveImage() throws IOException{
		BufferedImage image = new BufferedImage(graphPanel.getWidth(), graphPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		graphPanel.paint(g);
		
		String directory = System.getProperty("user.home");
    	JFileChooser myFileChooser = new JFileChooser(directory + "\\Desktop");
    	
    	int retVal = myFileChooser.showSaveDialog(null);
    	if(retVal==JFileChooser.APPROVE_OPTION){
    		File myImage = new File(myFileChooser.getSelectedFile()+".png");
    	    ImageIO.write(image, "png", myImage);
    	    JOptionPane.showMessageDialog(null,"Image saved successfully!");
    	 }
	}
	
	public void refresh(){
		if(initialised)
			generateGraphCharts((String) viewBox.getSelectedItem());
	}
}
