package ps.blob.blobps.Blob;

import java.util.HashMap;

import ps.blob.blobps.Item;
import ps.blob.blobps.Player;
import ps.blob.blobps.Special.Special;

/**
 * Created by Jason on 12/3/2014.
 * Modified by Chijioke/nuplex 12/11/2014
 */
public class PersonalBlob extends Blob {
	private String personalName;
	private final int type = PERSONAL;
	private Player owner;
	/** Because we need to be able to access the currentHP and the maxHP at 
	 * the same time, or without losing the original max hp value */
	private int currentHP;
	/** Because we need to be able to access the currentSP and the maxSP at 
	 * the same time, or without losing the original max sp value */	
	private int currentSP;
	/** Mostly for displaying damage if we want to do that, may have other
	 * use that may come up;
	 */
	int lastDamageTaken;
	
	/**
	 * Used for creating new a PersonalBlob from scratch 
	 * (it did not exist in the Game.allBlobs dictionary)
	 * @param personalName
	 * @param owner
	 * @param name
	 * @param hp
	 * @param sp
	 * @param atk
	 * @param def
	 * @param rarity
	 * @param image
	 * @param dropList
	 * @param special
	 */
	public PersonalBlob(String personalName, Player owner, String name, int hp, int sp, int atk, int def, double rarity, String image, HashMap<Double, Item> dropList, Special special){
		super(name, hp, sp, atk, def, rarity, image, dropList, special);
		this.personalName = personalName;
		this.owner = owner;
		currentHP = hp;
		currentSP = sp;
		lastDamageTaken = 0;
	}
	
	/**
	 * PersonalBlob constructor that effectively can make an EnemyBlob a 
	 * PersonalBlob instantly. This is also is the one you would use when te blob
	 * is already present in the Game.allBlobs dictionary
	 * @param personalName
	 * @param owner
	 * @param blob
	 */
	public PersonalBlob(String personalName, Player owner, Blob blob){
		super(blob);
		this.personalName = personalName;
		this.owner = owner;
		currentHP = hp;
		currentSP = sp;
		lastDamageTaken = 0;
	}
	
	public void fullHeal(){
		currentHP = hp;
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

	public String getPersonalName() {
		return personalName;
	}
	
	public void setPersonalName(String name){
		this.personalName = name;
	}

	public int getType() {
		return type;
	}

	public Player getOwner() {
		return owner;
	}
	
    @Override
    public boolean equals(Object o){
    	if(o == null){
    		return false;
    	} else if (!(o instanceof PersonalBlob)){
    		return false;
    	} else {
    		PersonalBlob pb = (PersonalBlob) o;
    		return pb.getPersonalName().equals(this.personalName);
    	}
    }
}

