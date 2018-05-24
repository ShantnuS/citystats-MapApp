package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OWMKeyGetter {

	
	public static String getKey() throws IOException{
		return Files.readAllLines(Paths.get("res//openweathermap.key")).get(0);
	}
	
}
