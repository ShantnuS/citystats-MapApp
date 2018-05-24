package model;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.websocket.Base64;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.Metadata;
import org.thethingsnetwork.data.common.messages.ActivationMessage;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.mqtt.Client;

import controller.Controller;
import controller.DataParser;

public class TTNClient {

	Client client; 
	Controller controller;

	public TTNClient(String region, String appId, String accessKey) {
		
		//Changing keep alive time of the client 
		MqttConnectOptions mcopts = new  MqttConnectOptions();
		mcopts.setKeepAliveInterval(Integer.MAX_VALUE);
		
		controller = Controller.getInstance();
		
		try {
			client = new Client(region, appId, accessKey, mcopts);
		} catch (URISyntaxException e) {
			System.err.println("Could not create client!");
			e.printStackTrace();
		}
		
        client.onError((Throwable _error) -> TTNClient.passError(_error));
        client.onActivation((String _devId, ActivationMessage _data) -> TTNClient.passActivation(_devId, _data));
        client.onConnected((Connection _client) -> TTNClient.passConnection(_client)); 
        client.onMessage((String devId, DataMessage data) -> TTNClient.passMessage(devId, data));
        
        try {
			client.start();
		} catch (MqttException e) {
			System.err.println("Could not start client! - mqtt error");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Could not start client! - unknown error");
			e.printStackTrace();
		}
	}	
	
	public static void passMessage(String devId, DataMessage data) {
		UplinkMessage message = (UplinkMessage) data;
		byte[] bytes = message.getPayloadRaw();
		String payload = "none";
    	String devID = message.getDevId();
    	Metadata metaData = message.getMetadata();
    	
    	//Decode payload
		try {
			payload = new String(bytes, "UTF-8");
			System.out.println("UTF8 decode: " + payload);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Could not decode payload!");
			e.printStackTrace();
		}
		
		TTNDevice device = Controller.getInstance().getDevice(devID);
		
		CSData csData = DataParser.parseData(device, payload, metaData.getTime());
		device.setLatestData(csData);
		
		Controller.getInstance().updateMarker(device); //update this
		Controller.getInstance().updateLastData("Last Data: " + devID + " " + csData.getDate());
		Controller.getInstance().refreshDataPanel();
	}
	
	public static void passConnection(Connection connection) {
		System.out.println("Connected!");
		Controller.getInstance().setStatus(true);
	}
	
	public static void passError(Throwable error) {
		System.out.println("Client Error!");
		System.err.println(error);
		System.err.println(error.getMessage());
		System.out.println("An error occured!");
		//Controller.getInstance().setStatus(true);
	}
	
	public static void passActivation(String devId, ActivationMessage data) {
		System.out.println("Activated: " + devId + " with data: " + data);
	}
	
	//Legacy code
	public static void passMessageLEGACY(String devId, DataMessage data) {
		UplinkMessage message = (UplinkMessage) data;
		System.out.println("Raw message: " + message.getPayloadRaw());
		byte[] bytes = message.getPayloadRaw();
		String base64String = Base64.encodeBytes(bytes);
		System.out.println("Base64 decode: " + base64String);
		
		try {
			String s2 = new String(bytes, "UTF-8");
			System.out.println("UTF8 decode: " + s2);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(message.getPayloadFields());
		
    	String appID = message.getAppId();
    	String devID = message.getDevId();
    	String payload = Arrays.toString(message.getPayloadRaw());
    	Metadata metaData = message.getMetadata();
    	
    	TTNData myData = new TTNData(appID, devID, payload, metaData);
		myData.setDevice(Controller.getInstance().getDevice(devID));
		TTNDevice device = myData.getDevice();
		//device.setLatestTTNData(myData);
		Controller.getInstance().updateMarker(device);
		System.out.println("Arrays to string: " + payload);
		Controller.getInstance().updateLastData("Last Data: " + devID + " - " + metaData.getTime());
	}
}
