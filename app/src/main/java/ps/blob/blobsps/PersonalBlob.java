public class PersonalBlob extends Blob {
    protected String blobName;
    protected final static int BLOBTYPE;
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

    public PersonalBlob(String name, int hp, int sp, int atk, int def, double rarity, boolean wild, String image, HashMap<Double, Item> dropList, int special) {
        super(name, hp, sp, atk, def, rarity, wild, image, dropList, special);
        this.BLOBTYPE = 1;
    }

    public abstract void useItem(Item item);

    //Special explained in spec

    public void useSpecial(Blob userBlob, Blob enemyBlob){
       // special.use(userBlob, enemyBlob);
    }
}