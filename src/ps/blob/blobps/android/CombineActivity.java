package ps.blob.blobps.android;

import ps.blob.blobps.R;
import ps.blob.blobps.R.id;
import ps.blob.blobps.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class CombineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_combine);
		
	}


}
