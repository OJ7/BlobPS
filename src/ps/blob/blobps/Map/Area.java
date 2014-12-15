package ps.blob.blobps.Map;

import java.util.TreeSet;

import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Representation of an Area.
 * 
 * @author Chijioke/nuplex
 *
 */
public class Area {
	protected static final int KNOWN = 0, UNKNOWN = 1, EVENT = 2, UNSET = 3;
	private static int areaIDCounter = 0;
	private static TreeSet<String> usedNames = new TreeSet<String>();
	private String name;
	/** The default "x-y" name of the area */
	private String defaultName;
	private int id;
	protected int x;
	protected int y;
	protected LatLng areaSW, areaNE;
	protected LatLngBounds areaBounds;
	private GroundOverlay overlay;
	private int type = UNSET;
	protected String rank = "UNSET";
	protected String[] ranks = { "A", "B", "C", "D", "E" };

	/**
	 * Area constructor.
	 * 
	 * @param areaSW
	 * @param areaNE
	 * @param x
	 *            - x coordinate in relation to the grid
	 * @param y
	 *            - y coordinate in relation to the grid
	 * @param name
	 */
	public Area(LatLng areaSW, LatLng areaNE, int x, int y, String name) {
		this.areaSW = areaSW;
		this.areaNE = areaNE;
		this.areaBounds = new LatLngBounds(this.areaSW, this.areaNE);
		this.name = name;
		defaultName = name;
		usedNames.add(name);
		this.id = areaIDCounter;
		areaIDCounter++;
		this.x = x;
		this.y = y;
	}

	/**
	 * Turns an area into an event area. <br/>
	 * <b>You must reassign the area to the result for the change to take effect.</b>
	 * 
	 * @param description
	 * @return an event area with the passed in description
	 */
	public EventArea toEventArea(String description) {
		return new EventArea(this, description);
	}

	/**
	 * Turns an area into a known area. <br/>
	 * <b>You must reassign the area to the result for the change to take effect.</b>
	 * 
	 * @return the area
	 */
	public KnownArea toKnownArea() {
		return new KnownArea(this);
	}

	/**
	 * Turns an area into a known area. <br/>
	 * <b>You must reassign the area to the result for the change to take effect.</b>
	 * 
	 * @param rank
	 *            - the rank of the area. Valid values are A-E.
	 * @return the area
	 */
	public KnownArea toKnownArea(String rank) {
		return new KnownArea(this, rank);
	}

	/**
	 * Turns an area into an unknown area. <br/>
	 * <b>You must reassign the area to the result for the change to take effect.</b>
	 * 
	 * @return the area
	 */
	public UnknownArea toUnknownArea() {
		return new UnknownArea(this);
	}

	/**
	 * Gives the area a new name. The name must be unique.
	 * 
	 * @param newName
	 * @return true if rename was successful, false if it wasn't
	 */
	public boolean rename(String newName) {
		if (usedNames.contains(newName)) {
			return false;
		}
		usedNames.remove(name);
		usedNames.add(newName);
		name = newName;
		return true;
	}

	/**
	 * Returns the name of the area that it was initialized with.
	 * 
	 * @return the default name of the area.
	 */
	public String getDefaultName() {
		return defaultName;
	}

	public static int getAreaIDCounter() {
		return areaIDCounter;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public LatLng getAreaSW() {
		return areaSW;
	}

	public LatLng getAreaNE() {
		return areaNE;
	}

	public LatLngBounds getAreaBounds() {
		return areaBounds;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean containsPoint(LatLng point) {
		return areaBounds.contains(point);
	}

	public int getType() {
		return type;
	}

	protected void setType(int type) {
		this.type = type;
	}

	public String getRank() {
		return rank;
	}

	public GroundOverlay getGroundOverlay() {
		return overlay;
	}

	public void setGroundOverlay(GroundOverlay overlay) {
		this.overlay = overlay;
	}

	public void removeGroundOverlay() {
		overlay.remove();
		overlay = null;
	}

}
