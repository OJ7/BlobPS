package ps.blob.blobps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import com.google.android.gms.maps.model.LatLng;

import ps.blob.blobps.Blob.Blob;
import ps.blob.blobps.Blob.EnemyBlob;
import ps.blob.blobps.Blob.PersonalBlob;
import ps.blob.blobps.Special.Special;
import android.location.Location;

/**
 *  Representation of the player.
 * @author Chijioke/nuplex
 *
 */
public class Player {

	/**Player Name, optional*/
	private String name; 
	private int id;
	/**[Blob's personal name, blob]*/
	private HashMap<String, PersonalBlob> blobs;
	/**Order of blobs (in preference) in player's list.
	 * The first one is the one used in battle.
	 */
	private TreeMap<Integer, String> blobOrder;
	private ArrayList<Item> items;
	private Location location = null;
	
	private static int blobCounter = 1;

	/**
	 * Player constructor. This does NOT set the player's
	 * location. Call setLocation() when it is ensured
	 * that the MapActivity is running and that Map, BlobPS, and Game Classes 
	 * have been created.
	 */
	public Player(){
		Special heavyHit = new Special("Heavy Hit", 
				"An impressive attack that deals damage 1.5x the blob's ATK.", 
				Special.HEAVY_HIT);
		PersonalBlob defaultBlob1 = new PersonalBlob("MyFirstBlob", this, "MyFirstBlob", 10, 3, 5, 5, 0.0, null, null, heavyHit);
		PersonalBlob defaultBlob2 = new PersonalBlob("MySecondBlob", this, "MySecondBlob", 20, 6, 10, 10, 0.0, null, null, heavyHit);
		blobs = new HashMap<String, PersonalBlob>();
		blobs.put("MyFirstBlob", defaultBlob1);
		blobs.put("MySecondBlob", defaultBlob2);
		blobOrder = new TreeMap<Integer, String>();
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
		Special heavyHit = new Special("Heavy Hit", 
				"An impressive attack that deals damage 1.5x the blob's ATK.", 
				Special.HEAVY_HIT);
		PersonalBlob defaultBlob1 = new PersonalBlob("MyFirstBlob", this, "MyFirstBlob", 10, 3, 5, 5, 0.0, null, null, heavyHit);
		PersonalBlob defaultBlob2 = new PersonalBlob("MySecondBlob", this, "MySecondBlob", 20, 6, 10, 10, 0.0, null, null, heavyHit);

		blobs = new HashMap<String, PersonalBlob>();
		blobs.put("MyFirstBlob", defaultBlob1);		
		blobs.put("MySecondBlob", defaultBlob2);

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
		if(blobs.containsKey(blob.getPersonalName())){
			throw new IllegalArgumentException("Cannot add blob: personal names"
					+ " must be unique");
		}
		blobs.put(blob.getPersonalName(), blob);
		blobOrder.put(blobCounter, blob.getPersonalName());
		blobCounter++;
	}

	/**
	 * Adds a an item to the player's items.
	 * @param item
	 */
	public void addItem(Item item){
		items.add(item.getThisItemsInstance());
	}

	/**
	 * Renames the blob to newName. Renaming a blob may change their order.
	 * @param oldName
	 * @param newName
	 * @return true if renamed, false if wasn't.
	 */
	public boolean renameBlob(String oldName, String newName){
		if(!blobs.containsKey(oldName)){
			return false;
		}
		PersonalBlob b = blobs.get(oldName);
		b.setPersonalName(newName);
		addBlob(b);
		removeBlob(oldName);
		return true;
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
		
		int blobNum = Integer.MAX_VALUE;
		for(int i : blobOrder.keySet()){
			//case blob to remove is the last one
			if(blobOrder.get(i+1) == null){
				/*the below do the same thing but the state of the list is
				 * not the same. In the first no blob has been removed yet, 
				 * where as in the second a blob has been removed and the
				 * last node is being removed for decrementation.
				 */
				if(blobOrder.get(i).equals(personalName)){
					blobOrder.remove(i);
					blobCounter--;
				} else if (blobNum != Integer.MAX_VALUE){
					/* means a blob was removed, so
					 * list size must be decremented
					 */
					blobOrder.remove(i);
					blobCounter--;
				}
				break;  //don't want to continue
			}
			if(personalName.equals(blobOrder.get(i))){
				blobNum = i;
				blobOrder.put(i, blobOrder.get(i+1));
			} 
		}
	}

	/**
	 * Clears all player data.
	 */
	public void clear(){
		blobs.clear();
		blobOrder.clear();
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
	 * Heals all blobs fully;
	 */
	public void healAllBlobs(){
		for(PersonalBlob b : blobs.values()){
			b.fullHeal();
		}
	}
	
	/**
	 * Get's the first blob. Useful for getting the blob
	 * you fight with by default in battle.
	 * @return first blob
	 */
	public PersonalBlob getFirstBlob(){
		return blobs.get(blobOrder.firstKey());
	}
	
	/**
	 * Get's the first blob that is alive. Null if none are alive.
	 * @return first alive blob, null if none are.
	 */
	public PersonalBlob getFirstAliveBlob(){
		for(int i : blobOrder.keySet()){
			PersonalBlob blob = blobs.get(blobOrder.get(i));
			if(!blob.isDead()){
				return blob;
			}
		}
		return null;
	}
	
	/**
	 * @return true if all blobs are dead, false otherwise.
	 */
	public boolean allDead(){
		return getFirstAliveBlob() == null;
	}
	
	/**
	 * Gets a blob by its personal name.
	 * @param personalName
	 * @return the blob
	 */
	public PersonalBlob getBlob(String personalName){
		return blobs.get(personalName);
	}
	
	/**
	 * Gets a blob by its personal name, if it is alive. If not it will
	 * return null.
	 * @param personalName
	 * @return The blob if it is alive, false otherwise.
	 */
	public PersonalBlob getAliveBlob(String personalName){
		PersonalBlob b = getBlob(personalName);
		return b.isDead() ? null:b;
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
	
	/**
	 * Get's the players current latitude and longitude
	 */
	public LatLng getLatLng(){
		return new LatLng(location.getLatitude(), location.getLongitude());
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
