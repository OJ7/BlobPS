package ps.blob.blobps.android;

import java.util.HashMap;

import com.google.android.gms.internal.bp;

import ps.blob.blobps.BlobPS;
import ps.blob.blobps.Game;
import ps.blob.blobps.Item;
import ps.blob.blobps.R;
import ps.blob.blobps.Battle.BattleInstance;
import ps.blob.blobps.Blob.EnemyBlob;
import ps.blob.blobps.Combine.CombineInstance;
import ps.blob.blobps.R.layout;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BattleActivity extends BlobPSActivity {

	private String TAG = "BattleActivity";

	ProgressBar myHP, mySP, enemyHP, enemySP;
	ImageView blobButton, itemsButton, runButton;
	ImageView personalBlobImage, enemyBlobImage;
	TextView personalBlobHP, enemyBlobHP;
	private GestureDetectorCompat mDetector;
	BattleInstance battleInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_battle);

		EnemyBlob enemy = new EnemyBlob("Evil Blob", 1, 1, 1, 1, 1, "peter_blob",
				new HashMap<Double, Item>(), bps().getGame().randomSpecial());
		battleInstance = new BattleInstance(bps().getPlayer(), enemy);

		// set up gestures
		Log.i(TAG, "Setting up Battle Gestures");
		mDetector = new GestureDetectorCompat(this, new MyGestureListener());

		// cache widgets
		myHP = (ProgressBar) findViewById(R.id.personal_blob_hp_bar);
		mySP = (ProgressBar) findViewById(R.id.personal_blob_sp);
		enemyHP = (ProgressBar) findViewById(R.id.enemy_blob_hp);
		enemySP = (ProgressBar) findViewById(R.id.enemy_blob_sp);
		blobButton = (ImageView) findViewById(R.id.show_blobs_button);
		itemsButton = (ImageView) findViewById(R.id.show_items_button);
		runButton = (ImageView) findViewById(R.id.run_away_button);
		personalBlobImage = (ImageView) findViewById(R.id.personal_blob);
		enemyBlobImage = (ImageView) findViewById(R.id.enemy_blob);
		personalBlobHP = (TextView) findViewById(R.id.personal_blob_hp_number);
		enemyBlobHP = (TextView) findViewById(R.id.enemy_blob_hp_number);

		setBattleGestures();
		setButtonListeners();
		setupBattleField();

	} // end of onCreate

	private void setupBattleField() {
		/*int imageResource = getResources().getIdentifier(
				"drawable/" + battleInstance.getPersonalBlob().getImageReference(), null,
				getPackageName());
		Drawable myDrawable = getResources().getDrawable(imageResource);
		personalBlobImage.setImageDrawable(myDrawable);
*/
		int imageResource = getResources().getIdentifier(
				"drawable/" + battleInstance.getEnemyBlob().getImageReference(), null,
				getPackageName());
		Drawable myDrawable = getResources().getDrawable(imageResource);
		enemyBlobImage.setImageDrawable(myDrawable);

		updateBlobInfo();
	}

	private void setupBlobList(View v) {
		View popupView = getLayoutInflater().inflate(R.layout.fragment_blob_party, null);
		PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		LinearLayout blobListLayout = (LinearLayout) popupView.findViewById(R.id.blob_list_holder);
		LinearLayout row = new LinearLayout(popupView.getContext());

		ImageView tmp = new ImageView(popupView.getContext());
		Drawable myDrawable = getResources().getDrawable(R.drawable.purple_blob);
		tmp.setImageDrawable(myDrawable);
		row.addView(tmp);
		blobListLayout.addView(row);

		row = new LinearLayout(popupView.getContext());
		tmp = new ImageView(popupView.getContext());
		myDrawable = getResources().getDrawable(R.drawable.green_blob);
		tmp.setImageDrawable(myDrawable);
		row.addView(tmp);
		blobListLayout.addView(row);

		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable());

		// If the PopupWindow should be focusable
		popupWindow.setFocusable(true);

		// If you need the PopupWindow to dismiss when when touched outside
		popupWindow.setBackgroundDrawable(new ColorDrawable());

		int location[] = new int[2];

		// Get the View's(the one that was clicked in the Fragment) location
		v.getLocationOnScreen(location);

		// Using location, the PopupWindow will be displayed right under anchorView
		popupWindow.showAtLocation(v, Gravity.CENTER, location[0], location[1] + v.getHeight());

	}

	private void setButtonListeners() {

		final String DEBUG_TAG = "ButtonListener";

		// Listener for Blob Button
		blobButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO - Pull up Blobs List Fragment
				Log.d(DEBUG_TAG, "Clicked Blobs Button");
				// setupBlobList(v);
			}
		});

		// Listener for Items Button
		itemsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO - Pull up Items List Fragment
				Log.d(DEBUG_TAG, "Clicked Items Button");
				battleInstance.attemptCapture(bps().getGame().getAllItems().get("Sponge"));
				// Blob Captured
				Toast.makeText(getApplicationContext(), "Blob Captured!", Toast.LENGTH_SHORT)
						.show();
				finish();

			}
		});

		// Listener for Run Button
		runButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO - Run away from battle
				Log.d(DEBUG_TAG, "Clicked Run Button");
				Toast.makeText(getApplicationContext(), "Ran away safely!", Toast.LENGTH_SHORT)
						.show();
				finish(); // change to finishActivity once done to get result of battle
			}
		});
	}

	private void setBattleGestures() {
		enemyBlobImage.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				mDetector.onTouchEvent(event);
				return true;
			}
		});

	}

	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		private static final String DEBUG_TAG = "Gestures";

		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
			Toast.makeText(getApplicationContext(), "Tapped on Enemy Blob", Toast.LENGTH_SHORT)
					.show();
			// TODO - Perform normal attack here
			if (!battleInstance.battleIsOver()) { // Player turn
				battleInstance.conductTurn(battleInstance.NORMAL);
				updateBlobInfo();
			}
			if (!battleInstance.battleIsOver()) { // Enemy turn
				battleInstance.conductTurn(battleInstance.NORMAL);
				updateBlobInfo();
			} else {
				endBattle();

			}
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
				float velocityY) {
			Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
			Toast.makeText(getApplicationContext(), "Swiped on Enemy Blob", Toast.LENGTH_SHORT)
					.show();
			// TODO - Perform special attack here
			if (!battleInstance.battleIsOver()) { // Player turn
				battleInstance.conductTurn(battleInstance.SPECIAL);
				updateBlobInfo();
			}
			if (!battleInstance.battleIsOver()) { // Enemy turn
				battleInstance.conductTurn(battleInstance.SPECIAL);
				updateBlobInfo();
			} else {
				endBattle();
			}
			return true;
		}
	}

	private void updateBlobInfo() {
		//personalBlobHP.setText(battleInstance.getPersonalBlob().getHP());
		//enemyBlobHP.setText(battleInstance.getEnemyBlob().getHP());
	}

	private void endBattle() {
		String winner;
		if (battleInstance.getWinner() == 0) {
			winner = "You win";
		} else {
			winner = "You lose";
		}

		Toast.makeText(getApplicationContext(), "Battle finished! " + winner, Toast.LENGTH_SHORT)
				.show();

		finish();
	}
}
