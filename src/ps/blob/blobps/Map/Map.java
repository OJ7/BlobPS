package ps.blob.blobps.Map;

import ps.blob.blobps.android.MapsActivity;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

public class Map {
	AreaGrid grid;
	
	public Map(){
		
	}
	
	private final GoogleMap getGoogleMap(){
		return MapsActivity.getInstance().getGoogleMap();
	}
	
	public Location getCurrentLocation(){
		return getGoogleMap().getMyLocation();
	}
	
}
