package ps.blob.blobsps;

import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class AreaGrid {

    private LatLng areaSW, areaNE;
    private LatLngBounds areaBounds;
    private int areaID;
    private GroundOverlay overlay;

    public AreaGrid(LatLng areaSW, LatLng areaNE, int areaID){
        this.areaSW = areaSW;
        this.areaNE = areaNE;
        this.areaBounds = new LatLngBounds(this.areaSW, this.areaNE);
        this.areaID = areaID;

    }

    public LatLngBounds getAreaBounds(){
        return areaBounds;
    }

    public int getAreaID(){
        return areaID;
    }

    public GroundOverlay getGroundOverlay(){
        return overlay;
    }


    public void setGroundOverlay(GroundOverlay overlay){
        this.overlay = overlay;
    }

    public void removeGroundOverlay(){
        overlay.remove();
        overlay = null;
    }

    public boolean tileContainsPoint(LatLng point){
        return areaBounds.contains(point);
    }




}
