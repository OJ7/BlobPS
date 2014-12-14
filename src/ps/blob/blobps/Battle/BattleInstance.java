package ps.blob.blobps.Battle;

import ps.blob.blobps.Item;
import ps.blob.blobps.Player;
import ps.blob.blobps.Blob.EnemyBlob;
import ps.blob.blobps.Blob.PersonalBlob;

/**
 * A representation of a single battle. This class must
 * be directly manipulated in order for a battle to progress,
 * otherwise battles would be 100% automatic and out of the
 * control of the player.
 * @author Chijioke
 *
 */
public class BattleInstance {
	private Player player;
	private PersonalBlob playersCurrent;
	private EnemyBlob enemy;
	/** Who */
	private final static int PLAYER = 0, ENEMY = 1;
	/** Attack type */
	public final static int NORMAL = 100, SPECIAL = 200;
	private int next;
	private int current;
	private int winner;
	
	/**
	 * Creates a new battle instance.
	 * @param player
	 * @param enemy
	 */
	public BattleInstance(Player player, EnemyBlob enemy){
		this.player = player;
		this.playersCurrent = player.getFirstBlob();
		this.enemy = enemy;
		next = ENEMY;
		current = PLAYER;
	}
	
	/**
	 * Conducts a turn, with the current blob, which is not necessarily
	 * the player. Turns auto switch between player and enemy. Returns true if 
	 * the turn completed, false if the enemy is dead, the player's blob is dead,
	 * or all the player's blobs are dead. 
	 * @param actionType - ATTACK_NORMAL, ATTACK_SPECIAL, ITEM
	 * @return true if turn was completed, false if current or
	 * next (the player's blob or the enemy) are dead.
	 */
	public boolean conductTurn(int attackType){
		if(battleIsOver() || playersCurrentIsDead()){
			return false;
		}
		if(attackType == NORMAL){
			if(current == PLAYER){
				playersCurrent.attack(enemy);
			} else {
				enemy.attack(playersCurrent);
			}
			next();
			return true;
		}
		if(attackType == SPECIAL){
			if(current == PLAYER){
				playersCurrent.useSpecial(enemy);
			} else {
				enemy.useSpecial(playersCurrent);
			}
			next();
			return true;
		}
		/*should not get here*/
		throw new IllegalArgumentException("conductTurn can only take in "
				+ "NORMAL and SPECIAL values");
	}
	
	/**
	 * Changes the player's current blob to the one passed in.
	 * @param blob
	 */
	public void changePlayersBlob(PersonalBlob blob){
		playersCurrent = blob;
	}
	
	/**
	 * Uses player's selected item on the current blob. Note that using an item
	 * ends a player's turn.
	 * @param blob
	 */
	public void useItem(Item item){
		//only item to use for now is heal
		player.useHealItem(item, playersCurrent);
		next();
	}
	
	/**
	 * Player attempts to capture blob with item. If the blob is captured the battle
	 * should end after that.
	 * @param item
	 * @return true if blob was captured and added to player's blobs. 
	 * false if the capture failed.
	 */
	public boolean attemptCapture(Item item){
		return player.useCaptureItem(item, enemy);
	}
	
	/**
	 * Returns who is next and sets current and next appropriately.
	 * @return true next
	 */
	private int next(){
		int old = next;
		if(old == PLAYER){
			next = ENEMY;
			return old;
		} else {
			next = PLAYER;
			return old;
		}
	}
	
	/**
	 * @return true if enemy is dead, false otherwise
	 */
	public boolean enemyIsDead(){
		return enemy.isDead();
	}
	
	/**
	 * @return true if player's current is dead, false otherwise
	 */
	public boolean playersCurrentIsDead(){
		return playersCurrent.isDead();
	}
	
	/**
	 * True if all of the player's blobs are dead. In this case
	 * the player loses and the battle ends.
	 * @return true if all of the player's blobs are dead, false otherwise
	 */
	public boolean playersRosterAllDead(){
		return player.allDead();
	}
	
	/**
	 * Returns whether the next blob is dead. It would be wise to, if the 
	 * player's current is dead, to check that not all the player's blobs are 
	 * dead. If not, then they need to change blobs.
	 * @return true if the blob who's turn is next is alive, false otherwise
	 */
	public boolean nextIsAlive(){
		if(next == PLAYER){
			return !playersCurrent.isDead();
		} else {
			return !enemy.isDead();
		}
	}
	
	/**
	 * Returns true if all the player's blobs are dead or the enemy blob is dead
	 * and then sets the appropriate winner, since ties are not possible in a turn
	 * based game. 
	 * @return true if all player's blobs are dead, or enemy is dead, false otherwise.
	 */
	public boolean battleIsOver(){
		if(enemy.isDead()){
			winner = PLAYER;
			return true;
		} else if (player.allDead()){
			winner = ENEMY;
			return true;
		}
		return false;
	}
	
	/**
	 * @return the winner of the battle
	 */
	public final int getWinner(){
		return winner;
	}
	
	public boolean didPlayerWin(){
		return winner == PLAYER;
	}
	
	public boolean didEnemyWin(){
		return winner == ENEMY;
	}

	public final int getNext() {
		return next;
	}

	public final int getCurrent() {
		return current;
	}
	
	
	
}
