package ps.blob.blobps.android;

import ps.blob.blobps.R;
import ps.blob.blobps.Map.Area;
import ps.blob.blobps.Map.AreaGrid;
import ps.blob.blobps.Map.EventArea;
import ps.blob.blobps.Map.KnownArea;
import ps.blob.blobps.Map.Map;
import ps.blob.blobps.Map.UnknownArea;
import ps.blob.blobps.BlobPS;
import ps.blob.blobps.R.drawable;
import ps.blob.blobps.R.id;
import ps.blob.blobps.R.layout;
import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MapsActivity extends FragmentActivity {

	private String TAG = "MapsActivity";

	private static MapsActivity instance;
	private BlobPS blobPS;
	final Context context = this;

	private GoogleMap mMap; // Might be null if Google Play services APK is not
							// available.

	private FloatingActionButton mainFAB, locationFAB, blobFAB, itemFAB, combineFAB;
	private int expandFAB = 0; // 0 = collapsed, 1 = expanded
	private int locToggle = 0; // 0 = center on current location, 1 = center on
								// map

	private final LatLng UMD = new LatLng(38.986918, -76.942554);
	private LatLng myLocation = UMD;

	// A lot of this info is now in the Map class
	/*
	 * private final double NORTH = 39.001460, EAST = -76.956008, SOUTH = 38.980446, WEST =
	 * -76.931203; private final LatLng UMD_NE = new LatLng(NORTH, EAST), UMD_SW = new LatLng(SOUTH,
	 * WEST); private final LatLngBounds UMD_BOUNDS = new LatLngBounds(UMD_SW, UMD_NE);
	 * 
	 * 
	 * private final int numAreas = 8; // grid will be numAreas x numAreas private AreaGrid[][] grid
	 * = new AreaGrid[numAreas][numAreas];
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_maps);
		setUpMapIfNeeded();
		createMainMenu();
		instance = this;

		// Setting up BlobPS
		blobPS = new BlobPS();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	/**
	 * Sets up the Floating Action Button the Map Screen
	 */
	private void createMainMenu() {
		mainFABListener();
		locationFABListener();
	}

	/**
	 * Hides location button and shows menu item buttons like this: (Combine) (Items) (Blobs) (X)
	 */
	private void expandFABMenu() {
		ViewGroup vg = (ViewGroup) (locationFAB.getParent());
		vg.removeView(locationFAB);

		blobFABListener();
		itemFABListener();
		combineFABListener();
	}

	/**
	 * Hides menu item buttons and shows buttons like this: (Location) (Menu)
	 */
	private void contractFABMenu() {
		ViewGroup vg = (ViewGroup) (blobFAB.getParent());
		vg.removeView(blobFAB);
		vg.removeView(itemFAB);
		vg.removeView(combineFAB);

		locationFABListener();
	}

	private void mainFABListener() {
		// Creates Floating Action Button for Menu
		mainFAB = new FloatingActionButton.Builder(this)
				.withDrawable(getResources().getDrawable(R.drawable.ic_action_star))
				.withButtonColor(Color.RED).withGravity(Gravity.BOTTOM | Gravity.RIGHT)
				.withMargins(0, 0, 16, 16).create();

		// When clicked, menu will expand or collapse
		mainFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (expandFAB == 0) {
					expandFAB = 1;
					expandFABMenu();
					mainFAB.setFloatingActionButtonDrawable(getResources().getDrawable(
							R.drawable.ic_action_cancel));
				} else {
					expandFAB = 0;
					contractFABMenu();
					mainFAB.setFloatingActionButtonDrawable(getResources().getDrawable(
							R.drawable.ic_action_star));
				}
			}
		});
	} // end of mainFABListener

	private void locationFABListener() {
		// Creates Floating Action Button for Location
		if (locToggle == 0) {
			// Current Location
			locationFAB = new FloatingActionButton.Builder(this)
					.withDrawable(getResources().getDrawable(R.drawable.ic_action_locate))
					.withButtonColor(Color.parseColor("#00A0B0"))
					.withGravity(Gravity.BOTTOM | Gravity.RIGHT).withMargins(0, 0, 16, 86).create();

		} else {
			// Campus Location
			locationFAB = new FloatingActionButton.Builder(this)
					.withDrawable(getResources().getDrawable(R.drawable.ic_action_locate))
					.withButtonColor(Color.parseColor("#BD1550"))
					.withGravity(Gravity.BOTTOM | Gravity.RIGHT).withMargins(0, 0, 16, 86).create();

		}

		locationFAB.hideFloatingActionButton();
		locationFAB.showFloatingActionButton();

		// When clicked, should center on current location or campus
		locationFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (locToggle == 0) {
					locToggle = 1;
					locationFAB.setFloatingActionButtonColor(Color.parseColor("#BD1550"));
					centerMapOnMyLocation();
					Toast.makeText(getApplicationContext(), "Centering map on current location",
							Toast.LENGTH_SHORT).show();
				} else {
					locToggle = 0;
					locationFAB.setFloatingActionButtonColor(Color.parseColor("#00A0B0"));
					centerMapOnCampus();
					Toast.makeText(getApplicationContext(), "Centering map on campus",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	} // end of locationFABListener

	private void blobFABListener() {
		// Creates Floating Action Button for Blobs List
		blobFAB = new FloatingActionButton.Builder(this)
				.withDrawable(getResources().getDrawable(R.drawable.ic_launcher))
				.withButtonColor(Color.parseColor("#EDC951"))
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT).withMargins(0, 0, 16, 86).create();

		blobFAB.hideFloatingActionButton();
		blobFAB.showFloatingActionButton();

		// When clicked, shows Toast message saying "Unimplemented"
		blobFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MapsActivity.this, BattleActivity.class);
				startActivity(intent);
				Log.i(TAG, "Clicked on Blob FAB");

				// Toast.makeText(getApplicationContext(), "Unimplemented - Blobs Listing",
				// Toast.LENGTH_SHORT).show();
			}
		});
	} // end of blobFABListener

	private void itemFABListener() {
		// Creates Floating Action Button for Item List
		itemFAB = new FloatingActionButton.Builder(this)
				.withDrawable(getResources().getDrawable(R.drawable.ic_launcher))
				.withButtonColor(Color.parseColor("#CBE86B"))
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT).withMargins(0, 0, 16, 156).create();

		itemFAB.hideFloatingActionButton();
		itemFAB.showFloatingActionButton();

		// When clicked, shows Toast message saying "Unimplemented"
		itemFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Unimplemented - Item Listing",
						Toast.LENGTH_SHORT).show();
				Log.i(TAG, "Clicked on Item FAB");
			}

		});
	} // end of itemsFABListener

	private void combineFABListener() {
		// Creates Floating Action Button for Combine Activity
		combineFAB = new FloatingActionButton.Builder(this)
				.withDrawable(getResources().getDrawable(R.drawable.ic_launcher))
				.withButtonColor(Color.parseColor("#53777A"))
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT).withMargins(0, 0, 16, 226).create();

		combineFAB.hideFloatingActionButton();
		combineFAB.showFloatingActionButton();

		// When clicked, launches Combine Screen
		combineFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Implement Combine Screen Activity",
						Toast.LENGTH_SHORT).show();
				// TODO - launch combine activity
				Log.i(TAG, "Clicked on Combine FAB");
				Intent intent = new Intent(MapsActivity.this, CombineActivity.class);
				startActivity(intent);

			}

		});
	} // end of combineFABListener

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
		// Do a null check to confirm that we have not already instantiated the
		// map.
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

		// Getting current location
		mMap.setMyLocationEnabled(true);
		mMap.getMyLocation();

		handleClickOnMap();

	}

	/**
	 * Centers map on current location. If current location can not be resolved, it defaults to UMD
	 * location.
	 */
	private void centerMapOnMyLocation() {

		mMap.setMyLocationEnabled(true);

		Location location = mMap.getMyLocation();

		if (location != null) {
			myLocation = new LatLng(location.getLatitude(), location.getLongitude());
		}
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
	}

	/**
	 * Centers the view of the map on center of the campus
	 */
	private void centerMapOnCampus() {
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UMD, 14));
	}


	/**
	 * NOTE: currently removes the grid overlays (instead of popup dialog) for testing purposes
	 * 
	 * Calculates where on the map was clicked and brings up a popup box displaying information of
	 * the area. If an area outside of campus is clicked, a Toast message is displayed informing the
	 * user.
	 */
	private void handleClickOnMap() {
		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng position) {
				Area tappedArea = blobPS.getMap().getGrid().getAreaWithPoint(position);
				
				if (tappedArea != null) { // Area is inside grid //
					final Dialog dialog = new Dialog(context); 
					dialog.setContentView(R.layout.maps_info);
					TextView text = (TextView) dialog.findViewById(R.id.text);
					ImageView image = (ImageView) dialog.findViewById(R.id.image); 
					image.setImageResource(R.drawable.ic_launcher);
					if(tappedArea instanceof KnownArea) {
						dialog.setTitle(tappedArea.getName());
						text.setText("Highest rank of blob you can find here: " + ((KnownArea)tappedArea).getRank());
					}
					else if(tappedArea instanceof UnknownArea) {
						dialog.setTitle("???");
						text.setText("???");
					}
					else if(tappedArea instanceof EventArea) {
						dialog.setTitle(tappedArea.getName() + ": Event!");
						text.setText(((EventArea)tappedArea).getDescription());
					}
				
					Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
				
					dialogButton.setOnClickListener(new OnClickListener() {
						@Override public void onClick(View v) { 
							dialog.dismiss(); 
						} 
					});
					
					dialog.show();
				}
				
				if (tappedArea.getGroundOverlay() != null) {  
					tappedArea.removeGroundOverlay(); 
				} else {
					Toast.makeText(getApplicationContext(), "Tapped outside of campus grid", Toast.LENGTH_SHORT).show(); 
				}
			}
		});
	}

	public static final MapsActivity getInstance() {
		return instance;
	}

	public final GoogleMap getGoogleMap() {
		return mMap;
	}
}
