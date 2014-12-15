package ps.blob.blobps;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import ps.blob.blobps.Map.Map;

/**
 * Master back-end class, controls most of the back-end.
 * Manages saving and loading (persistency) and android inputs
 * for battling and combining.
 * 
 * If you want to load a BlobPS game, do
 * javac BlobPS load [filename or "recent"]
 * 
 * @author Chijioke/nuplex
 *
 */
public class BlobPS {
	private static BlobPS instance;
	private Game game;
	private Game hardClearBackup = null;
	private Game generalBackup = null;
	private static int saveCounter = 1;
	
	private Map map;
	
	public static void main(String[] args){
		//may have wrong array value
		//just for loading in a game.
		if(args[2] != null && args[2].equals("load")){
			if(args[3].equals("recent")){
				new BlobPS("", true);
			} else if (args[3] != null) {
				new BlobPS(args[3], false);
			} else if (args[3] == null){
				new BlobPS();
			}
		} else {
			new BlobPS();
		}
	}

	/**Creates brand new game*/
	public BlobPS(){
		instance = this;
		game = new Game();
		map = new Map(instance);
		game.getPlayer().setLocation(map.getCurrentLocation());
	}
	
	/**
	 * Loads BlobPS with a previous game.
	 * @param filename - name of file to load, if recent is false
	 * @param recent - if true, ignores filename and gets most recent file
	 */
	public BlobPS(String filename, boolean recent){
		if(recent){
			if(saveFilesExist()){
				game = loadMostRecentGame();
			} else {
				try {
					throw new FileNotFoundException("No save files present");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		} else {
			game = loadGame(filename);
		}
		map = new Map(instance);
		game.getPlayer().setLocation(map.getCurrentLocation());
		instance = this;
	}

	
	/**
	 * Saves the game locally to the file "blobps_game[saveCounter].blps"
	 */
	public void saveGame(){
		try {
			OutputStream file = 
					new FileOutputStream("blobps_game"+saveCounter+".blps");
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(game);
			output.close();
			saveCounter++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the game file passed in and returns a Game Object.
	 * If you want to replace the current game, then use the 
	 * replaceGame() method. Returns null if loading failed.
	 * <br/><br/>
	 * This class does not check if there is a save file present.
	 * @param filename
	 * @return Game if loaded successfully, null otherwise
	 */
	public Game loadGame(String filename){
		try {
			InputStream file = new FileInputStream(filename);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream (buffer);
			Game loaded = (Game) input.readObject();
			input.close();
			return loaded;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Loads the most recent game file and returns a Game Object.
	 * If you want to replace the current game, then use the 
	 * BlobPS.replaceGame() method. Returns null if loading failed.
	 * <br/><br/>
	 * This class does not check if there is a save file present.
	 * @return Game if loaded successfully, null otherwise
	 */
	public Game loadMostRecentGame(){
		int saveNum = saveCounter - 1; //last file
		InputStream file = null;
		while(file == null){
			try {
				file = new FileInputStream("blobps_game"+saveNum+".blps");
			} catch (FileNotFoundException e) {
				file = null;
			}
			if(saveNum < 1){
				try {
					throw new FileNotFoundException("No save files exist");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			/* in the odd case that the most recent file is not 
			 * saveCounter - 1 */
			saveNum--; 
		}
		//only out here if file != null
		try {
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream (buffer);
			Game loaded = (Game) input.readObject();	
			input.close();
			return loaded;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Make sure there is actually a save file present. Prevents
	 * FileNotFoundException from being thrown when loading a game. Note that
	 * this is not used in loadGame() or loadMostRecentGame() due to
	 * differences in implementation. It would ideally be used before calling
	 * those functions, if feels it should.
	 * @return true if files exist;
	 */
	public boolean saveFilesExist(){
		int saveNum = saveCounter - 1; //last file
		InputStream file = null;
		while(file == null){
			try {
				file = new FileInputStream("blobps_game"+saveNum+".blps");
			} catch (FileNotFoundException e) {
				file = null;
			}
			if(saveNum < 1){
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				return false; //no save files exist	
			}
			/* in the odd case that the most recent file is not 
			 * saveCounter - 1 */
			saveNum--; 
		}
		return true; //file exists
	}
	
	/**
	 * Clears all player data (personal blobs and items). Does not actually
	 * clear main dictionaries.<br/> This <b>does not</b> backup the game.
	 */
	public void clearGame(){
		game.clear();
	}
	
	/**
	 * Clears <b>ALL</b> game data. Resets all lists and dictionaries, as well 
	 * as player data. Backups the game in case of accidental use.<br/><br/> Note that
	 *  backups are <b>NOT</b> persistent. <b> Backups will be lost if 
	 *  application is closed. </b>
	 */
	public void hardClearGame(){
		hardClearBackup = new Game(game);
		game.hardClear();
	}
	
	/**
	 * Backs up the game temporarily. This is <b>NOT</b> persistent. <b>Backups 
	 * will be lost if application is closed.</b>
	 */
	public void backup(){
		generalBackup = new Game(game);
	}
	
	/**
	 * Gets the general backup of the game if it has been backupped at some 
	 * point. Will return null otherwise.
	 * @return the backupped game. Returns null if there is none.
	 */
	public Game retrieveBackup(){
		return generalBackup;
	}
	/**
	 * Gets the backup of the game that was the result of using hardClear().
	 * Will return if hardClearGame() was never used.
	 * @return the backupped game. Returns null if there is none.
	 */
	public Game retreiveHardClearBackup(){
		return hardClearBackup;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Map getMap() {
		return map;
	}
	
	/**
	 * Get's a BlobPS instance. Note that a BlobPS object must have been
	 * @return
	 */
	public static final BlobPS getInstance(){
		if (instance == null)
            instance = new BlobPS();
        return instance;
	}
	
	public Player getPlayer(){
		return game.getPlayer();
	}
	
	

}
