package ps.blob.blobps.Map;

import java.util.TreeMap;

import ps.blob.blobps.BlobPS;
import ps.blob.blobps.R;
import ps.blob.blobps.android.MapsActivity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Representation of the grid the map contains.
 * 
 * @author Chijioke/nuplex
 * 
 */
public class AreaGrid {

	private LatLng gridSW, gridNE;
	private LatLngBounds gridBounds;
	private int areasAcross;
	private int areasDown;
	/** Originating from southwest, at 0,0 */
	private Area[][] grid;
	private TreeMap<String, Area> importantAreas;
	private TreeMap<String, EventArea> eventAreas;

	/**
	 * Constructs the grid for the map. All areas within the grid are by default of type
	 * UnknownArea.
	 * 
	 * @param maxSW
	 *            - lower SW point of grid
	 * @param maxNE
	 *            - upper NE point of grid
	 * @param areasAcross
	 *            - number of areas from side to side of the grid
	 * @param areasDown
	 *            - number of areas from top to bottom of grid.
	 */
	public AreaGrid(LatLng gridSW, LatLng gridNE, int areasAcross, int areasDown) {
		this.gridSW = gridSW;
		this.gridNE = gridNE;
		this.gridBounds = new LatLngBounds(this.gridSW, this.gridNE);
		this.areasAcross = areasAcross;
		this.areasDown = areasDown;
		grid = new Area[areasAcross][areasDown];
		importantAreas = new TreeMap<String, Area>();
		eventAreas = new TreeMap<String, EventArea>();

		double latSW = gridSW.latitude;
		double longSW = gridSW.longitude;
		double latNE = gridNE.latitude;
		double longNE = gridNE.longitude;
		double latDiff = latNE - latSW;
		double latInc = latDiff / (double) areasDown; // "height" of each area
		double longDiff = longSW - longNE;
		double longInc = longDiff / (double) areasAcross; // "length" of each area

		for (int y = 0; y < areasDown; y++) {
			for (int x = 0; x < areasAcross; x++) {
				double areaLatSW = latNE - (latInc * ((double) y + 1));
				double areaLongSW = longNE + (longInc * (double) x);
				double areaLatNE = areaLatSW + latInc;
				double areaLongNE = areaLongSW + longInc;
				LatLng areaSW = new LatLng(areaLatSW, areaLongSW);
				LatLng areaNE = new LatLng(areaLatNE, areaLongNE);

				Area area = new UnknownArea(areaSW, areaNE, x, y, x + "-" + y);
				grid[x][y] = area;

				// Setting ground overlay
				GroundOverlayOptions areaOverlayOptions = new GroundOverlayOptions()
						.image(BitmapDescriptorFactory.fromResource(R.drawable.grey_overlay))
						.transparency((float) 0.75).positionFromBounds(area.getAreaBounds());

				GroundOverlay areaOverlay = MapsActivity.getInstance().getGoogleMap()
						.addGroundOverlay(areaOverlayOptions);
				area.setGroundOverlay(areaOverlay);

			}
		}
	}

	/**
	 * Adds an event area to the list of event areas. This provides quicker retrieval time for these
	 * areas.
	 * 
	 * @param area
	 */
	public void addAreaToEventAreas(EventArea area) {
		eventAreas.put(area.getName(), area);
	}

	/**
	 * Removes an area from the list of important areas.
	 * 
	 * @param area
	 */
	public void removeAreaFromEventAreas(EventArea area) {
		eventAreas.remove(area.getName());
	}

	/**
	 * This sets this area of the grid to the passed in area. This is mostly to change the type of
	 * the area.
	 * 
	 * @param area
	 * @return the area that was passed in.
	 */
	public Area setArea(int x, int y, Area area) {
		if (area != null) {
			grid[x][y] = area;
			return grid[x][y];
		} else {
			throw new NullPointerException("Trying to set part of grid to null");
		}
	}

	/**
	 * Adds an area to the list of important areas. This provides quicker retrieval time for these
	 * areas.
	 * 
	 * @param area
	 */
	public void addAreaToImportantAreas(Area area) {
		importantAreas.put(area.getName(), area);
	}

	/**
	 * Removes an area from the list of important areas.
	 * 
	 * @param area
	 */
	public void removeAreaFromImportantAreas(Area area) {
		importantAreas.remove(area.getName());
	}

	/**
	 * @param point
	 * @return true if the grid contains this point
	 */
	public boolean gridContainsPoint(LatLng point) {
		return gridBounds.contains(point);
	}

	/**
	 * Gets the area that the point is in. Will return null if no such area exists.
	 * 
	 * @param point
	 * @return area, null if no area with the point exists.
	 */
	public Area getAreaWithPoint(LatLng point) {
		for (int y = 0; y < areasDown; y++) {
			for (int x = 0; x < areasAcross; x++) {
				Area area = grid[x][y];
				if (area.containsPoint(point)) {
					return area;
				}
			}
		}
		return null;
	}

	/**
	 * Gets an area based on it's name. Note that this is not efficient.<br/>
	 * <br/>
	 * If you want to get an important area quickly, consider adding it to "importantAreas" using
	 * addAreaToImportantAreas() for quicker retrieval.<br/>
	 * <br/>
	 * It is also faster to get an Area based on its x-y coordinate.
	 * 
	 * @param name
	 * @return area, null if the area with that name does not exist.
	 */
	public Area getArea(String name) {
		for (int y = 0; y < areasDown; y++) {
			for (int x = 0; x < areasAcross; x++) {
				Area area = grid[x][y];
				if (area.getName().equals(name)) {
					return area;
				}
			}
		}
		return null;
	}

	/**
	 * Gets an area based on it's default name (the one it was initialized with). Note that this is
	 * not efficient.<br/>
	 * <br/>
	 * If you want to get an important area quickly, consider adding it to "importantAreas" using
	 * addAreaToImportantAreas() for quicker retrieval.<br/>
	 * <br/>
	 * It is also faster to get an Area based on its x-y coordinate.
	 * 
	 * @param name
	 * @return area, null if the area with that default name does not exist.
	 */
	public Area getAreaByDefaultName(String name) {
		for (int y = 0; y < areasDown; y++) {
			for (int x = 0; x < areasAcross; x++) {
				Area area = grid[x][y];
				if (area.getDefaultName().equals(name)) {
					return area;
				}
			}
		}
		return null;
	}

	/**
	 * Gets an area based on it's id. Note that this is not efficient.<br/>
	 * <br/>
	 * If you want to get an important area quickly, consider adding it to "importantAreas" using
	 * addAreaToImportantAreas() for quicker retrieval.<br/>
	 * <br/>
	 * It is also faster to get an Area based on its x-y coordinate.
	 * 
	 * @param id
	 * @return area, null if the area with that id does not exist.
	 */
	public Area getArea(int id) {
		for (int y = 0; y < areasDown; y++) {
			for (int x = 0; x < areasAcross; x++) {
				Area area = grid[x][y];
				if (area.getId() == id) {
					return area;
				}
			}
		}
		return null;
	}

	/**
	 * Gets an area based on it's x-y coordinate. This is the most efficient way to get an Area.
	 * 
	 * @param x
	 * @param y
	 * @return area, null if the area does not exist or x and y are not within the grid
	 */
	public Area getArea(int x, int y) {
		if (x >= areasAcross || y >= areasAcross) {
			return null;
		} else {
			return grid[x][y];
		}
	}

	/**
	 * Gets an area based on it's id. Note that this is not efficient.<br/>
	 * <br/>
	 * If you want to get an important area quickly, consider adding it to "importantAreas" using
	 * addAreaToImportantAreas() for quicker retrieval.<br/>
	 * <br/>
	 * It is also faster to get an Area based on its x-y coordinate.
	 * 
	 * @param areaSW
	 * @param areaNW
	 * @return area, null if the area with those LatLngs does not exist.
	 */
	public Area getArea(LatLng areaSW, LatLng areaNE) {
		for (int y = 0; y < areasDown; y++) {
			for (int x = 0; x < areasAcross; x++) {
				Area area = grid[x][y];
				if (area.getAreaSW().equals(areaSW) && area.getAreaNE().equals(areaNE)) {
					return area;
				}
			}
		}
		return null;
	}

	/**
	 * Makes the entire grid known. This does not affect Event Areas.
	 */
	public void makeEntireGridKnown() {
		for (int y = 0; y < areasDown; y++) {
			for (int x = 0; x < areasAcross; x++) {
				grid[x][y] = grid[x][y].toKnownArea();
			}
		}
	}

	/**
	 * Makes the entire grid unknown. This does not affect Event Areas.
	 */
	public void makeEntireGridUnknown() {
		for (int y = 0; y < areasDown; y++) {
			for (int x = 0; x < areasAcross; x++) {
				grid[x][y] = grid[x][y].toUnknownArea();
			}
		}
	}

	public boolean updateAreaAtCurrentLocation() {

		LatLng currLocation = BlobPS.getInstance().getPlayer().getLatLng();
		Area currArea = getAreaWithPoint(currLocation);
		if (currArea != null && currArea instanceof UnknownArea) {
			setArea(currArea.x, currArea.y, currArea.toKnownArea());
			return true;
		}

		return false;
	}

	public LatLngBounds getGridBounds() {
		return gridBounds;
	}

}
