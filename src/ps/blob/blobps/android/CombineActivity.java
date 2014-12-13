package ps.blob.blobps.android;

import ps.blob.blobps.R;
import ps.blob.blobps.R.id;
import ps.blob.blobps.R.layout;
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
import android.view.Window;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CombineActivity extends Activity {

	private ImageView leftBlob, middleBlob, rightBlob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_combine);

		// Caching all widgets
		leftBlob = (ImageView) findViewById(R.id.left_blob);
		middleBlob = (ImageView) findViewById(R.id.middle_blob);
		rightBlob = (ImageView) findViewById(R.id.right_blob);

		// Setting listeners for blobs
		leftBlob.setOnTouchListener(new CombineOnTouchListener());
		middleBlob.setOnTouchListener(new CombineOnTouchListener());
		rightBlob.setOnTouchListener(new CombineOnTouchListener());

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
		Drawable normalShape = getResources().getDrawable(R.drawable.ic_action_star);
		Drawable targetShape = getResources().getDrawable(R.drawable.ic_launcher);

		@Override
		public boolean onDrag(View v, DragEvent event) {

			// Handles each of the expected events
			switch (event.getAction()) {

			// signal for the start of a drag and drop operation.
				case DragEvent.ACTION_DRAG_STARTED:
					// do nothing
					break;

				// the drag point has entered the bounding box of the View
				case DragEvent.ACTION_DRAG_ENTERED:
					v.setBackground(targetShape); // change the shape of the view
					break;

				// the user has moved the drag shadow outside the bounding box of the View
				case DragEvent.ACTION_DRAG_EXITED:
					v.setBackground(normalShape); // change the shape of the view back to normal
					break;

				// drag shadow has been released, the drag point is within the bounding box of the
				// View
				case DragEvent.ACTION_DROP:
					// TODO - implement target area

					// NOTE: Below code doesn't work properly
					// if the view is the big blob, we accept the drag item
					if (v == findViewById(R.id.current_blob_layout)) {
						View view = (View) event.getLocalState();
						ViewGroup viewgroup = (ViewGroup) view.getParent();
						// viewgroup.removeView(view);

						LinearLayout containView = (LinearLayout) v;
						// containView.addView(view);

						Context context = getApplicationContext();
						Toast.makeText(context, "Implement Combine Blob!", Toast.LENGTH_LONG)
								.show();

						view.setVisibility(View.VISIBLE);
					} else {
						View view = (View) event.getLocalState();
						view.setVisibility(View.VISIBLE);
						Context context = getApplicationContext();
						Toast.makeText(context, "Drag to big blob to combine!", Toast.LENGTH_LONG)
								.show();
						break;
					}
					break;

				// the drag and drop operation has concluded.
				case DragEvent.ACTION_DRAG_ENDED:
					v.setBackground(normalShape); // go back to normal shape

				default:
					break;
			}
			return true;
		}
	}

}
