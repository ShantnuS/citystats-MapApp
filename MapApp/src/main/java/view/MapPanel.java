package view;

import java.io.File;
import java.io.IOException;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.Icon;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.MouseEvent;
import com.teamdev.jxmaps.Size;
import com.teamdev.jxmaps.swing.MapView;

import controller.Controller;
import controller.ETCHelper;
import model.OWMDevice;
import model.TTNDevice;

public class MapPanel extends MapView {

	private static final long serialVersionUID = 1L;
	
	Map map;
	
	public MapPanel() {
        setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                // Check if the map is loaded correctly
                if (status == MapStatus.MAP_STATUS_OK) {
                    map = getMap();
                    
                    //create controls
                    MapOptions options = new MapOptions();
                    MapTypeControlOptions controlOptions = new MapTypeControlOptions();
                    controlOptions.setPosition(ControlPosition.TOP_RIGHT);
                    options.setMapTypeControlOptions(controlOptions);
                    map.setOptions(options);
                    
                    
                    //ready the map
                    map.setCenter(Controller.getInstance().getInitLatLng());
                    map.setZoom(16.0);    
                    Controller.getInstance().initMarkers();
                    
                    try {
						Controller.getInstance().initOWM();
					} catch (IOException e) {
						System.err.println("Could not initOWM!");
						e.printStackTrace();
					}
                    
                    recenter();
                }
            }
        });
    }
	
	public void createMarker(LatLng latlng, TTNDevice device){
		Icon icon = new Icon();
		icon.loadFromFile(new File("res//assets//circle.png"));
        icon.setScaledSize(new Size(25,25));
        
        Marker marker = new Marker(map);
        marker.setPosition(latlng);
        marker.setIcon(icon);
        final InfoWindow infoWindow = new InfoWindow(map);
        infoWindow.setContent(ETCHelper.getTempFormatted(device.getDeviceID()));
        //infoWindow.open(map, marker);
        device.setMarker(marker);
        device.setInfoWindow(infoWindow);
        
        marker.addEventListener("click", new MapMouseEvent() {
            @Override
            public void onEvent(MouseEvent mouseEvent) {
            	if(device.getLatestData()!= null)
            		infoWindow.setContent(device.getLatestData().getFormatted());
            	infoWindow.open(map, marker);
            }
        });
	}
	
	public void createMarkerOWM(LatLng latlng, OWMDevice device){
		Icon icon = new Icon();
		icon.loadFromFile(new File("res//assets//square.png"));
        icon.setScaledSize(new Size(25,25));
		
		Marker marker = new Marker(map);
        marker.setPosition(latlng);
        marker.setIcon(icon);
        final InfoWindow infoWindow = new InfoWindow(map);
        
        device.setMarker(marker);
        device.setInfoWindow(infoWindow);
        
        marker.addEventListener("click", new MapMouseEvent() {
            @Override
            public void onEvent(MouseEvent mouseEvent) {
            		infoWindow.setContent(device.getFormatted());
            	infoWindow.open(map, marker);
            }
        });
	}
	
	public void updateMarker(TTNDevice device){
		if(device.getLatestData()!= null)
    		device.getInfoWindow().setContent(device.getLatestData().getFormatted());
		device.getInfoWindow().open(map, device.getMarker());
	}
	
	public void recenter(){
		map.setCenter(Controller.getInstance().getInitLatLng());
	}
}
