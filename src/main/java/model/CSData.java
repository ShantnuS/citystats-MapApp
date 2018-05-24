package model;

import controller.Controller;
import model.UnitManager;

//CS stands for city stats. This data object is only necessary for this application 

public class CSData {

	TTNDevice device;
	String raw;
	String formatted;
	String date;
	
	String temperature;
	String humidity;
	String light;
	String pressure;
	String altitude;
	String tilt;
	String voltage;

	
	public CSData(TTNDevice device, String raw){
		this.device = device;
		this.raw = raw;
		this.date = "none";
		this.temperature = "none";
		this.humidity = "none";
		this.light = "none";
		this.pressure = "none";
		this.altitude = "none";
		this.tilt = "none";
		this.voltage = "none";
	}
	
	public CSData(TTNDevice device, String date, 
			String temperature, String humidity,
			String light, String pressure, String altitude,
			String tilt, String voltage){
		this.device=device;
		this.date=date;
		this.temperature=temperature;
		this.humidity=humidity;
		this.light=light;
		this.pressure=pressure;
		this.altitude=altitude;
		this.tilt=tilt;
		this.voltage=voltage;
	}
	
	public TTNDevice getDevice() {
		return device;
	}
	public String getRaw(){
		System.out.println("inside getraw" + raw);
		return this.raw;
	}
	public String getFormatted(){
		return this.formatted;
	}
	public void setFormatted(String formatted){
		this.formatted = formatted;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getLight() {
		return light;
	}
	public void setLight(String light) {
		this.light = light;
	}
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getTilt() {
		return tilt;
	}

	public void setTilt(String tilt) {
		this.tilt = tilt;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	
	public void generateFormatted(){	
		String temp = "<h2><b>Device: " + this.device.getDeviceID() + "</b></h3>"
								 + "<br><b>Time: </b>" + this.date ;
		
		for(String e: Controller.getInstance().getVariables()){
			if(this.getVariable(e).equals("none")){
				if(device.getLatestData()!=null){
					this.createFromOld(device, e);
				}
			}
			
			temp+=this.createFormatted(e);
		}
		this.formatted = temp;
	}
	
	public void createFromOld(TTNDevice device, String variable){
		switch(variable){
		case "Temperature":	this.setTemperature(device.latestData.getTemperature() + "*");
							break;
		case "Light": 		this.setLight(device.latestData.getLight()+ "*");
							break;
		case "Humidity": 	this.setHumidity(device.latestData.getHumidity()+ "*");
							break;
		case "Pressure": 	this.setPressure(device.latestData.getPressure()+ "*");
							break;
		case "Altitude": 	this.setAltitude(device.latestData.getAltitude()+ "*");
							break;
		case "Tilt": 		this.setTilt(device.latestData.getTilt()+ "*");
							break;
		case "Voltage": 	this.setVoltage(device.latestData.getVoltage()+ "*");
							break;
		}
	}
	
	public String createFormatted(String variable){
		String temp = "";
		
		switch(variable){
		case "Temperature": temp += "<br><b>Temperature: </b>" + this.temperature + UnitManager.TEMPERATURE;
							break;
		case "Light": 		temp += "<br><b>Light: </b>" + this.light + UnitManager.LIGHT;
							break;
		case "Humidity": 	temp += "<br><b>Humidity: </b>" + this.humidity + UnitManager.HUMIDITY;
							break;
		case "Pressure": 	temp += "<br><b>Pressure: </b>" + this.pressure + UnitManager.PRESSURE;
							break;
		//case "Altitude": 	temp += "<br><b>Altitude: </b>" + this.altitude + UnitManager.ALTITUDE;
							//break;
		case "Tilt": 		temp += "<br><b>Tilt: </b>" + this.tilt + UnitManager.TILT;
							break;
		case "Voltage": 	temp += "<br><b>Voltage: </b>" + this.voltage + UnitManager.VOLTAGE;
							break;
		}
		return temp;
	}	
	
	public String getVariable(String variable){
		String temp = "";
		switch(variable){
		case "Temperature": temp = this.temperature;
							break;
		case "Light": 		temp = this.light;
							break;
		case "Humidity": 	temp = this.humidity;
							break;
		case "Pressure": 	temp = this.pressure;
							break;
		//case "Altitude": 	temp = this.altitude;
							//break;
		case "Tilt": 		temp = this.tilt;
							break;
		case "Voltage": 	temp = this.voltage;
							break;
		}
		return temp;
	}
	
	public String[] getAsStringArray(){
		//{"Date and Time", "Temperature", "Light", "Humidity", "Pressure", "Altitude", "Tilt", "Voltage"};
		//String[] array = {getDate(), getTemperature(), getLight(), getHumidity(), getPressure(), getAltitude(), getTilt(), getVoltage()};
		String[] array = {getDate(), getTemperature(), getLight(), getHumidity(), getPressure(), getTilt(), getVoltage()};
		return array;
	}
}
