package model;

import java.io.IOException;

import org.openweathermap.api.DataWeatherClient;
import org.openweathermap.api.UrlConnectionDataWeatherClient;
import org.openweathermap.api.model.currentweather.CurrentWeather;
import org.openweathermap.api.query.Language;
import org.openweathermap.api.query.QueryBuilderPicker;
import org.openweathermap.api.query.ResponseFormat;
import org.openweathermap.api.query.Type;
import org.openweathermap.api.query.UnitFormat;
import org.openweathermap.api.query.currentweather.CurrentWeatherOneLocationQuery;

import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.Marker;

import controller.OWMKeyGetter;

public class OWMDevice {

	String name;
	String temperature;
	String humidity;
	String pressure;
	String formatted;
	String latitude;
	String longitude;
	
	Marker marker;
	InfoWindow infoWindow;
	
	public OWMDevice(String name){
		this.name = name;
		
		try {
			this.calculateValues();
		} catch (IOException e) {
			System.err.println("Could not calculate values for OWM!");
			e.printStackTrace();
		}
	}
	
	public void calculateValues() throws IOException{
		String key = OWMKeyGetter.getKey();

		DataWeatherClient client = new UrlConnectionDataWeatherClient(key);
		CurrentWeatherOneLocationQuery currentWeatherOneLocationQuery = QueryBuilderPicker.pick()
				.currentWeather()               
				.oneLocation()                      
				.byCityName(name)             
				.countryCode("GB")                
				.type(Type.ACCURATE)                
				.language(Language.ENGLISH)         
				.responseFormat(ResponseFormat.JSON)
				.unitFormat(UnitFormat.METRIC)      
				.build();
		CurrentWeather currentWeather = client.getCurrentWeather(currentWeatherOneLocationQuery);
		
		this.temperature= Double.toString(currentWeather.getMainParameters().getTemperature());
		this.humidity = Double.toString(currentWeather.getMainParameters().getHumidity());
		this.pressure = Double.toString(currentWeather.getMainParameters().getPressure());
		this.latitude = currentWeather.getCoordinate().getLatitude();
		this.longitude = currentWeather.getCoordinate().getLongitude();
		
		this.createFormatted();
	}
	
	public void createFormatted(){
		this.formatted="";
		
		formatted += "<h2><b>OpenWeatherMap: " + this.name + "</b></h3>";
		formatted += "<br><b>Temperature: </b>" + this.temperature + UnitManager.TEMPERATURE;
		formatted += "<br><b>Humidity: </b>" + this.humidity + UnitManager.HUMIDITY;
		formatted += "<br><b>Pressure: </b>" + this.pressure + " hpa";	
	}
	
	public void setMarker(Marker marker){
		this.marker = marker;
	}
	
	public void setInfoWindow(InfoWindow window){
		this.infoWindow = window;
	}
	
	public String getFormatted(){
		return formatted;
	}
	
	public String getLatitude(){
		return this.latitude;
	}
	
	public String getLongitude(){
		return this.longitude;
	}
}
