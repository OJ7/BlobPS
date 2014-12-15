package ps.blob.blobps.Blob;

import java.util.ArrayList;
import java.util.HashMap;

import ps.blob.blobps.Item;
import ps.blob.blobps.Player;
import ps.blob.blobps.Special.Special;

/**
 * A blob that is fought against or found in the wild.
 * @author Chijioke/nuplex
 */
public class EnemyBlob extends Blob {
	private final int type = ENEMY;
	/** Because we need to be able to access the currentHP and the maxHP at 
	 * the same time, or without losing the original max hp value */
	private int currentHP;
	/** Because we need to be able to access the currentSP and the maxSP at 
	 * the same time, or without losing the original max sp value */	
	private int currentSP;
	/** Mostly for displaying damage if we want to do that, may have other
	 * use that may come up; 
	 * This actually useful for seeing/getting damage taken from specials.
	 */
	int lastDamageTaken;
	
	
	public EnemyBlob(String name, int hp, int sp, int atk, int def, double rarity,
			String image, HashMap<Double, Item> dropList, Special special){
		super(name, hp, sp, atk, def, rarity, image, dropList, special);
		currentHP = hp;
		currentSP = sp;
		lastDamageTaken = 0;
	}
	
	/**
	 * Creates an enemy blob. This can also make a PersonalBlob into an 
	 * Enemy Blob
	 * @param blob
	 */
	public EnemyBlob(Blob blob){
		super(blob);
		currentHP = hp;
		currentSP = sp;
		lastDamageTaken = 0;
	}
	
	/**
	 * If the blob is  successfully captured, returns a 
	 * new, fully healed, personal blob for the player.
	 * @param name
	 * @param owner
	 * @return blob
	 */
	public PersonalBlob capture(String name, Player owner){
		return new PersonalBlob(name, owner, this);
	}

	/**
	 * Attempts to capture the blob using the item. If it capture is successful,
	 * returns true, otherwise false. <br/><b>Note that this method does actually 
	 * capture the blob, so you must call capture() on your own if it returns
	 * true.</b>
	 * <br/>
	 * <b>This uses the item!</b> 
	 * <br/> 
	 * This method is basically a theoretical capture. It is meant to keep
	 * from having to do error throwing or null-checking in capture(). Again, only
	 * call capture() if this returns true.
	 * @param item
	 * @return true if blob was captured.
	 */
	public boolean attemptCapture(Item item){
		return item.capture(this);
	}
	
	public ArrayList<Item> dropItems(){
		ArrayList<Item> drops = new ArrayList<Item>();
		for(Double d : dropList.keySet()){
			if(Item.getAnOutcome(d)){
				drops.add(dropList.get(d));
			}
		}
		return drops;
	}
	
	public boolean isDead(){
		return currentHP == 0;
	}

	public boolean isOutOfSP(){
		return currentSP == 0;
	}
	
	@Override
	public void receiveDamage(int damage) {
		lowerHP(damage);
		lastDamageTaken = damage;
	}
	
	@Override
	public void raiseHP(int amount) {
		int newHP = currentHP + amount;
		currentHP = (newHP > hp) ? hp:newHP;
	}

	@Override
	public void lowerHP(int amount) {
		int newHP = currentHP - amount;
		currentHP = (newHP < 0) ? 0:newHP;
	}

	@Override
	public void raiseSP(int amount) {
		int newSP = currentSP + amount;
		currentSP = (newSP > sp) ? sp:newSP;
	}

	@Override
	public void lowerSP(int amount) {
		int newSP = currentSP - amount;
		currentSP = (newSP < 0) ? 0:newSP;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}

	public int getCurrentSP() {
		return currentSP;
	}

	public void setCurrentSP(int currentSP) {
		this.currentSP = currentSP;
	}

	public int getLastDamageTaken() {
		return lastDamageTaken;
	}

	public void setLastDamageTaken(int lastDamageTaken) {
		this.lastDamageTaken = lastDamageTaken;
	}

	public int getType() {
		return type;
	}
	
	@Override
    public boolean equals(Object o){
    	if(o == null){
    		return false;
    	} else if (!(o instanceof EnemyBlob)){
    		return false;
    	} else {
    		EnemyBlob pb = (EnemyBlob) o;
    		return pb.name.equals(this.name);
    	}
    }
}