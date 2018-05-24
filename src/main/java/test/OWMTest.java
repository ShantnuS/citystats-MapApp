package test;

import java.io.IOException;

import controller.OWMKeyGetter;

import org.openweathermap.api.DataWeatherClient;
import org.openweathermap.api.UrlConnectionDataWeatherClient;
import org.openweathermap.api.model.currentweather.CurrentWeather;
import org.openweathermap.api.query.Language;
import org.openweathermap.api.query.QueryBuilderPicker;
import org.openweathermap.api.query.ResponseFormat;
import org.openweathermap.api.query.Type;
import org.openweathermap.api.query.UnitFormat;
import org.openweathermap.api.query.currentweather.CurrentWeatherOneLocationQuery;


public class OWMTest {

	public static void testOWM(String[] args) throws IOException{
		System.out.println("Testing Open weather map API");
		String key = OWMKeyGetter.getKey();
		System.out.println("Key is: " + key);

		DataWeatherClient client = new UrlConnectionDataWeatherClient(key);
		CurrentWeatherOneLocationQuery currentWeatherOneLocationQuery = QueryBuilderPicker.pick()
				.currentWeather()                   // get current weather
				.oneLocation()                      // for one location
				.byCityName("Southampton")              // for Kharkiv city
				.countryCode("GB")                  // in Ukraine
				.type(Type.ACCURATE)                // with Accurate search
				.language(Language.ENGLISH)         // in English language
				.responseFormat(ResponseFormat.JSON)// with JSON response format
				.unitFormat(UnitFormat.METRIC)      // in metric units
				.build();
		CurrentWeather currentWeather = client.getCurrentWeather(currentWeatherOneLocationQuery);
		
		System.out.println("Name of city: " + currentWeather.getCityName());
		System.out.println("City coordinate: " + currentWeather.getCoordinate());
		System.out.println("Temperature: " + currentWeather.getMainParameters().getTemperature());
		System.out.println("Humidity: " + currentWeather.getMainParameters().getHumidity());
		System.out.println("Pressure: " + currentWeather.getMainParameters().getPressure());
	}
	
}
