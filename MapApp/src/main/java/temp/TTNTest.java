package temp;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.common.messages.ActivationMessage;
import org.thethingsnetwork.data.mqtt.Client;

public class TTNTest 
{ 
    public TTNTest() throws MqttException, Exception{
    	System.out.println("TTN TEST ACTIVATED");
    }
    
    public void init(String region, String appId, String accessKey) throws MqttException, Exception{
        System.out.println( "Attempting to connect..." );

        Client client = new Client(region, appId, accessKey);
                         
        client.onError((Throwable _error) -> System.err.println("error: " + _error.getMessage()));
        client.onActivation((String _devId, ActivationMessage _data) -> System.out.println("Activation: " + _devId + ", data: " + _data));
        client.onConnected((Connection _client) -> System.out.println("Connected!")); 
        client.onMessage((String devId, DataMessage data) -> TTNTest.doIt(data));
        
        //System.out.println("Message: " + devId + " " + (UplinkMessage) data));
       
        client.start();
    }
    
    public static void doIt(DataMessage data){
    	UplinkMessage msg = (UplinkMessage) data;
    	System.out.println(msg.getAppId());
    	System.out.println(msg.getDevId());
    	
    	byte[] payload = msg.getPayloadRaw();
    	System.out.println(Arrays.toString(payload));
    	
    	System.out.println(msg.getMetadata().getTime());
    	
    	System.out.println(data);
    	System.out.println(msg);
    }
}
