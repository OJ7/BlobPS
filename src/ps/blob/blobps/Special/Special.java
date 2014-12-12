package ps.blob.blobps.Special;

import ps.blob.blobps.Blob.Blob;

public class Special {
	public final static int HEAVY_HIT = 1, VAMP = 2, PARTIAL_HEAL = 3;
	private final int specialCode;
    private String name;
    private String description;

    public Special(String description, String name, int specialCode) {
        this.name = name;
        this.description = description;
        this.specialCode = specialCode;
    }

    public void use(Blob userBlob, Blob enemyBlob){
    	switch(specialCode){
    		case HEAVY_HIT:
    			heavyHit(userBlob, enemyBlob);
    			break;
    		case VAMP:
    			vamp(userBlob, enemyBlob);
    			break;
    		case PARTIAL_HEAL:
    			partialHeal(userBlob);
    			break;
    	}
    }
    
    private void heavyHit(Blob b1, Blob b2){
    	b2.receiveDamage((int)(b1.getAtk()*(1.5)));
    }
    
    private void vamp(Blob b1, Blob b2){
    	b1.raiseHP(b1.getAtk());
    	b2.receiveDamage(b1.getAtk());
    }
    
    private void partialHeal(Blob b1){
    	b1.raiseHP((int)(b1.getHP()*0.5));
    }
    
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}