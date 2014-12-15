package ps.blob.blobps.android;

import java.util.ArrayList;
import java.util.TreeMap;

import ps.blob.blobps.R;
import ps.blob.blobps.Blob.Blob;
import ps.blob.blobps.Blob.PersonalBlob;
import ps.blob.blobps.Combine.CombineInstance;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CombineActivity extends BlobPSActivity {

	String TAG = "CombineActivity";
	private ImageView mainBlobImage;
	LinearLayout blobListLayout;
	private ArrayList<Blob> blobList = new ArrayList<Blob>();
	private TreeMap<Integer, Blob> blobTreeMap = new TreeMap<Integer, Blob>();
	CombineInstance combineInstance;
	Blob mainBlob, currentBlob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_combine);

		combineInstance = new CombineInstance();

		// Caching all widgets
		mainBlobImage = (ImageView) findViewById(R.id.main_blob);
		blobListLayout = (LinearLayout) findViewById(R.id.blob_list);

		// Setting listeners for blobs
		mainBlobImage.setOnDragListener(new CombineDragListener());

		// Getting List of blobs
		try {
			blobList.addAll(bps().getGame().getPlayer().getBlobs().values());
		} catch (NullPointerException e) {
			Log.i(TAG, "Failed to get list of player's blobs");
		}

		// TEMP: adding random blob to list
		/*
		 * try { ArrayList<Blob> tmp =
		 * BlobPS.getInstance().getGame().createCompletelyRandomBlobs(5); for (Blob b : tmp) {
		 * blobList.add(b); } } catch (NullPointerException e) { Log.i(TAG,
		 * "Failed to create random blobs"); }
		 */

		// Setting Main Blob as first Blob in list
		mainBlob = blobList.get(0);
		int imageResource = getResources().getIdentifier(
				"drawable/" + mainBlob.getImageReference(), null, getPackageName());
		mainBlobImage.setImageResource(imageResource);

		addBlobsToLayout();

	}

	private void addBlobsToLayout() {

		int i = 0;
		for (Blob b : blobList) {
			if (i++ > 0) {
				ImageView imageView = new ImageView(getApplicationContext());

				int imageResource = getResources().getIdentifier(
						"drawable/" + b.getImageReference(), null, getPackageName());
				Drawable myDrawable = getResources().getDrawable(imageResource);
				imageView.setImageDrawable(myDrawable);

				imageView.setOnTouchListener(new CombineOnTouchListener());
				// Adding image to layout
				blobListLayout.addView(imageView);

				imageView.getLayoutParams().width = 250;
				imageView.setPadding(15, 0, 0, 15);

				blobTreeMap.put(imageView.getId(), b);
			}
		}
	}

	private void showCombinedBlob(PersonalBlob newBlob) {
		setContentView(R.layout.screen_combined_blob);

		TextView blobName = (TextView) findViewById(R.id.combined_blob_name);
		blobName.setText(newBlob.getPersonalName());

		LinearLayout hpSPInfo = (LinearLayout) findViewById(R.id.hp_sp_info);
		TextView hpTextView = new TextView(getApplicationContext());
		TextView spTextView = new TextView(getApplicationContext());
		hpTextView.setText("HP: " + newBlob.getHP());
		spTextView.setText("SP: " + newBlob.getSP());
		hpSPInfo.addView(hpTextView);
		hpSPInfo.addView(spTextView);

		LinearLayout atkDEFInfo = (LinearLayout) findViewById(R.id.atk_def_info);
		TextView atkTextView = new TextView(getApplicationContext());
		TextView defTextView = new TextView(getApplicationContext());
		atkTextView.setText("ATK: " + newBlob.getAtk());
		defTextView.setText("DEF: " + newBlob.getDef());
		atkDEFInfo.addView(atkTextView);
		atkDEFInfo.addView(defTextView);

		LinearLayout root = (LinearLayout) findViewById(R.id.combine_screen_root_layout);
		root.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void updateCombineBlobStats(View v) {
		currentBlob = blobTreeMap.get(v.getId());
		TextView currText = (TextView) findViewById(R.id.main_blob_info);
		TextView combText = (TextView) findViewById(R.id.comb_blob_info);

		currText.setText("HP: " + mainBlob.getHP() + "\n" + "SP: " + mainBlob.getSP() + "\n"
				+ "Atk: " + mainBlob.getAtk() + "\n" + "Def: " + mainBlob.getDef());

		@SuppressWarnings("static-access")
		PersonalBlob temp = combineInstance.doCombine((PersonalBlob) mainBlob,
				(PersonalBlob) currentBlob);

		combText.setText("HP: " + temp.getHP() + "\n" + "SP: " + temp.getSP() + "\n" + "Atk: "
				+ temp.getAtk() + "\n" + "Def: " + temp.getDef());
	}

	private final class CombineOnTouchListener implements View.OnTouchListener {

		private String TAG = "CombineOnTouchListener";

		static final int MIN_DISTANCE_Y = 40;
		private float downY, upY;

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					downY = event.getY();
					updateCombineBlobStats(view);
					return true;
				}
				case MotionEvent.ACTION_MOVE: {
					upY = event.getY();

					float deltaY = downY - upY;

					// swipe vertical?
					if (Math.abs(deltaY) > MIN_DISTANCE_Y) {
						Log.i(TAG, "swipe vertical...");
						if (deltaY < 0) { // moving down
							// Start your drag here if appropriate
							return true;
						}
						if (deltaY > 0) { // moving up
							// Or start your drag here if appropriate

							DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

							view.startDrag(null, // data to be dragged
									shadowBuilder, // drag shadow
									view, // local data about the drag and drop operation
									0 // no needed flags
							);

							return true;
						}
					}
				}
			}
			return false;
		}

	}

	class CombineDragListener implements OnDragListener {

		private String DEBUG_TAG = "Dragging";

		@Override
		public boolean onDrag(View v, DragEvent event) {

			// Handles each of the expected events
			switch (event.getAction()) {

			// signal for the start of a drag and drop operation.
				case DragEvent.ACTION_DRAG_STARTED:
					Log.i(DEBUG_TAG, "Started Drag");
					break;

				case DragEvent.ACTION_DRAG_ENTERED:
					Log.i(DEBUG_TAG, "Entered view");
					break;

				case DragEvent.ACTION_DRAG_EXITED:
					Log.i(DEBUG_TAG, "Exited view");

					break;

				case DragEvent.ACTION_DROP:
					Log.i(DEBUG_TAG, "Dropped in view");
					Context context = getApplicationContext();
					if (v == findViewById(R.id.main_blob)) {
						PersonalBlob newBlob = combineInstance.doCombine((PersonalBlob) mainBlob,
								(PersonalBlob) currentBlob);
						Log.i(DEBUG_TAG, "Combined Blob! .... " + newBlob.getPersonalName());
						Toast.makeText(context, "Blob Combined: " + newBlob.getPersonalName(),
								Toast.LENGTH_LONG).show();
						showCombinedBlob(newBlob);

					} else {
						Toast.makeText(context, "Drag to main blob to combine!", Toast.LENGTH_LONG)
								.show();

						break;
					}
					break;

				case DragEvent.ACTION_DRAG_ENDED:
					Log.i(DEBUG_TAG, "Ended drag");
					break;

				default:
					break;
			}
			return true;
		}
	}

}
