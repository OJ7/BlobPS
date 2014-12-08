package ps.blob.blobsps.Combine;

import java.util.Random;

import ps.blob.blobsps.Blob.PersonalBlob;

/**
 * Created by Jason on 12/3/2014.
 */
public class CombineInstance {
    public PersonalBlob doCombine(PersonalBlob base, PersonalBlob combine) {
        int combineTier = combine.getTier();

        double newHp = hpAlgorithm(base, combine, combineTier);
        int newSp = spAlgorithm(base, combine, combineTier);
        double newAtk = atkAlgorithm(base, combine, combineTier);
        double newDef = defAlgorithm(base, combine, combineTier);

        String image = "";

        Random random = new Random();
        int special = random.nextInt(2 - 0 + 1) + 0;
        PersonalBlob newBlob = new PersonalBlob("insert random name here", newHp, newSp, newAtk, newDef, 1.0, false, image, special);
        return newBlob;
    }

    private double hpAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
        double newHp = 0;
        if(combineTier == 1) {
            newHp = base.getHp() + combine.getHp()*.5;
        }
        else if(combineTier == 2) {
            newHp = base.getHp() + combine.getHp()*.75;
        }
        else if(combineTier == 3) {
            newHp = base.getHp() + combine.getHp();
        }
        return newHp;
    }

    private int spAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
        int newSp = 0;
        if(combineTier == 1) {
            newSp = base.getSp() + 1;
        }
        else if(combineTier == 2) {
            newSp = base.getSp() + 2;
        }
        else if(combineTier == 3) {
            newSp = base.getSp() + 3;
        }
        return newSp;
    }

    private double atkAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
        double newAtk = 0;
        if(combineTier == 1) {
            newAtk = base.getAtk() + combine.getAtk()*.5;
        }
        else if(combineTier == 2) {
            newAtk = base.getAtk() + combine.getAtk()*.75;
        }
        else if(combineTier == 3) {
            newAtk = base.getAtk() + combine.getAtk();
        }
        return newAtk;
    }

    private double defAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
        double newDef = 0;
        if(combineTier == 1) {
            newDef = base.getDef() + combine.getDef()*.5;
        }
        else if(combineTier == 2) {
            newDef = base.getDef() + combine.getDef()*.75;
        }
        else if(combineTier == 3) {
            newDef = base.getDef() + combine.getDef();
        }
        return newDef;
    }
}
