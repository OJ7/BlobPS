package ps.blob.blobps.Map;

import java.util.Random;

import com.google.android.gms.maps.model.LatLng;
/**
 * Represents an area that the player has been in.
 * @author Chijioke/nuplex
 *
 */
public class KnownArea extends Area {
	/**
	 * Constructor for KnownArea. Generates rank randomly.
	 * @param areaSW
	 * @param areaNE
	 * @param x
	 * @param y
	 * @param name
	 */
	public KnownArea(LatLng areaSW, LatLng areaNE, int x, int y, String name) {
		super(areaSW, areaNE, x, y, name);
		setType(KNOWN);
		rank = generateRank();
	}
	
	/**
	 * Constructor for KnownArea. Generates rank randomly.
	 * @param areaSW
	 * @param areaNE
	 * @param x
	 * @param y
	 * @param name
	 */
	public KnownArea(Area area){
		super(area.areaSW, area.areaNE, area.x, area.y, area.getName());
		setType(KNOWN);
		rank = generateRank();
	}
	
	/**
	 * Constructor for KnownArea.
	 * @param areaSW
	 * @param areaNE
	 * @param x
	 * @param y
	 * @param name
	 * @param rank - The rank of the area. Valid ranks are A-E.
	 */
	public KnownArea(LatLng areaSW, LatLng areaNE, int x, int y, String name,
			String rank) {
		super(areaSW, areaNE, x, y, name);
		setType(KNOWN);
		toRank(rank);
	}
	
	/**
	 * Constructor for KnownArea.
	 * @param areaSW
	 * @param areaNE
	 * @param x
	 * @param y
	 * @param name
	 * @param rank - The rank of the area. Valid ranks are A-E.
	 */
	public KnownArea(Area area, String rank){
		super(area.areaSW, area.areaNE, area.x, area.y, area.getName());
		setType(KNOWN);
		toRank(rank);
	}
	
	/**
	 * Generates a new random rank from A-E.
	 * @return the rank
	 */
	public String generateRank(){
		Random r = new Random();
		int choice = r.nextInt(ranks.length);
		return ranks[choice];
	}
	
	/**
	 * Makes this area the passed in rank.
	 * Valid ranks are from A-E.
	 * @param rank
	 */
	public void toRank(String rank){
		boolean valid = false;
		for(int i = 0; i < ranks.length; i++){
			if(ranks[i].equals(rank)){
				valid = true;
				break;
			}
		}
		if(!valid){
			throw new IllegalArgumentException("Valid rank areas are A-E, "
					+ "the value "+rank+" was passed in.");
		}
		this.rank = rank;
	}
	

}
