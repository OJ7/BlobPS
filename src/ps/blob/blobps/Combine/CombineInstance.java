package ps.blob.blobps.Combine;

import java.util.Random;

import ps.blob.blobps.BlobPS;
import ps.blob.blobps.Game;
import ps.blob.blobps.Blob.EnemyBlob;
import ps.blob.blobps.Blob.PersonalBlob;
import ps.blob.blobps.Special.Special;

/**
 * Created by Jason on 12/3/2014.
 * Slightly modified by Chijioke on 12/12/2014. (mostly resolve syntax errors)
 */
public class CombineInstance {
    public static PersonalBlob doCombine(PersonalBlob base, PersonalBlob combine) {
        String newName = base.getPersonalName()+combine.getPersonalName(); 
        String newPersonalName = base.getName()+combine.getName();
    	
    	int combineTier = combine.getTier();

        int newHp = hpAlgorithm(base, combine, combineTier);
        int newSp = spAlgorithm(base, combine, combineTier);
        int newAtk = atkAlgorithm(base, combine, combineTier);
        int newDef = defAlgorithm(base, combine, combineTier);
        
        double newRarity = base.getRarity() * combine.getRarity();
        		
        String image = base.getImageReference(); //for now just base

        Special newSpecial; //special randomly inherited from one of the blobs
        Random random = new Random();
        switch(random.nextInt(2)){
        case 0:
        	newSpecial = base.getSpecial();
        	break;
        case 1:
        	newSpecial = combine.getSpecial();
        	break;
        default:
        	newSpecial = base.getSpecial();
        }
        PersonalBlob newBlob = new PersonalBlob(newPersonalName, base.getOwner(), 
        		newName, newHp, newSp, newAtk, newDef, newRarity, image, base.getDropList(), newSpecial);
        newBlob.setTier(combineTier);
        Game.getInstance().addBlobToGame(new EnemyBlob(newBlob));
        BlobPS.getInstance().getPlayer().addBlob(newBlob);
        return newBlob;
    }

    private static int hpAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
        double newHp = 0;
        if(combineTier == 1) {
            newHp = base.getHP() + combine.getHP()*.5;
        }
        else if(combineTier == 2) {
            newHp = base.getHP() + combine.getHP()*.75;
        }
        else if(combineTier == 3) {
            newHp = base.getHP() + combine.getHP();
        }
        return (int) newHp;
    }

    private static int spAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
        int newSp = 0;
        if(combineTier == 1) {
            newSp = base.getSP() + 1;
        }
        else if(combineTier == 2) {
            newSp = base.getSP() + 2;
        }
        else if(combineTier == 3) {
            newSp = base.getSP() + 3;
        }
        return (int)newSp;
    }

    private static int atkAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
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
        return (int) newAtk;
    }

    private static int defAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
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
        return (int) newDef;
    }
}
