package ps.blob.blobps.android;

import ps.blob.blobps.R;
import ps.blob.blobps.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BattleActivity extends Activity {

	private String TAG = "BattleActivity";

	ProgressBar myHP, mySP, enemyHP, enemySP;
	Button blobButton, itemsButton, runButton;
	ImageView enemyBlob;
	private GestureDetectorCompat mDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_battle);

		// set up gestures
		mDetector = new GestureDetectorCompat(this, new MyGestureListener());

		// cache widgets
		myHP = (ProgressBar) findViewById(R.id.personal_blob_hp);
		mySP = (ProgressBar) findViewById(R.id.personal_blob_sp);
		enemyHP = (ProgressBar) findViewById(R.id.enemy_blob_hp);
		enemySP = (ProgressBar) findViewById(R.id.enemy_blob_sp);
		blobButton = (Button) findViewById(R.id.show_blobs_button);
		itemsButton = (Button) findViewById(R.id.show_items_button);
		runButton = (Button) findViewById(R.id.run_away_button);
		enemyBlob = (ImageView) findViewById(R.id.enemy_blob);

		setBattleGestures();
		setButtonListeners();

	} // end of onCreate

	private void setButtonListeners() {

		final String DEBUG_TAG = "ButtonListener";
		
		// Listener for Blob Button
		blobButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO - Pull up Blobs List Fragment
				Log.d(DEBUG_TAG, "Clicked Blobs Button");

			}
		});

		// Listener for Items Button
		itemsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO - Pull up Items List Fragment
				Log.d(DEBUG_TAG, "Clicked Items Button");
				
			}
		});

		// Listener for Run Button
		runButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO - Run away from battle
				Log.d(DEBUG_TAG, "Clicked Run Button");
			}
		});
	}

	private void setBattleGestures() {
		enemyBlob.setOnTouchListener(new OnTouchListener() {
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
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
				float velocityY) {
			Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
			Toast.makeText(getApplicationContext(), "Swiped on Enemy Blob", Toast.LENGTH_SHORT)
					.show();
			// TODO - Perform special attack here
			return true;
		}
	}
}
