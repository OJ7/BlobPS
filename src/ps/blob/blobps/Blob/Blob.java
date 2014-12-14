package ps.blob.blobps.Blob;

import java.util.HashMap;

import ps.blob.blobps.Item;
import ps.blob.blobps.Special.Special;

public abstract class Blob {
    protected String name;
    public final static int PERSONAL = 1, ENEMY = 2;
    protected int hp;
    protected int sp;
    protected int atk;
    protected int def;
    protected Special special;
    /** Chance of finding in the wild, Max chance is 5% */
    protected double rarity;
    protected int tier;
    protected String imageReference;
    protected HashMap<Double, Item> dropList;

    public Blob(String name, int hp, int sp, int atk, int def, double rarity, 
    		String image, HashMap<Double, Item> dropList, Special special) {
        this.name = name;
        this.hp = hp;
        this.sp = sp;
        this.atk = atk;
        this.def = def;
        this.rarity = rarity;
        this.imageReference = image;
        this.dropList = dropList;
        this.special = special;
        this.tier = 1; //by default 1 for now.
    }
    
    public Blob(Blob blob){
    	this.name = blob.name;
        this.hp = blob.hp;
        this.sp = blob.sp;
        this.atk = blob.atk;
        this.def = blob.def;
        this.rarity = blob.rarity;
        this.imageReference = blob.imageReference;
        this.dropList = new HashMap<Double, Item>(blob.dropList);
        //this.special = new SpecialCommand(special);
    }
    
    public void attack(Blob blob) {
        int damage = this.atk - blob.getDef();
        if (damage < 0) {
            damage = 0;
        }
        blob.receiveDamage(damage);
    }

    public abstract void receiveDamage(int damage);

	public void useHealItem(Item item){
		item.heal(this);
	}

    public void useSpecial(Blob enemyBlob){
       special.use(this, enemyBlob);
    }

    public String getName() {
		return name;
	}

	public int getHP() {
        return hp;
    }

    /**
     * Raises blob's hp by specified amount. This is abstract because the
     * Blob class is the definition of a blob, not an instance of it. Raising
     * and lowering hp by nature is for instances.
     * @param amount
     */
    public abstract void raiseHP(int amount);
    /**
     * Lowers blob's hp by specified amount. This is abstract because the
     * Blob class is the definition of a blob, not an instance of it. Raising
     * and lowering hp by nature is for instances.
     * @param amount
     */
    public abstract void lowerHP(int amount);
    
    public void setHP(int hp) {
        this.hp = hp;
    }

    public int getSP() {
        return this.sp;
    }

    public void setSP(int sp) {
        this.sp = sp;
    }
    
    /**
     * Raises blob's sp by specified amount. This is abstract because the
     * Blob class is the definition of a blob, not an instance of it. Raising
     * and lowering sp by nature is for instances.
     * @param amount
     */
    public abstract void raiseSP(int amount);
    /**
     * Lowers blob's sp by specified amount. This is abstract because the
     * Blob class is the definition of a blob, not an instance of it. Raising
     * and lowering sp by nature is for instances.
     * @param amount
     */
    public abstract void lowerSP(int amount);
    
    public int getAtk() {
        return this.atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return this.def;
    }

    public void setDef(int def) {
        this.def = def;
    }
    
    public Special getSpecial() {
		return special;
	}

	public double getRarity() {
        return this.rarity;
    }

    public int getTier() {
        return tier;
    }
    
    public void setTier(int tier) {
    	this.tier = tier;
    }
    
    public abstract int getType();

    public String getImageReference() {
        return imageReference;
    }

    public HashMap<Double, Item> getDropList() {
        return this.dropList;
    }
    
    @Override
    public String toString(){
    	return name +"\n\tHP: "+hp+"\n\tSP: "+sp+"\n\tATK: "+atk+"\n\tDEF: "+def
    			+"\n\tSpecial: "+special.getName()+"\n\tFind Chance: "+rarity
    			+"\n\tTier: "+tier;
    }
    
}