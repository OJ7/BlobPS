package ps.blob.blobsps;

import java.util.HashMap;

public abstract class Blob {
    protected String blobName;
    protected final static int BLOB_PERSONAL = 0, BLOB_ENEMY = 1;
    protected int hp;
    protected int sp;
    protected int atk;
    protected int def;
    protected Special special;  
    protected double rarity;
    // recipe instead of wild because it will be true for all cases except
    // recipe cases
    protected boolean recipe;
    protected int tier;
    protected String imageReference;
    protected HashMap<Double, Item> dropList;

    public Blob(String name, int hp, int sp, int atk, int def, double rarity, boolean wild, String image, HashMap<Double, Item> dropList, int special) {
        this.blobName = name;
        this.hp = hp;
        this.sp = sp;
        this.atk = atk;
        this.def = def;
        this.rarity = rarity;
        //this.wild = wild;
        this.imageReference = image;
        this.dropList = dropList;
        //How did you want to do this special? did you want something like a seed because in the spec it said
        //int special. Either way I set up the constructor to take a seed so change it how you will
        //this.special = Special(special);
    }

    //public abstract void attack(Blob blob) {
    public void attack(Blob blob) {
        int damage = this.atk - blob.getDef();
        if (damage < 0) {
            damage = 0;
        }
        blob.setHp(blob.getHp() - damage);
    }

    //I don't think we need to do recieve damage because its just getting the
    //blobHp and then subtracting damage from it.

    public abstract void useItem(Item item);

    //Still confused on special. Chijioke can you explain a bit more

    public abstract void useSpecial();

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