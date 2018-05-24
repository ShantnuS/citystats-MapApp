package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SQLKeyGetter {

	public static String getUsername() throws IOException{
		return Files.readAllLines(Paths.get("res//sql.key")).get(0);
	}
	
	public static String getPassword() throws IOException {
		return Files.readAllLines(Paths.get("res//sql.key")).get(1);
	}
	
}
