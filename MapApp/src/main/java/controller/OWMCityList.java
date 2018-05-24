package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OWMCityList {

	public static String[] getCities() throws IOException{
		return Files.readAllLines(Paths.get("res//cities.txt")).get(0).split(" ");
	}
}
