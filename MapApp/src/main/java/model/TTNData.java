package model;

import org.thethingsnetwork.data.common.Metadata;

//LEGACY CODE WHEN DATA OBJECT WAS NOT READY
public class TTNData {

	String appID;
	String deviceID;
	TTNDevice device;
	String payload;
	Metadata metaData;
	
	public TTNData(String appID, String deviceID, String payload, Metadata metaData) {
		this.appID = appID;
		this.deviceID = deviceID;
		this.payload = payload;
		this.metaData = metaData;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public TTNDevice getDevice() {
		return device;
	}

	public void setDevice(TTNDevice device) {
		this.device = device;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Metadata getMetaData() {
		return metaData;
	}

	public void setMetaData(Metadata metaData) {
		this.metaData = metaData;
	}
	
	
}
