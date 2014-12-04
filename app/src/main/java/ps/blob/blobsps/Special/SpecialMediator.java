package ps.blob.blobsps.Special;

import java.util.HashMap;

/**
 * Created by Jason on 12/3/2014.
 */
public class SpecialMediator {
    public HashMap<Integer, Special> specialList;

    private void createSpecials() {
        Special heavyHit = new Special("Heavy Hit", "An impressive attack that deals 1.5x the blob's innate damage.");
        specialList.put(0, heavyHit);
        Special vampHit = new Special("Vamp", "It hits for 1x of the blob's innate damage and heals itself for that much.");
        specialList.put(1, vampHit);
        Special heal = new Special("Heal", "It heals itself for 1/2 of it's max hp");
        specialList.put(2, heal);
    }

    public SpecialMediator() {
        this.specialList = new HashMap<Integer, Special>();
        createSpecials();
    }

    public HashMap<Integer, Special> getSpecialList() {
        return specialList;
    }
}
