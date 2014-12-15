package ps.blob.blobps.Map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Represents an area the player has not been in.
 * Rank of an unknown area is ???.
 * @author Chijioke/nuplex
 *
 */
public class UnknownArea extends Area {

	public UnknownArea(LatLng areaSW, LatLng areaNE, int x, int y, String name) {
		super(areaSW, areaNE, x, y, name);
		setType(UNKNOWN);
		rank = "???";
	}

	public UnknownArea(Area area){
		super(area.areaSW, area.areaNE, area.x, area.y, area.getName());
		setType(UNKNOWN);
		rank = "???";
	}
	
}
