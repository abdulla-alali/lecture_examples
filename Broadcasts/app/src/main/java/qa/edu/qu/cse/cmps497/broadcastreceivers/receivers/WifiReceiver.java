package qa.edu.qu.cse.cmps497.broadcastreceivers.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Example of statically registered receiver
 * It will just throw a Toast when Wifi status changes
 * See AndroidManifest.xml for the action and intent-filter
 */
public class WifiReceiver extends BroadcastReceiver {
    public WifiReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);

        Log.i("TAG", "is connected? " + isConnected);

        Toast.makeText(context, "Is Wifi connected? : " + isConnected, Toast.LENGTH_SHORT).show();

    }
}
