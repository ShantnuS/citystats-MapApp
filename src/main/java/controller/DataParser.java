package controller;

import model.CSData;
import model.TTNDevice;

public class DataParser {

	//Use this to turn payload into normalised data type that can be used.
	//Might even parse entire TTNData object to create something better
	public static CSData parseData(TTNDevice device, String payload, String date){
		System.out.println("Parsing!");
		CSData data = new CSData(device, payload);
		data.setDate(DataParser.parseTime(date));
		String[] parts = payload.split(":");
		String firstLetter;
		String value;
		for(String i : parts){
			firstLetter = i.substring(0, 1);
			value = i.substring(1, i.length());
			
			switch(firstLetter){
			case "t": 	data.setTemperature(value);
						break;
			case "h": 	data.setHumidity(value);
						break;
			case "l": 	data.setLight(value);
						break;
			case "p": 	data.setPressure(value);
						break;
			case "a": 	data.setAltitude(value);
						break;
			case "i": 	data.setTilt(value);
						break;
			case "v": 	data.setVoltage(value);
						break;
			}
		}
		data.generateFormatted();
		return data;
	}
	
	public static String parseTime(String date){
		String[] parts = date.split("T");
		String time = parts[1].substring(0, 8) + "@" + parts[0];
		
		return time;
	}
}
