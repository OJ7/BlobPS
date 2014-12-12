package ps.blob.blobps;

import java.util.Random;
import java.util.TreeMap;

import ps.blob.blobps.Blob.*;
import ps.blob.blobps.Special.Special;

public class Game {

	public final static Game instance = new Game();
	private TreeMap<String, EnemyBlob> allBlobs; //using EnemyBlob as dictionary entry
	private TreeMap<String, Item> allItems;
	private TreeMap<String, Special> allSpecials;
	private Player player;

	/**
	 * The game itself, that is this is the "master" of the back-end.
	 * <br/>
	 * All items, specials, and blobs are defined here.
	 */
	public Game(){
		player = new Player();
		allBlobs = new TreeMap<String, EnemyBlob>();
		allItems = new TreeMap<String, Item>();
		allSpecials = new TreeMap<String, Special>();

		/* Blob */
		defineBlobs();
		/* Items */
		defineItems();
		/* Specials */
		defineSpecials();
	}

	private void defineBlobs(){}

	private void defineItems(){}

	private void defineSpecials(){
		Special heavyHit = new Special("Heavy Hit", 
				"An impressive attack that deals damage 1.5x the blob's ATK.", 
				Special.HEAVY_HIT);
		Special vampHit = new Special("Vamp", 
				"Deals 1x damage of the blob's ATK and heals for the same amount.",
				Special.VAMP);
		Special heal = new Special("Heal", ""
				+ "It heals itself for 1/2 of it's max HP.",
				Special.PARTIAL_HEAL);

		allSpecials.put(heavyHit.getName(), heavyHit);
		allSpecials.put(vampHit.getName(), vampHit);
		allSpecials.put(heal.getName(), heal);
	}

	/**
	 * Adds a blob to the game. Takes in an EnemyBlob as those are what are
	 * fought and found in the wild. So by default they define the dictionary.
	 * New combined blobs are also added to the dictionary and thus to the Map.
	 * @param blob
	 */
	public void addBlobToGame(EnemyBlob blob){
		allBlobs.put(blob.getName(), blob);
	}

	/**
	 * Generates a random blob according to the ranges passed in. This method
	 * will only consider the first two indexes of an array. If an array of less
	 * than size 2 is passed in, then it will throw an IllegalArgumentException.
	 * @param special
	 * @param atkRange
	 * @param defRange
	 * @param spRange
	 * @param rarity
	 * @param tier
	 * @return
	 */
	public EnemyBlob createRandomBlob(int special, int[] atkRange, int[] defRange, 
			int[] spRange, double rarity, int tier){
		String name = nameRandomizer();
		//TODO: this
		return null;
	}

	/**
	 * Generates a name. If all possible names are taken, it will return
	 * "AddMoreStringsx" where x is between (0,10000]. Returns null if there is
	 * an error in the code.
	 * @return randomly generated name, or "AddMoreStringsx"
	 */
	private String nameRandomizer(){
		String[] setColor =
			{"Red", "Blue", "Green", "Yellow", "Pink", "Purple", "Violet",
				"Orange", "Brown", "Black", "White", "Silver", "Gray",
				"Gold", "Bronze", "Cyan", "Maroon"};
		String[] setGreek =
			{"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta",
				"Iota", "Kappa", "Lambda", "Mu", "Nu", "Omicron", "Pi", "Rho", 
				"Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"};
		String[] setAdj=
			{"Ominous", "Ugly", "Hideous", "Shiny", "Ghetto", "Reformed",
				"Libertarian", "Nerdy", "Scary", "Wimpy", "Cocky",
				"Egotistical", "Conceited", "Neutral", "Fair", "Average",
				"Unimpressive", "Shallow", "Generous", "Pretty", "Fabulous",
				"Smart", "Smelly", "Stinky", "Toxic", "Glowy", "Annoying",
				"Gloomy", "Lonely", "Bright", "Special"};

		boolean taken = true;
		Random r = new Random();
		int maxLoop = 1000;
		int currLoop = 1;
		while(taken){
			int adjIndex = r.nextInt(setAdj.length);
			int colorIndex = r.nextInt(setColor.length);
			int greekIndex = r.nextInt(setGreek.length);
			String name;
			switch(r.nextInt(6)){
			case 0:
				name = setColor[colorIndex]+setGreek[greekIndex]+setAdj[adjIndex];
			case 1:
				name = setColor[colorIndex]+setAdj[adjIndex]+setGreek[greekIndex];
			case 2:
				name = setGreek[greekIndex]+setColor[colorIndex]+setAdj[adjIndex];
			case 3:
				name = setGreek[greekIndex]+setAdj[adjIndex]+setColor[colorIndex];
			case 4:
				name = setAdj[adjIndex]+setColor[colorIndex]+setGreek[greekIndex];
			case 5:	
				name = setAdj[adjIndex]+setGreek[greekIndex]+setColor[colorIndex];
			default:
				name = setAdj[adjIndex]+setColor[colorIndex]+setGreek[greekIndex];
			}
			if(!allBlobs.containsKey(name)){
				return name;
			}
			
			if(currLoop == maxLoop){
				return "AddMoreStrings"+r.nextInt(10000);
			}
			currLoop++;
		}
		
		/*Should not be possible to reach here*/
		return null;
	}

}
