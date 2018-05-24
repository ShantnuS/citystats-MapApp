package model;

import java.util.ArrayList;

import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.Marker;

public class TTNDevice {

	String deviceID;
	String longitude;
	String latitude;
	ArrayList<CSData> allData;
	CSData latestData;
	Marker marker;
	InfoWindow infoWindow;
	
	public TTNDevice(String deviceID, String latitude, String longitude) {
		this.deviceID = deviceID;
		this.longitude = longitude;
		this.latitude = latitude;
		allData = new ArrayList<CSData>();
	}
	
	public void setLatestData(CSData latestData) {
		this.latestData = latestData;
	}
	
	public void addData(CSData data) {
		allData.add(data);
	}
	
	public void eraseData(){
		allData = new ArrayList<CSData>();
	}
	
	public CSData getLatestData() {
		return this.latestData;
	}
	
	public ArrayList<CSData> getAllData(){
		return this.allData;
	}
	
	public Marker getMarker(){
		return this.marker;
	}
	
	public InfoWindow getInfoWindow(){
		return this.infoWindow;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setMarker(Marker marker){
		this.marker = marker; 
	}
	
	public void setInfoWindow(InfoWindow infoWindow){
		this.infoWindow = infoWindow;
	}
	
}
