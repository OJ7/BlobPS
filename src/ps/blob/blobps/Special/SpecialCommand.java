package ps.blob.blobsps.Special;

import ps.blob.blobsps.Blob.Blob;

/**
 * Created by Jason on 12/3/2014.
 */
public class SpecialCommand {
    int index;

    public SpecialCommand(int i) {
        this.index = i;
    }

    public void processSpecial(Blob blob1, Blob blob2) {
        if(index == 0) {
            processHeavyHit();
        }
        else if(index == 1) {
            processVampHit();
        }
        else if(index == 2) {
            processHeal();
        }
    }

    private void processHeavyHit() {

    }

    private void processVampHit() {

    }

    private void processHeal() {

    }
}
