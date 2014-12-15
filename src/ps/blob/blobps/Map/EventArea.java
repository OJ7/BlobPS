package ps.blob.blobps.Map;

import java.util.HashMap;

import ps.blob.blobps.BlobPS;
import ps.blob.blobps.Item;
import ps.blob.blobps.Player;
import ps.blob.blobps.Blob.Blob;
import ps.blob.blobps.Blob.EnemyBlob;
import com.google.android.gms.maps.model.LatLng;

/**
 * Represents an area that has an event.
 * Event have ranks of type EVENT, which are equivalent to rank A.
 * @author Chijioke/nuplex
 *
 */
public class EventArea extends Area {
	private String description;
	/** [Blob, Quantity] */
	private HashMap<EnemyBlob, Integer> gainableBlobs;
	/** [Item, Quantity] */
	private HashMap<Item, Integer> gainableItems;
	private boolean playerHasBeenHere = false;
	
	public EventArea(LatLng areaSW, LatLng areaNE, int x, int y, String name
			, String description) {
		super(areaSW, areaNE, x, y, name);
		this.description = description;
		gainableBlobs = new HashMap<EnemyBlob, Integer>();
		gainableItems = new HashMap<Item, Integer>();
		setType(EVENT);
		rank = "EVENT";
	}
	
	public EventArea(Area area, String description){
		super(area.areaSW, area.areaNE, area.x, area.y, area.getName());
		this.description = description;
		gainableBlobs = new HashMap<EnemyBlob, Integer>();
		gainableItems = new HashMap<Item, Integer>();
		setType(EVENT);
		rank = "EVENT";
	}
	
	/**
	 * Rewards the player with blobs and/or items for visiting the event tile. 
	 * Any blobs won this way will have to be manually renamed. If the player
	 * has visited the tile before, they will receive no reward.
	 * @param player
	 */
	public void rewardPlayer(Player player){
		if(!playerHasBeenHere){
			for(EnemyBlob b : gainableBlobs.keySet()){
				//capture  == toPersonalBlob
				String personalName = BlobPS.getInstance().getGame().nameRandomizer();
				player.addBlob(b.capture(personalName, player));
			}
			for(Item i : gainableItems.keySet()){
				player.addItem(i);
			}
			playerHasBeenHere = true;
		}
	}
	
	/**
	 * Adds a blob to the reward list, with the specified quantity.
	 * @param blob
	 */
	public void addReward(EnemyBlob blob, int quantity){
		gainableBlobs.put(blob, quantity);
	}
	
	/**
	 * Removes a blob to the reward list.
	 * @param blob
	 */
	public void removeReward(EnemyBlob blob){
		gainableBlobs.remove(blob);
	}
	
	/**
	 * Adds an item to the reward list, with the specified quantity.
	 * @param item
	 */
	public void addReward(Item item, int quantity){
		gainableItems.put(item, quantity);
	}
	
	/**
	 * Removes an item from the reward list.
	 * @param item
	 */
	public void removeReward(Item item){
		gainableItems.remove(item);
	}

	/**
	 * Returns the list of rewards in string format.
	 * <br/>It will look as follows:
	 * <br/>[Reward] x[Quantity]
	 * <br/>[Reward] x[Quantity]
	 * <br/> ....other rewards....
	 * @return
	 */
	public String getRewardListText(){
		StringBuffer rl = new StringBuffer("");
		for(Blob b : gainableBlobs.keySet()){
			int num = gainableBlobs.get(b);
            rl.append(b.getName()+" x"+num+"\n");
		}
		for(Item i : gainableItems.keySet()){
			int num = gainableItems.get(i);
            rl.append(i.getName()+" x"+num+"\n");
		}
		
		return rl.toString();
	}
	
	/**
	 * @return true if player has already received reward for event.
	 */
	public boolean playerHasBeenHere(){
		return playerHasBeenHere;
	}
	
	public HashMap<EnemyBlob, Integer> getBlobRewardList(){
		return gainableBlobs;
	}
	
	public HashMap<Item, Integer> getItemRewardList(){
		return gainableItems;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String desc) {
		description = desc;
	}
	

}
