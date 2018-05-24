package controller;

import model.CSData;
import model.TTNDevice;

public class ETCHelper {

	
	public static String getTempFormatted(String deviceID){
		String temp = "<h2><b>Device: " + deviceID + "</b></h3>"
				 + "<br><b>Time: </b>" + "none" ;
		
		temp += "<br><b>Temperature: </b>" + "none";
		temp += "<br><b>Light: </b>" + "none";
		temp += "<br><b>Humidity: </b>" + "none";
		temp += "<br><b>Pressure: </b>" + "none";
		//temp += "<br><b>Altitude: </b>" + "none";
		temp += "<br><b>Tilt: </b>" + "none";
		temp += "<br><b>Voltage: </b>" + "none";
		
		return temp;
	}
	
	public static double getValueFromName(TTNDevice device, String variable){
		double value =0;
		switch(variable){
		case "Temperature": if(!device.getLatestData().getTemperature().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(device.getLatestData().getTemperature().replaceAll("\\*", "")); 
		break;
		case "Humidity": if(!device.getLatestData().getHumidity().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(device.getLatestData().getHumidity().replaceAll("\\*", "")); 
		break;
		case "Light": if(!device.getLatestData().getLight().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(device.getLatestData().getLight().replaceAll("\\*", "")); 
		break;
		case "Pressure": if(!device.getLatestData().getPressure().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(device.getLatestData().getPressure().replaceAll("\\*", "")); 
		break;
		case "Altitude": if(!device.getLatestData().getAltitude().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(device.getLatestData().getAltitude().replaceAll("\\*", "")); 
		break;
		case "Tilt": if(!device.getLatestData().getTilt().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(device.getLatestData().getTilt().replaceAll("\\*", "")); 
		break;
		case "Voltage": if(!device.getLatestData().getVoltage().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(device.getLatestData().getVoltage().replaceAll("\\*", "")); 
		break;
		}
		
		return value;
	}
	
	public static double getValueFromName(CSData data, String variable){
		double value =0;
		switch(variable){
		case "Temperature": if(!data.getTemperature().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(data.getTemperature().replaceAll("\\*", "")); 
		break;
		case "Humidity": if(!data.getHumidity().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(data.getHumidity().replaceAll("\\*", "")); 
		break;
		case "Light": if(!data.getLight().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(data.getLight().replaceAll("\\*", "")); 
		break;
		case "Pressure": if(!data.getPressure().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(data.getPressure().replaceAll("\\*", "")); 
		break;
		case "Altitude": if(!data.getAltitude().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(data.getAltitude().replaceAll("\\*", "")); 
		break;
		case "Tilt": if(!data.getTilt().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(data.getTilt().replaceAll("\\*", "")); 
		break;
		case "Voltage": if(!data.getVoltage().replaceAll("\\*", "").equals("none")) value=Double.parseDouble(data.getVoltage().replaceAll("\\*", "")); 
		break;
		}
		
		return value;
	}
	
	public static double getAverageValue(TTNDevice device, String variable){
		double value = 0;
		int count = 0;
		switch(variable){
		case "Temperature": for(CSData d: device.getAllData()){
			if(!(d.getTemperature().replaceAll("\\*", "").equals("none"))){
				value+=Double.parseDouble(d.getTemperature().replaceAll("\\*", ""));
				count++;
			}
		}
		break;
		case "Humidity": for(CSData d: device.getAllData()){
			if(!(d.getHumidity().replaceAll("\\*", "").equals("none"))){
				value+=Double.parseDouble(d.getHumidity().replaceAll("\\*", ""));
				count++;
			}
		}
		break;
		case "Light": for(CSData d: device.getAllData()){
			if(!(d.getLight().replaceAll("\\*", "").equals("none"))){
				value+=Double.parseDouble(d.getLight().replaceAll("\\*", ""));
				count++;
			}
		}
		break;
		case "Pressure": for(CSData d: device.getAllData()){
			if(!d.getPressure().replaceAll("\\*", "").equals("none")){
				value+=Double.parseDouble(d.getPressure().replaceAll("\\*", ""));
				count++;
			}
		}
		break;
		case "Altitude": for(CSData d: device.getAllData()){
			if(!d.getAltitude().replaceAll("\\*", "").equals("none")){
				value+=Double.parseDouble(d.getAltitude().replaceAll("\\*", ""));
				count++;
			}
		}
		break;
		case "Tilt": for(CSData d: device.getAllData()){
			if(!d.getTilt().replaceAll("\\*", "").equals("none")){
				value+=Double.parseDouble(d.getTilt().replaceAll("\\*", ""));
				count++;
			}
		}
		break;
		case "Voltage": for(CSData d: device.getAllData()){
			if(!d.getVoltage().replaceAll("\\*", "").equals("none")){
				value+=Double.parseDouble(d.getVoltage().replaceAll("\\*", ""));
				count++;
			}
		}
		break;
		}
		return value/count;
	}
}
