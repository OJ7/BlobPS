package ps.blob.blobps.android;

import java.util.ArrayList;

import ps.blob.blobps.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CombineActivity extends FragmentActivity {

	String TAG = "CombineActivity";

	private MultiViewPager mPager;
	private FragmentStatePagerAdapter mAdapter;
	public ArrayList<BlobPagerFragment> blobs = new ArrayList<BlobPagerFragment>();

	// private ImageView leftBlob, middleBlob, rightBlob, mainBlob;
	private ImageView middleBlob, mainBlob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_combine);

		// Adding MultiViewPager for blobs list
		mPager = (MultiViewPager) findViewById(R.id.pager);

		mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return Integer.MAX_VALUE;
			}

			@Override
			public Fragment getItem(int position) {
				BlobPagerFragment blob = new BlobPagerFragment();
				blobs.add(blob);
				return blob;
			}

			public Fragment addFragment(View view, int position) {

				return null;
			}

		};
		mPager.setAdapter(mAdapter);

		// Getting Blob List View
		final LayoutInflater factory = getLayoutInflater();
		View view = factory.inflate(R.layout.fragment_combine_blob_list, null);

		// Caching all widgets
		// leftBlob = (ImageView) findViewById(R.id.left_blob);
		middleBlob = (ImageView) view.findViewById(R.id.middle_blob);
		// rightBlob = (ImageView) findViewById(R.id.right_blob);
		mainBlob = (ImageView) findViewById(R.id.current_blob);

		// Setting listeners for blobs
		// leftBlob.setOnTouchListener(new CombineOnTouchListener());
		middleBlob.setOnTouchListener(new CombineOnTouchListener());
		// rightBlob.setOnTouchListener(new CombineOnTouchListener());
		mainBlob.setOnDragListener(new CombineDragListener());

		addBlob();

	}

	private void addBlob() {

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

				if (v == findViewById(R.id.current_blob)) {
					Context context = getApplicationContext();
					Toast.makeText(context, "Implement Combine Blob!", Toast.LENGTH_LONG).show();
					// TODO - implement combine (and maybe confirmation dialog)

				} else {
					Context context = getApplicationContext();
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
