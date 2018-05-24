package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.teamdev.jxmaps.Icon;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Size;

import model.OWMDevice;
import model.TTNClient;
import model.TTNDevice;
import view.MainFrame;

public class Controller {

	TTNClient client;
	String region;
	String appId;
	String accessKey;
	MainFrame frame;
	HashMap<String, TTNDevice> devices;
	boolean status; //true if connected
	String[] variables = {"Temperature", "Light", "Humidity", "Pressure", "Tilt", "Voltage"};
	String currentVariable;
	
	private Controller() {
		devices = new HashMap<String, TTNDevice>();
		status = false;
		currentVariable = variables[0];
		try {
			this.createDevices();
		} catch (Exception e) {
			System.err.println("Could not create devices from file!");
			e.printStackTrace();
		}	
		
		System.out.println("Controller is ready!");
	}
	
	private static Controller instance;
	
	public static Controller getInstance() {
		if(instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	public void startUI() {
		frame = new MainFrame();
	}
	
	public void startClient() {
		if(region != null && appId != null && accessKey != null) {
			client = new TTNClient(region, appId, accessKey);
		}
		else {
			System.err.println("Please set Region, ID and Key using setRIK()");
		}
	}
	
	//Set Region, ID, and Key. Do this before starting the client
	public void setRIK(String region, String appId, String accessKey) {
		this.region = region;
		this.appId = appId;
		this.accessKey = accessKey;
	}
	
	public TTNDevice getDevice(String deviceID) {
		return devices.get(deviceID);
	}
	
	public void createDevices() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("res\\devices.txt"));
		String line = null;  
		while ((line = br.readLine()) != null)  {  
			String[] parts = line.split(" ");
			String deviceID = parts[0];
			String latitude = parts[1];
			String longitude = parts[2];
			TTNDevice device = new TTNDevice(deviceID, latitude, longitude);
			this.addDevice(device);
			System.out.println("Added device: " + deviceID);
		} 
		br.close();
	}
	
	public void initOWM() throws IOException{
		OWMDevice device;
		LatLng latlng;
		for(String city : OWMCityList.getCities()){
			device = new OWMDevice(city);
			latlng = new LatLng(Double.parseDouble(device.getLatitude()),Double.parseDouble(device.getLongitude()));
			frame.getMapPanel().createMarkerOWM(latlng, device);
		}
	}
	
	public void addDevice(TTNDevice device) {
		String deviceID = device.getDeviceID();
		devices.put(deviceID, device);
	}
	
	public String[] getDeviceIDs(){
		return devices.keySet().toArray(new String[devices.keySet().size()]);
	}
	
	public void printAllDevices() {
		TTNDevice d;
		for(String id: devices.keySet()){
			d = devices.get(id);
			System.out.println(d.getDeviceID() +  "," +  d.getLatitude() + "," + d.getLongitude());
		}
	}
	
	public void printLatestData() {
		TTNDevice d;
		for(String id: devices.keySet()){
			d = devices.get(id);
			System.out.println(d);
			System.out.println(d.getLatestData().getRaw());
		}
	}
	
	//Get LatLng of the first device in the arraylist (Just to centre the map)
	public LatLng getInitLatLng(){
		return this.getLatLng(devices.get("lopy1"));
	}
	
	//Get the LatLng object for a device
	public LatLng getLatLng(TTNDevice device){
		String lat = device.getLatitude();
		String lng = device.getLongitude();
		
		return new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
	}
	
	//Update marker upon new data receiving
	public void updateMarker(TTNDevice device){
		//Set icon of the marker
		//this.setIcon(device);
		//Update marker on map
		frame.getMapPanel().updateMarker(device);
	}
	
	//Create the initial markers on the map
	public void initMarkers(){
		TTNDevice d;
		for(String id: devices.keySet()){
			d = devices.get(id);
			frame.getMapPanel().createMarker(this.getLatLng(d), d);
		}
	}
	
	public HashMap<String,TTNDevice> getAllDevices(){
		return this.devices;
	}
	
	public MainFrame getMainFrame(){
		return this.frame;
	}
	
	public void setStatus(boolean status){
		this.status = status;
		if(frame != null){
			frame.getStatusPanel().setStatus(status);
			frame.repaint();
		}
	}
	
	public boolean getStatus(){
		return this.status;
	}
	
	public String[] getVariables(){
		return this.variables;
	}
	
	public void setCurrentVariable(String variable){
		this.currentVariable = variable;
		System.err.println("Switching to: " + currentVariable);
		
		TTNDevice d;
		for(String id: devices.keySet()){
			d = devices.get(id);
			if(d.getLatestData()!=null){
				d.getLatestData().generateFormatted();
				updateMarker(d);
			}
		}
	}
	
	public String getCurrentVariable(){
		return this.currentVariable;
	}
	
	public void updateLastData(String text){
		frame.getStatusPanel().setLastDataLabel(text);
	}
	
	public void refreshDataPanel(){
		frame.getDataPanel().refresh();
	}
	
/*	public void setIcon(TTNDevice device){
		Icon icon = new Icon();
        icon.loadFromFile(new File("res//assets//circle.png"));
        icon.setScaledSize(new Size(25,25));
		device.getMarker().setIcon(icon);
	}*/
	
}
