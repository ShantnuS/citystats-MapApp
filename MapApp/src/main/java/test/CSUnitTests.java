package test;

import controller.DataParser;
import junit.framework.TestCase;
import model.CSData;
import model.TTNDevice;

public class CSUnitTests extends TestCase {

	public void testDataParse() {
		TTNDevice testDevice = new TTNDevice("lopy1", "15.6", "13.5");
		String payload = "t17.5:v4.7";
		String date = "1970-01-01T00:00:00Z";
		CSData data = DataParser.parseData(testDevice, payload, date);
		String result = data.getTemperature();
        assertEquals("17.5", result);
    }
	
}
