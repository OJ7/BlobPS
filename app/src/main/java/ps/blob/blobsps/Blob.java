public class Blob {
    protected String blobName;
    protected final static int BLOB_PERSONAL, BLOB_ENEMY;
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
        this.name = name;
        this.hp = hp;
        this.sp = sp;
        this.atk = atk;
        this.def = def;
        this.rarity = rarity;
        this.wild = wild;
        this.image = image;
        this.dropList = dropList;
        //How did you want to do this special? did you want something like a seed because in the spec it said
        //int special. Either way I set up the constructor to take a seed so change it how you will
        this.special = Special(special);
    }
}