package ps.blob.blobps.Map;

import java.util.Random;
import java.util.TreeMap;

import ps.blob.blobps.BlobPS;
import ps.blob.blobps.Item;
import ps.blob.blobps.Player;
import ps.blob.blobps.Battle.BattleInstance;
import ps.blob.blobps.Blob.EnemyBlob;
import ps.blob.blobps.android.MapsActivity;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * The map itself. This class handles overhead map functions,
 * such as determining when battles happen, handling events, and changing
 * unknown areas into known ones.
 * @author Chijioke/nuplex
 *
 */
public class Map {
	//LatLng numbers made less precise for easier calculations
	private final double NORTH = 39, EAST = -77, SOUTH = 39,
			WEST = -77;
	private final LatLng UMD_NE = new LatLng(NORTH, EAST), 
			UMD_SW = new LatLng(SOUTH, WEST);
	private final int AREAS_ACROSS = 8, AREAS_DOWN = 8;
	private AreaGrid grid;
	private Thread process;
	private BattleInstance battleToDo;
	/**chance of battle happening out of 100%. 
	 * Decimal precision is 3 (x.x% or 0.xxx);
	 */
	private static double battleChance = 0;

	/**
	 * Map constructor. <b>This class can only be called after a game object
	 * has been constructed</b>
	 */
	public Map(){
		grid = new AreaGrid(UMD_SW, UMD_NE, AREAS_ACROSS, AREAS_DOWN);
		createRandomEvents(2);
		process = new Thread(){
			Player player = BlobPS.getInstance().getPlayer(); 

			@Override
			public void run() {
				player.refreshLocation();

				Area curr = grid.getAreaWithPoint(player.getLatLng());
				if(curr.getType() == Area.KNOWN){
					if(timeToBattle()){
						EnemyBlob enemy = getEnemyBasedOnAreaRank(curr.getRank());
						battleToDo = BlobPS.getInstance().getGame().battle(player, enemy);
						//at this point the battle must be handled outside of the class
						try {
							wait();
							//remember to resume the class after the battle is finished.
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						try {
							sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} else if (curr.getType() == Area.UNKNOWN){
					int x = curr.x;
					int y = curr.y;
					grid.setArea(x, y, curr.toKnownArea());
				} else if (curr.getType() == Area.EVENT){
					EventArea evarea = (EventArea) curr;
					evarea.rewardPlayer(player); //does nothing if player has been here
				} else {
					try {
						throw new Exception("Area is of type UNSET, this should not be "
								+ "possible, area type changed to UNKNOWN");
					} catch (Exception e) {
						e.printStackTrace();
						curr.setType(Area.UNKNOWN);
					}
				}
			}

		};
	}

	/**
	 * Runs the map.
	 * <b>Do not call this unless the player's location has been set.</b>
	 */
	public void run(){
		process.start();
	}

	/**
	 * Interrupts the map's process, stopping it.
	 */
	public void stop(){
		process.interrupt();
	}
	
	/**
	 * Resumes the map's processes. 
	 */
	public void resume(){
		process.notify();
	}
	
	
	/**
	 * Returns true if battleToDo is not null. If it is not, then the battle
	 * should be completed by calling getWaitingBattle(). You must call
	 * resume() after the battle has ended, or if it was not selected.
	 * @return true if there is a battle waiting to happen, false otherwise.
	 */
	public boolean waitingBattleExists(){
		return battleToDo != null;
	}
	
	/**
	 * Gets the waiting battle, if there is one. 
	 * <br/><b>IMPORTANT: You MUST call resume() after the battle has been
	 * finished, regardless if the player ran or fought.</b> Otherwise, the
	 * map will not be actively processing anything.
	 * @return the waiting battle, otherwise will return null.
	 */
	public BattleInstance getWaitingBattle(){
		BattleInstance bi = battleToDo;
		battleToDo = null;
		return bi;
	}

	/**
	 * Creates a random number num of events and places them randomly on the map.
	 * There is no guarantee that this will not override random events.
	 * @param num - cannot be higher than the total number of areas.
	 */
	public void createRandomEvents(int num){
		int numAreas = AREAS_ACROSS * AREAS_DOWN;
		if(num > (numAreas)){
			throw new IllegalArgumentException("Cannot make "+num+" event areas "
					+ "because there are only "+numAreas+" areas.");
		}
		
		String desc = "Random event! Here are some goodies you'll get if you"
				+ " visit this area.";
	
		Random r = new Random();
		while(num > 0){
			int x = r.nextInt(AREAS_ACROSS);
			int y = r.nextInt(AREAS_DOWN);
		
			EventArea area = (EventArea) grid.setArea(x, y,
					grid.getArea(x, y).toEventArea(desc));
			
			int maxReturn = 3;
			for(EnemyBlob b : BlobPS.getInstance().getGame().getAllBlobs().values()){
				if(maxReturn < 1){
					break;
				}
				if(r.nextInt(8) == 4){
					area.addReward(b, 1);
					maxReturn--;
				}
			}
			
			maxReturn = 3;
			for(Item i : BlobPS.getInstance().getGame().getAllItems().values()){
				if(maxReturn < 1){
					break;
				}
				if(r.nextInt(3) == 1){
					area.addReward(i, r.nextInt(1)+1);
					maxReturn--;
				}
			}
			
			//in case above did not give items or blobs
			if(area.getBlobRewardList().isEmpty()
					&& area.getItemRewardList().isEmpty()){
				Item item = BlobPS.getInstance().getGame().getAllItems().get("Cookie");
				area.addReward(item, r.nextInt(2)+1);
			}
			
			num--;
		}
	}

	/**
	 * Determines whether a battle will happen or not.
	 * @param area
	 * @param player
	 * @return true if time to battle, false if not
	 */
	private boolean timeToBattle(){
		Random r = new Random();
		int cap = 1000;
		int battleChancePoints = (int)(battleChance * cap);
		int chancePoint = r.nextInt(cap+1);
		if(chancePoint <= battleChancePoints){
			battleChance = 0;
			return true;
		} else {
			double inc = ((double)r.nextInt(100)+1)*0.01;
			battleChance += inc;
			return false;
		}
	}

	/**
	 * Gets an enemy based on the Area rank.
	 * @param rank
	 * @return an enemy blob
	 */
	private EnemyBlob getEnemyBasedOnAreaRank(String rank){
		TreeMap<String, EnemyBlob> blobs = BlobPS.getInstance().getGame().getAllBlobs();
		EnemyBlob enemy = null;
		Random r = new Random();
		switch(rank){
		case "EVENT":
		case "A":
			for(EnemyBlob b : blobs.values()){
				if(r.nextInt(3) == 1){ //prevent from choosing first
					if(b.getRarity() < 1){
						return b;
					}
				}
			}
			if(enemy == null){
				for(EnemyBlob b : blobs.values()){ //get first
					if(b.getRarity() < 1){
						return b;
					}
				}
			}
			if(enemy == null){
				for(EnemyBlob b : blobs.values()){ //get first
					if(b.getRarity() < 3){
						return b;
					}
				}
			}
			if(enemy == null){ // if somehow STILL null
				return blobs.firstEntry().getValue();
			}
		case "B":
			for(EnemyBlob b : blobs.values()){
				if(r.nextInt(3) == 1){ //prevent from choosing first
					if(b.getRarity() > 1 && b.getRarity() < 2){
						return b;
					}
				}
			}
			if(enemy == null){
				for(EnemyBlob b : blobs.values()){ //get first
					if(b.getRarity() > 1 && b.getRarity() < 2){
						return b;
					}
				}
			}
			if(enemy == null){
				for(EnemyBlob b : blobs.values()){ //get first
					if(b.getRarity() > 1){
						return b;
					}
				}
			}
			if(enemy == null){ // if somehow STILL null
				return blobs.firstEntry().getValue();
			}
		case "C":
			for(EnemyBlob b : blobs.values()){
				if(r.nextInt(3) == 1){ //prevent from choosing first
					if(b.getRarity() > 2 && b.getRarity() < 3){
						return b;
					}
				}
			}
			if(enemy == null){
				for(EnemyBlob b : blobs.values()){ //get first
					if(b.getRarity() > 2 && b.getRarity() < 3){
						return b;
					}
				}
			}
			if(enemy == null){
				for(EnemyBlob b : blobs.values()){ //get first
					if(b.getRarity() > 1){
						return b;
					}
				}
			}
			if(enemy == null){ // if somehow STILL null
				return blobs.firstEntry().getValue();
			}
		case "D":
			for(EnemyBlob b : blobs.values()){
				if(r.nextInt(3) == 1){ //prevent from choosing first
					if(b.getRarity() > 3 && b.getRarity() < 4){
						return b;
					}
				}
			}
			if(enemy == null){
				for(EnemyBlob b : blobs.values()){ //get first
					if(b.getRarity() > 3 && b.getRarity() < 4){
						return b;
					}
				}
			}
			if(enemy == null){
				for(EnemyBlob b : blobs.values()){ //get first
					if(b.getRarity() > 2){
						return b;
					}
				}
			}
			if(enemy == null){ // if somehow STILL null
				return blobs.firstEntry().getValue();
			}
		case "E":
			for(EnemyBlob b : blobs.values()){
				if(r.nextInt(3) == 1){ //prevent from choosing first
					if(b.getRarity() > 4){
						return b;
					}
				}
			}
			if(enemy == null){
				for(EnemyBlob b : blobs.values()){ //get first
					if(b.getRarity() > 4){
						return b;
					}
				}
			}
			if(enemy == null){
				for(EnemyBlob b : blobs.values()){ //get first
					if(b.getRarity() > 2){
						return b;
					}
				}
			}
			if(enemy == null){ // if somehow STILL null
				return blobs.firstEntry().getValue();
			}
		default:
			throw new IllegalArgumentException("Unable to determne battle due to "
					+ "unknown rank passed in of "+rank);
		}
	}
	
	
	private final GoogleMap getGoogleMap(){
		return MapsActivity.getInstance().getGoogleMap();
	}

	//for other back-end classes.
	public Location getCurrentLocation(){
		return getGoogleMap().getMyLocation();
	}

	public AreaGrid getGrid() {
		return grid;
	}

}
