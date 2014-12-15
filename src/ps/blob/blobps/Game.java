package ps.blob.blobps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import ps.blob.blobps.Battle.BattleInstance;
import ps.blob.blobps.Blob.*;
import ps.blob.blobps.Combine.CombineInstance;
import ps.blob.blobps.Special.Special;

/**
 * The game itself. Contains game information such as blobs, specials, and items.
 * Also manages battles (to an extent) and combining.
 * @author Chijioke/nuplex
 *
 */
public class Game implements Serializable {

	private static final long serialVersionUID = -4661826553199738447L;

	/**Instance for use by other classes under Game (e.g. Item, BattleInstance, etc.)
	 * . To those classes there is ever only one Game, so it
	 * is functionally static.
	 */
	private static Game instance;
	private TreeMap<String, EnemyBlob> allBlobs; //using EnemyBlob as dictionary entry
	private TreeMap<String, Item> allItems;
	private TreeMap<String, Special> allSpecials;
	private Player player;
	
	//Here because this class can be saved.
	private static int nextPlayerID = 1;
	
	

	/** Mins for completelyRandomBlob to secure a sense of balancing*/
	private final static int RANDOM_HP_MIN = 10, RANDOM_SP_MIN = 1,
			RANDOM_ATK_MIN = 3, RANDOM_DEF_MIN = 3;
	/** Caps for completelyRandomBlob to secure a sense of balancing*/
	private final static int RANDOM_HP_CAP = 10000, RANDOM_SP_CAP = 30,
			RANDOM_ATK_CAP = 3000, RANDOM_DEF_CAP = 3000;

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

		defineBlobs();
		defineItems();
		defineSpecials();
		
		nextPlayerID = 0;
		
		instance = this;
	}

	@SuppressWarnings("static-access")
	public Game(Game game){
		this.allBlobs = new TreeMap<String, EnemyBlob>(game.allBlobs);
		this.allItems = new TreeMap<String, Item>(game.allItems);
		this.allSpecials = new TreeMap<String, Special>(game.allSpecials);
		this.player =  new Player(game.player);
		this.nextPlayerID = game.nextPlayerID; //to be safe;
		
		instance = this;
	}

	private void defineBlobs(){
		//For now no hard-defined blobs
		createCompletelyRandomBlobs(50);
	}

	private void defineItems(){
		Item cookie = new Item(Item.HEAL_25, "Cookie", 
				"Nom nom heals 25% of blob's HP", 0.30, "");
		allItems.put(cookie.getName(), cookie);
		Item cake = new Item(Item.HEAL_50, "Cake",
				"Delicious plain cake, heals 50% of blob's HP", 0.15, "");
		allItems.put(cake.getName(), cake);
		Item blobbyTreats = new Item(Item.HEAL_100, "Blobby Treats",
				"Like doggy treats, only for blobs. Fully heals blob", 0.10,"");
		allItems.put(blobbyTreats.getName(), blobbyTreats);

		Item sponge = new Item(Item.CAPTURE_70, "Sponge", 
				"Soaks up blobs at a 70% success rate, lower rate with higher tier blobs!",
				0.10, "");
		allItems.put(sponge.getName(), sponge);

		Item superSponge = new Item(Item.CAPTURE_85, "Super Sponge", 
				"Super soaks up blobs at a 85% success rate, lower rate with higher tier blobs!",
				0.10, "");
		allItems.put(superSponge.getName(), superSponge);

		Item saharaSponge = new Item(Item.CAPTURE_100, "Sahara Sponge", 
				"Soaks up blobs at a 100% success rate, lower rate with higher tier blobs!",
				0.10, "");
		allItems.put(saharaSponge.getName(), saharaSponge);

	}

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
	 * Generates a new random blob according to the ranges passed in and adds it to the
	 * allBlobs dictionary. This method
	 * will only consider the first two indexes of an array. If an array of less
	 * than size 2 is passed in, then an ArrayOutOfBoundsException will be thrown.
	 * <br/><br/>
	 * Array Format: [lowerBound, UpperBound], lower and upper included in 
	 * possible values.
	 * 
	 * @param special
	 * @param atkRange
	 * @param defRange
	 * @param spRange
	 * @param rarity
	 * @param tier
	 * @return
	 */
	public EnemyBlob createNewRandomBlob(int[] hpRange, int[] spRange, 
			int[] atkRange, int[] defRange){
		String name = nameRandomizer();

		Random r = new Random();
		/* r.nextInt(range[1]-range[0] +1) + range[0] is to give proper range
		 * because random gives a number from 0 to n-1; We want lowerBound to 
		 * upperBound, including the bounds, so we add the lowerBound to make
		 * sure that is the minimum, but subtract it from n because we do not
		 * want a situation where random gives upperBound and lowerBound is
		 * added to it.
		 * 
		 */
		int hp = r.nextInt(hpRange[1]-hpRange[0]+1) + hpRange[0];
		int sp = r.nextInt(spRange[1]-spRange[0]+1) + spRange[0];
		int atk = r.nextInt(atkRange[1]-atkRange[0]+1) + atkRange[0];
		int def = r.nextInt(defRange[1]-defRange[0]+1) + defRange[0];

		int newTier = r.nextInt(3)+1;

		String image = "";

		HashMap<Double, Item> droplist = generateDroplist();

		Special newSpecial = randomSpecial();

		/*TODO: Make not awful, currently just assigns a number between 0.1 %
			and 5% randomly.
		*/
		double newRarity = 0.001;
		int loops = 5;
		while(loops > 0){
			newRarity += r.nextDouble();
			loops--;
		}
		

		EnemyBlob newBlob = new EnemyBlob(name, hp, sp, atk, def, newRarity, image, 
				droplist, newSpecial);
		newBlob.setTier(newTier);
		allBlobs.put(newBlob.getName(), newBlob);
		return newBlob;
	}

	/**
	 * Creates a new, likely unbalanced, completely random blob and
	 * adds it to the allBlobs dictionary. <br/> <b> WARNING: EXTREMELY UNBALACED </b>
	 * @return new blob
	 */
	public EnemyBlob createCompletelyRandomBlob(){
		int[] hpRange = {RANDOM_HP_MIN, RANDOM_HP_CAP};
		int[] spRange = {RANDOM_SP_MIN, RANDOM_SP_CAP};
		int[] atkRange = {RANDOM_ATK_MIN, RANDOM_ATK_CAP};
		int[] defRange = {RANDOM_DEF_MIN, RANDOM_DEF_CAP};
		return createNewRandomBlob(hpRange, spRange, atkRange, defRange);
	}

	/**
	 * Creates num blobs and adds them to the allBlobs dictionary.
	 * @param num
	 * @param hpRange
	 * @param spRange
	 * @param atkRange
	 * @param defRange
	 * @return ArrayList of blobs created
	 */
	public ArrayList<Blob> createRandomBlobs(int num, int[] hpRange, 
			int[] spRange, int[] atkRange, int[] defRange){
		ArrayList<Blob> newBlobs = new ArrayList<Blob>();
		for(int i = num; i > 0; i--){
			newBlobs.add(createNewRandomBlob(hpRange, spRange, atkRange,
					defRange));
		}
		return newBlobs;
	}

	/**
	 * Creates num blobs, completely unbalanced and random, and 
	 * adds them to the allBlobs dictionary.
	 * @param num - number blobs to make
	 * @return ArrayList of blobs created
	 */
	public ArrayList<Blob> createCompletelyRandomBlobs(int num){
		ArrayList<Blob> newBlobs = new ArrayList<Blob>();
		for(int i = num; i > 0; i--){
			newBlobs.add(createCompletelyRandomBlob());
		}
		return newBlobs;
	}


	/**
	 * Makes an PersonalBlob of the blob passed in. If the blob does not exist this 
	 * will return null 
	 * @param blobName
	 * @param personalName
	 * @param owner
	 * @return an PersonalBlob of the blob passed in or null if does not exist.
	 */
	public PersonalBlob makePersonalBlob(String blobName, String personalName
			,Player owner){
		if(!allBlobs.containsKey(blobName)){
			return null;
		} else{
			return new PersonalBlob(personalName, owner, allBlobs.get(blobName));
		}
	}

	/**
	 * Makes an EnemyBlob of the blob passed in. If the blob does not exist this 
	 * will return null
	 * @param blobName
	 * @return an EnemyBlob of the blob passed in or null if does not exist.
	 */
	public EnemyBlob makeEnemyBlob(String blobName){
		if(!allBlobs.containsKey(blobName)){
			return null;
		} else {
			return new EnemyBlob(allBlobs.get(blobName));
		}
	}
	
	/**
	 * Returns a battle instance. Note that for a battle to be operated it
	 * must be directly manipulated through the returned BattleInstance. If this
	 * was not the case, battles would be automatic, that is the player would
	 * have no control over how the battle progressed.
	 * @param player
	 * @param enemy
	 * @return the battle instance
	 */
	public BattleInstance battle(Player player, EnemyBlob enemy){
		return new BattleInstance(player, enemy);
	}

	/**
	 * Performs a combine and returns the result. 
	 * @param base
	 * @param combine
	 * @return the new combined PersonalBlob
	 */
	public PersonalBlob combine(PersonalBlob base, PersonalBlob combine){
		return CombineInstance.doCombine(base, combine);
	}

	/**
	 * Clears player data.
	 */
	public void clear(){
		player.clear();
	}
	
	/**
	 * Clears all dictionaries and lists, as well as player data.
	 */
	public void hardClear(){
		clear();
		allBlobs.clear();
		allItems.clear();
		allSpecials.clear();
	}

	/**
	 * Generates a name. If all possible names are taken, it will return
	 * "AddMoreStringsx" where x is between (0,10000]. Returns null if there is
	 * an error in the code.
	 * @return randomly generated name, or "AddMoreStringsx"
	 */
	public String nameRandomizer(){
		String[] setColor =
			{"Red", "Blue", "Green", "Yellow", "Pink", "Purple", "Violet",
				"Orange", "Brown", "Black", "White", "Silver", "Gray",
				"Gold", "Bronze", "Cyan", "Maroon", "Indigo", "Rainbow"};
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
				"Gloomy", "Lonely", "Bright", "Special", "Light", "Dark"};

		boolean taken = true;
		Random r = new Random();
		int maxLoop = 1000;
		int currLoop = 1;
		while(taken){
			int adjIndex = r.nextInt(setAdj.length);
			int colorIndex = r.nextInt(setColor.length);
			int greekIndex = r.nextInt(setGreek.length);
			String name;
			switch(r.nextInt(4)){
			case 0:
				name = setColor[colorIndex]+setAdj[adjIndex]+setGreek[greekIndex];
				break;
			case 1:
				name = setGreek[greekIndex]+setAdj[adjIndex]+setColor[colorIndex];
				break;
			case 2:
				name = setAdj[adjIndex]+setColor[colorIndex]+setGreek[greekIndex];
				break;
			case 3:	
				name = setAdj[adjIndex]+setGreek[greekIndex]+setColor[colorIndex];
				break;
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

	/**
	 * Generates a droplist. Lazy implementation - item chances of dropping
	 * are the same as in the wild.
	 * 
	 */
	private HashMap<Double, Item> generateDroplist(){
		HashMap<Double, Item> list = new HashMap<Double, Item>();

		Random r = new Random();
		int loops = r.nextInt(allItems.size());
		while(loops > 0){
			Item i = (Item) allItems.values().toArray()[r.nextInt(allItems.size())];
			if(!list.containsValue(i)){
				list.put(i.getChanceFinding(), i);
			}
			loops--;
		}

		return list;
	}

	private Special randomSpecial(){
		Random r = new Random();
		return (Special) allSpecials.values().toArray()[r.nextInt(allSpecials.size())];
	}

	public Player getPlayer(){
		return player;
	}

	public final TreeMap<String, EnemyBlob> getAllBlobs() {
		return allBlobs;
	}

	public final TreeMap<String, Item> getAllItems() {
		return allItems;
	}

	public final TreeMap<String, Special> getAllSpecials() {
		return allSpecials;
	}
	
	/**
	 * Returns a new playerID and increments it.
	 * @return next player ID
	 */
	public static int getNextPlayerID() {
		return nextPlayerID++;
	}

	public static final Game getInstance(){
		return instance;
	}

	public static void main(String[] args){
		//testing random blob creation
		int[] hpRange = {30,300};
		int[] spRange = {1,10};
		int[] atkRange = {10, 100};
		int[] defRange = {5, 95};
		System.out.println(instance.createNewRandomBlob(hpRange, spRange, atkRange,
				defRange).toString());
	}


}
