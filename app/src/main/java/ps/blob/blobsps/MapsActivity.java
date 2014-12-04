package ps.blob.blobsps;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private FloatingActionButton fabButton, blobFAB, itemsFAB, combineFAB, settingsFAB, locationFAB;
    private int expandFAB = 0; // 0 = collapsed, 1 = expanded
    private int locToggle = 0; // 0 = will center on current location, 1 = will center on map

    private final LatLng UMD = new LatLng(38.989822, -76.940637);
    private final double NORTH = 39.001460, EAST = -76.956008, SOUTH = 38.980446, WEST = -76.931203;
    private final LatLng UMD_NE = new LatLng(NORTH, EAST),
            UMD_SW = new LatLng(SOUTH, WEST);
    private final LatLngBounds UMD_BOUNDS = new LatLngBounds(UMD_SW, UMD_NE);

    private final int numAreas = 4; // grid will be numAreas x numAreas
    private AreaGrid[][] grid = new AreaGrid[numAreas][numAreas];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        setUpGrid();
        setupFAB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the Floating Action Button the Map Screen
     */
    private void setupFAB() {
        fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_star))
                .withButtonColor(Color.RED)
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16).create();
        showLocationFAB();

        fabButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO (minor) - implement material design animations
                if (expandFAB == 0) {
                    expandFAB = 1;
                    showFABMenu();
                    fabButton.setFloatingActionButtonDrawable(getResources().getDrawable(
                            R.drawable.ic_action_cancel));
                } else {
                    expandFAB = 0;
                    hideFABMenu();
                    fabButton.setFloatingActionButtonDrawable(getResources().getDrawable(
                            R.drawable.ic_action_star));
                }
            }
        });

    }

    private void hideFABMenu() {
        blobFAB.hideFloatingActionButton();
        itemsFAB.hideFloatingActionButton();
        combineFAB.hideFloatingActionButton();
        settingsFAB.hideFloatingActionButton();
        showLocationFAB();
    }

    private void showFABMenu() {
        locationFAB.hideFloatingActionButton();
        showBlobFAB();
        showItemFAB();
        showCombineFAB();
        showSettingsFAB();
    }

    private void showLocationFAB() {
        locationFAB = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_locate))
                .withButtonColor(Color.CYAN).withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 86).create();

        locationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locToggle == 0) {
                    locToggle = 1;
                    locationFAB.setFloatingActionButtonColor(Color.MAGENTA);
                    centerMapOnMyLocation();
                    Toast.makeText(getApplicationContext(),
                            "Attempting to center map on current location", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    locToggle = 0;
                    locationFAB.setFloatingActionButtonColor(Color.CYAN);
                    centerMapOnCampus();
                    Toast.makeText(getApplicationContext(), "Centering map on campus",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    } // end of showLocationFAB

    private void showBlobFAB() {
        //Normal
        blobFAB = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_launcher))
                .withButtonColor(Color.BLUE).withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 86).create();

        blobFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Implement Blobs Activity",
                        Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MapsActivity.this, BlobsActivity.class);
                //startActivity(intent);

            }

        });


    } // end of showBlobFAB

    private void showItemFAB() {
        itemsFAB = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_launcher))
                .withButtonColor(Color.GREEN).withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 156).create();
        itemsFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Implement Items List Activity",
                        Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MapsActivity.this, ItemsActivity.class);
                //startActivity(intent);

            }

        });
    } // end of showItemFAB

    private void showCombineFAB() {
        itemsFAB = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_launcher))
                .withButtonColor(Color.GREEN).withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 226).create();
        itemsFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Implement Combine Screen Activity",
                        Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MapsActivity.this, CombineActivity.class);
                //startActivity(intent);

            }

        });
    } // end of showCombineFAB

    private void showSettingsFAB() {
        itemsFAB = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_launcher))
                .withButtonColor(Color.GREEN).withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 296).create();
        itemsFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Implement Settings Activity",
                        Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
                //startActivity(intent);

            }

        });
    } // end of showItemFAB

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near UMD.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Centering Map on UMD
        centerMapOnCampus();
        // Disabling Zoom Buttons
        mMap.getUiSettings().setZoomControlsEnabled(false);

        mMap.setMyLocationEnabled(true);
        mMap.getMyLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng position) {
                // grid.getTile(position)
                AreaGrid tappedArea = null;
                for(AreaGrid[] areaArray : grid){
                    for(AreaGrid area : areaArray){
                        if(area.getAreaBounds().contains(position)){
                            tappedArea = area;
                            break;
                        }
                    }
                }
                if(tappedArea != null){
                    Toast.makeText(getApplicationContext(), "Tapped on area with ID: " + tappedArea.getAreaID(),
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Tapped outside of campus grid",
                            Toast.LENGTH_SHORT).show();
                }
                // for each LatLngBounds [a cell in the grid]
                // if(LatLngBounds.contains(position)) { tile = LatLngBounds }
                // showInfoScreen(tile)

            }
        });

    }

    /**
     * Centers map on current location. If current location can not be resolved, it defaults to UMD
     * location.
     */
    private void centerMapOnMyLocation() {

        Location location = mMap.getMyLocation();
        LatLng myLocation = UMD;

        if (location != null) {
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));
    }

    private void centerMapOnCampus() {
        //mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(UMD_BOUNDS, 14));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UMD, 14));

    }

    private void setUpGrid() {
        double areaLength = Math.abs(EAST - WEST) / numAreas,
                areaHeight = Math.abs(NORTH - SOUTH) / numAreas;
        int areaID = 0;
        for (int row = 0; row < numAreas; row++) {
            for (int col = 0; col < numAreas; col++) {
                LatLng SW = new LatLng(NORTH - (row+1)*areaHeight, WEST - col*areaLength);
                LatLng NE = new LatLng(NORTH - row*areaHeight, WEST - (col+1)*areaLength);
                Log.i("MapsActivity", "areaID = " + areaID);
                Log.i("MapsActivity", "SW = " + SW.toString());
                Log.i("MapsActivity", "NE = " + NE.toString());

                AreaGrid area = new AreaGrid(SW, NE, areaID++);
                // TODO - Add overlay on area
                GroundOverlayOptions areaOverlay = new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.grey_overlay))
                        .positionFromBounds(area.getAreaBounds());
                mMap.addGroundOverlay(areaOverlay);
                grid[row][col] = area;
            }
        }
    }

}
