package ps.blob.blobps;

import java.util.Random;

import ps.blob.blobps.Blob.Blob;

/**
 * Represents an item.
 * </br>
 * Heal 25% - Heals 25% of Blob HP | 30% chance of finding in the wild</br>
 * Heal 50% - Heals 50% of Blob HP | 15%</br>
 * Full Heal -  Heals all of blobs HP | 10%</br>
 * Average Capture- 70% chance of capturing blob, lowers with higher tier by 10% per tier | 10%</br>
 * Advanced Capture - 85% chance of capturing blob, lowers with higher tier by 5% per tier | 3%</br>
 * Master Capture - 100% chance of capturing blob, lowers with higher tier by 2.5% per tier |0.1%
 *
 * @author Chijioke/nuplex
 *
 */
public class Item {

	private final static int HEAL_25 = 825, HEAL_50 = 850, HEAL_100 = 8100,
			CAPTURE_70 = 370, CAPTURE_85 = 385, CAPTURE_100 = 3100;

	public final static int HEAL = 8, CAPTURE = 3;
	/** The item's actual code*/
	private int itemCode;
	private int type;
	private String name;
	private String description;
	/** Chance of fining this item in the wild */
	private double chanceFinding;
	private String imageReference;
	//used for determining capture chances
	private static Random numPicker;

	public Item(int item, String name, String description, double chanceFinding, 
			String imageReference) {
		this.itemCode = item;
		switch(item){
		case HEAL_25:
		case HEAL_50:
		case HEAL_100:
			type = HEAL;
			break;
		case CAPTURE_70:
		case CAPTURE_85:
		case CAPTURE_100:
			type = CAPTURE;
			break;
		default:
			throw new IllegalArgumentException("item passed in is not valid or had not assigned type");
		}

		this.name = name;
		this.description = description;
		this.chanceFinding = chanceFinding;
		this.imageReference = imageReference;
		numPicker = new Random();

	}

	/**
	 * Copy constructor
	 * @param item
	 */
	public Item(Item item){
		this.itemCode = item.itemCode;
		this.type = item.type;
		this.name = item.name;
		this.description = item.description;
		this.chanceFinding = item.chanceFinding;
		this.imageReference = item.imageReference;
	}

	/**
	 * Returns an instance of an item, for use by player or blob
	 * @param itemCode
	 * @return
	 */
	public Item getItemInstance(Item item){
		return new Item(item);
	}

	/**
	 * Heals the blob with the item.
	 * @param blob
	 */
	public void heal(Blob blob){
		switch(itemCode){
		case HEAL_25:
			blob.raiseHP((int)(blob.getHP()*(0.25)));
			break;
		case HEAL_50:
			blob.raiseHP((int)(blob.getHP()*(0.50)));
			break;
		case HEAL_100:
			blob.setHP(blob.getHP());;
			break;
		default:
			throw new IllegalArgumentException("Unknown heal item");
		}
	}

	/**
	 * If item is successful returns true, returns false if the item failed. 
	 * <br/> <b>DOES NOT ACTUALLY CAPTURE BLOB</b> 
	 * This is to save from error or null checking. 
	 * If this function returns true, capture the blob
	 * use <b>EnemyBlob.capture()</b>
	 * @param blob
	 * @return false if capture failed, true otherwise
	 */
	public boolean capture(Blob blob){
		if(blob.getType() != Blob.ENEMY){
			throw new IllegalArgumentException("Attempted to capture non-ememy blob");
		} else {
			double chance;
			
			switch(itemCode){
			case CAPTURE_70:
				chance = 0.70;
				//assuming tiers start at 1
				// - 1 because tier 1 is already 70%
				chance = chance - ((blob.getTier() - 1)*0.10);
				return getAnOutcome(chance);
			case CAPTURE_85:
				chance = 0.85;
				chance = chance - ((blob.getTier() - 1)*0.05);
				return getAnOutcome(chance);
			case CAPTURE_100:
				chance = 1.0;
				chance = chance - ((blob.getTier() - 1)*0.025);
				return getAnOutcome(chance);
			default:
				throw new IllegalArgumentException("Unknown capture item");
			}
		}
	}

	/**
	 * @param chancePercent percentage chance
	 * @return true if fell within chance percentage, false if did not.
	 */
	public static boolean getAnOutcome(double chancePercent){
		/* The cap for numPicker. For example of how this works, if there is a 71.2% 
		 * chance, then if numPicker chooses a number at or under 712, the monster is 
		 * captured. Note that chancePercent will never go beyond two decimal points,
		 * that is XX.X% or 00.xxx is the only thing that will come in.
		 */
		int cap = 1000; //1000 to allow chanceNum to always be an integer
		int chanceNum = (int) (chancePercent * 1000);
		int numChosen = numPicker.nextInt(cap)+1; //+1 because nextInt is [0, n)
		if(numChosen <= chanceNum){
			return true;
		}
		return false;
	}

	public int getItemCode() {
		return itemCode;
	}


	public int getType() {
		return type;
	}


	public String getName() {
		return name;
	}


	public String getDescription() {
		return description;
	}


	public double getChanceFinding() {
		return chanceFinding;
	}


	public String getImageReference() {
		return imageReference;
	}


	public void setImageReference(String location){
		imageReference = location;
	}
}