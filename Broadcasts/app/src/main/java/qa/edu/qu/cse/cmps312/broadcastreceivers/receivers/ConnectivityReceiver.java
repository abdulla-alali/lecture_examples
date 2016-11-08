package qa.edu.qu.cse.cmps312.broadcastreceivers.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Example of statically registered receiver
 * It will just throw a Toast when 3G/4G data status changes
 * See AndroidManifest.xml for the action and intent-filter
 */
public class ConnectivityReceiver extends BroadcastReceiver {
    public ConnectivityReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isDisconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        Log.i("TAG", "is Disconnected? " + isDisconnected);

        Toast.makeText(context, "Is 3G/4G connected? : " + !isDisconnected, Toast.LENGTH_SHORT).show();

    }
}
