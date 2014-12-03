package ps.blob.blobsps.Blob;

import ps.blob.blobsps.Item;
import ps.blob.blobsps.Special.Special;

/**
 * Created by Jason on 12/3/2014.
 */
public class PersonalBlob extends Blob {
    protected String blobName;
    protected final static int BLOBTYPE = 1;
    protected int hp;
    protected int sp;
    protected int atk;
    protected int def;
    protected Special special;
    protected double rarity;
    protected boolean recipe;
    protected int tier;
    protected String imageReference;

    public PersonalBlob(String name, int hp, int sp, int atk, int def, double rarity, boolean recipe, String image, int special) {
        super(name, hp, sp, atk, def, rarity, false, image, null, special);
    }

    public void useItem(Item item) {
        // TODO
    }

    public void useSpecial(Blob userBlob, Blob enemyBlob){
        // special.use(userBlob, enemyBlob);
    }
}

