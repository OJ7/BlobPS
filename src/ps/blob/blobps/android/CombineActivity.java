package ps.blob.blobps.android;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.games.Game;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import ps.blob.blobps.BlobPS;
import ps.blob.blobps.R;
import ps.blob.blobps.R.id;
import ps.blob.blobps.R.layout;
import ps.blob.blobps.Blob.Blob;
import ps.blob.blobps.Blob.PersonalBlob;
import ps.blob.blobps.Combine.CombineInstance;
import android.R.integer;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CombineActivity extends BlobPSActivity {

	String TAG = "CombineActivity";
	private ImageView currentBlobImage, mainBlobImage;
	private ArrayList<Blob> blobList = new ArrayList<Blob>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_combine);

		CombineInstance combineInstance = new CombineInstance();

		// Caching all widgets
		currentBlobImage = (ImageView) findViewById(R.id.current_blob);
		mainBlobImage = (ImageView) findViewById(R.id.main_blob);

		// Setting listeners for blobs
		currentBlobImage.setOnTouchListener(new CombineOnTouchListener());
		mainBlobImage.setOnDragListener(new CombineDragListener());

		// Getting List of blobs
		try {
			blobList.addAll(bps().getGame().getPlayer().getBlobs().values());
		} catch (NullPointerException e) {
			// TODO: handle exception
			Log.i(TAG, "Failed to get list of player's blobs");
		}

		// TEMP: adding random blob to list
		try {
			ArrayList<Blob> tmp = BlobPS.getInstance().getGame().createCompletelyRandomBlobs(5);
			for (Blob b : tmp) {
				blobList.add(b);
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
			Log.i(TAG, "Failed to create random blobs");
		}

		
		// Setting Main Blob as first Blob in list
		Blob mainBlob = blobList.get(0);
		// TODO - once implemented, get string for image 
		mainBlobImage.setImageResource(R.drawable.ic_launcher);
		
		addBlobsToLayout();

	}
	
	private void addBlobsToLayout() {
		LinearLayout blobListLayout = (LinearLayout) findViewById(R.id.blob_list);
		int i = 0;
		for(Blob b : blobList){
			if(i++ > 0){
				ImageView imageView = new ImageView(getApplicationContext());
				
				b.getImageReference(); // TODO - add this to drawable once working
				Drawable myDrawable = getResources().getDrawable(R.drawable.purple_blob);
				imageView.setImageDrawable(myDrawable);
				
				// Setting width of image to be same as default
				/*LayoutParams params = imageView.getLayoutParams();
				params.width = 150;
				imageView.setLayoutParams(params);*/
				
				imageView.setOnTouchListener(new CombineOnTouchListener());
				//Adding image to layout
				blobListLayout.addView(imageView);
			}
		}
		
	}

	private void updateCombineBlobStats(View v) {
		// TODO - Update the Combine Blob Stats here
		currentBlobImage = (ImageView) v;

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
					
					Toast.makeText(context, "Implement Combine Blob!", Toast.LENGTH_LONG).show();
					// TODO - implement combine (and maybe confirmation dialog)

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
