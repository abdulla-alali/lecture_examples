package qa.edu.qu.cse.cmps497.services.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

public class LocalLoggingService extends IntentService {

    public static String EXTRA_LOG = "qa.edu.qu.cse.cmps497.services.MESSAGE";
    private static final String TAG = "LoggingService";

    public LocalLoggingService() {
        super("LocalLoggingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, intent.getStringExtra(EXTRA_LOG));

    }

}
