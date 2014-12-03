public class CombineInstance {
    public PersonalBlob doCombine(PersonalBlob base, PersonalBlob combine) {
        int combineTier = combine.getTier();

        int newHp = hpAlgorithm(base, combine, combineTier);
        int newSp = spAlgorithm(base, combine, combineTier);
        int newAtk = atkAlgorithm(base, combine, combineTier);
        int newDef = defAlgorithm(base, combine, combineTier);

        PersonalBlob newBlob = new PersonalBlob();
    }

    private int hpAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
        int newHp = 0;
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

    private int atkAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
        int newAtk = 0;
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

    private int defAlgorithm(PersonalBlob base, PersonalBlob combine, int combineTier) {
        int newDef = 0;
        if(combineTier == 1) {
            newHP = base.getDef() + combine.getDef()*.5;
        }
        else if(combineTier == 2) {
            newHP = base.getDef() + combine.getDef()*.75;
        }
        else if(combineTier == 3) {
            newHP = base.getDef() + combine.getDef();
        }
        return newDef;
    }
}
