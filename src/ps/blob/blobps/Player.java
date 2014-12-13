package ps.blob.blobps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ps.blob.blobps.Blob.EnemyBlob;
import ps.blob.blobps.Blob.PersonalBlob;
import android.location.Location;

/**
 *  Representation of the player.
 * @author Chijioke/chokon
 *
 */
public class Player {

	/**Player Name, optional*/
	private String name; 
	private int id;
	/**[Blob's personal name, blob]*/
	private HashMap<String, PersonalBlob> blobs;
	private ArrayList<Item> items;
	private Location location = null;

	/**
	 * Player constructor. This does NOT set the player's
	 * location. Call setLocation() when it is ensured
	 * that the MapActivity is running and that Map, BlobPS, and Game Classes 
	 * have been created.
	 */
	public Player(){
		blobs = new HashMap<String, PersonalBlob>();
		items = new ArrayList<Item>();
	}

	/**
	 * Optional Player constructor that gives the player
	 * a name. If no name is given, id is returned in place where needed.
	 * <br/><br/>
	 * This does NOT set the player's
	 * location. Call setLocation() when it is ensured
	 * that the MapActivity is running and that Map, BlobPS, and Game Classes 
	 * have been created.
	 * @param name
	 */
	public Player(String name){
		this.name = name;
		blobs = new HashMap<String, PersonalBlob>();
		items = new ArrayList<Item>();
	}

	/**
	 * Copy constructor
	 */
	public Player(Player player){
		this.name = player.name;
		this.id = player.id;
		this.blobs = new HashMap<String, PersonalBlob>(player.blobs);
		this.items = new ArrayList<Item>(player.items);
		this.location = new Location(location);
	}

	/**
	 * Adds the blob to player's roster.
	 * @param blob
	 */
	public void addBlob(PersonalBlob blob){
		blobs.put(blob.getPersonalName(), blob);
	}

	/**
	 * Adds a an item to the player's items.
	 * @param item
	 */
	public void addItem(Item item){
		items.add(item.getThisItemsInstance());
	}

	public void useHealItem(Item item, PersonalBlob blob){
		if(item.getType() != Item.HEAL){
			throw new IllegalArgumentException("using non-heal item "
					+ "in useHealItem() on "+blob.getPersonalName());
		} else {
			for(Item i:items){
				if(i.equals(item)){
					blob.useHealItem(item);
					items.remove(item);
					break;
				}
			}
		}
	}

	/**
	 * Uses a capture item. If successful, adds the blob to the player's
	 * roster. The player will need to manually set the name, otherwise
	 * the personal name will just be the players blob's name + player's name
	 * +_+random int from 1-100000.
	 * of the blob.
	 * @param item
	 * @param blob
	 * @return true if blob was captured and now in roster, false capture failed
	 */
	public boolean useCaptureItem(Item item, EnemyBlob blob){
		if(item.getType() != Item.HEAL){
			throw new IllegalArgumentException("using non-capture item "
					+ "in useCaptureItem() on "+blob.getName());
		} else {
			for(Item i:items){
				if(i.equals(item)){
					items.remove(item);
					if(blob.attemptCapture(item) == true){
						Random r = new Random();
						addBlob(blob.capture(blob.getName()+getName()
								+"_"+r.nextInt(100000)+1, this));
						return true;
					} else {
						return false;
					}
				}
			}			
		}
		return false;
	}

	/**
	 * Removes the blob from the player's roster.
	 * @param personalName
	 */
	public void removeBlob(String personalName){
		blobs.remove(personalName);
	}

	/**
	 * Clears all player data.
	 */
	public void clear(){
		blobs.clear();
		items.clear();
	}

	/**
	 * Refreshes the player's location. If a new location is unattainable,
	 * the old location will be kept.
	 */
	public void refreshLocation(){
		Location old = location;
		Location newL = BlobPS.getInstance().getMap().getCurrentLocation();
		location =  newL == null ? old:newL;
	}

	/**
	 * @param name
	 * @return true if player has blob with name
	 */
	public boolean hasBlob(String name){
		return blobs.containsKey(name);
	}
	
	/**
	 * @param item
	 * @return true if player has item
	 */
	public boolean hasItem(Item item){
		return items.contains(item);
	}
	
	/**
	 * Returns the player's name, or id if the name
	 * was never set.
	 * @return name, if name was not set, the player id.
	 */
	public String getName(){
		return name.equals("") ? Integer.toString(id):name;
	}

	/**
	 * @return player location, null if there is no location.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the players location. 
	 * <br/> It is important that this is called after the Player
	 * object is constructed, as location is initialized as null. Call this only
	 * when it is ensured that the MapActivity is running and that Map, BlobPS,
	 * and Game classes have been created.
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public HashMap<String, PersonalBlob> getBlobs() {
		return blobs;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public String toString(){
		return getName();
	}

}
