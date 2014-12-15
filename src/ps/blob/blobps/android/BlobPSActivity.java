package ps.blob.blobps.android;

import ps.blob.blobps.BlobPS;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BlobPSActivity extends Activity {

    private final BlobPS blobPS = BlobPS.getInstance();

    protected BlobPS bps() { return blobPS; }
}