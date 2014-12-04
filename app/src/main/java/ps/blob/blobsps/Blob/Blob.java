package ps.blob.blobsps.Blob;

import java.util.HashMap;

import ps.blob.blobsps.Item;
import ps.blob.blobsps.Special.Special;
import ps.blob.blobsps.Special.SpecialCommand;

public abstract class Blob {
    protected String blobName;
    protected final static int BLOB_TYPE = 0;
    protected int hp;
    protected int sp;
    protected int atk;
    protected int def;
    protected SpecialCommand special;
    protected double rarity;
    // recipe instead of wild because it will be true for all cases except
    // recipe cases
    protected boolean recipe;
    protected int tier;
    protected String imageReference;
    protected HashMap<Double, Item> dropList;

    public Blob(String name, int hp, int sp, int atk, int def, double rarity, boolean recipe, String image, HashMap<Double, Item> dropList, int special) {
        this.blobName = name;
        this.hp = hp;
        this.sp = sp;
        this.atk = atk;
        this.def = def;
        this.rarity = rarity;
        this.recipe = recipe;
        this.imageReference = image;
        this.dropList = dropList;
        this.special = new SpecialCommand(special);
    }

    public void attack(Blob blob) {
        int damage = this.atk - blob.getDef();
        if (damage < 0) {
            damage = 0;
        }
        blob.receiveDamage(damage);
    }

    public void receiveDamage(int damage) {
        int newHp = this.getHp() - damage;
        this.setHp(newHp);
    }

    public abstract void useItem(Item item);

    public void useSpecial(Blob userBlob, Blob enemyBlob){
       // special.use(userBlob, enemyBlob);
    }

    public int getHp() {
        return this.hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getSp() {
        return this.sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

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

    public double getRarity() {
        return this.rarity;
    }

    public int getTier() {
        return tier;
    }

    public String getImageReference() {
        return imageReference;
    }

    public HashMap<Double, Item> getDropList() {
        return this.dropList;
    }
}