package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.CSData;
import model.TTNDevice;

public class SQLManager {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://csvps.shantnu.me/citystats";

	public static void getDeviceData(TTNDevice device) throws IOException{
		System.out.println("Getting SQL data for: " + device.getDeviceID());
		String USER = SQLKeyGetter.getUsername();
		String PASS = SQLKeyGetter.getPassword();

		Connection conn = null;
		PreparedStatement stmt = null;
		String selectSQL = "SELECT * FROM csdata WHERE devID = ?";

		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.prepareStatement(selectSQL);
			stmt.setString(1, device.getDeviceID());

			// execute select SQL stetement
			ResultSet rs = stmt.executeQuery();
			
			String time;
			String temperature;
			String humidity;
			String light;
			String pressure;
			String altitude;
			String tilt;
			String voltage;
			
			device.eraseData();
			while(rs.next()){
				//Retrieve by column name
				time = rs.getString("time");
				temperature = rs.getString("temperature");
				humidity = rs.getString("humidity");
				light = rs.getString("light");
				pressure = rs.getString("pressure");
				altitude = rs.getString("altitude");
				tilt = rs.getString("tilt");
				voltage = rs.getString("voltage");
			
				device.addData(new CSData(device,time,temperature,
						humidity,light,pressure,
						altitude,tilt,voltage));
			}
			rs.close();
			
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					conn.close();
			}catch(SQLException se){
			}// do nothing
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
	}

	public static void getDeviceLatestData(TTNDevice device){
		
	}
}
