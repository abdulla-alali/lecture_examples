package qa.edu.qu.cse.cmps312.servicesremote;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class MessengerRemoteService extends Service {

    private final static String MESSAGE_KEY = "qa.edu.qu.cse.cmps312.servicesremotemessenger.MESSAGE";
    private final static int LOG_OP = 1;

    private static final String TAG = "LoggingService";

    // Messenger Object that receives Messages from connected clients
    final Messenger mMessenger = new Messenger(new IncomingMessagesHandler());

    // Handler for incoming Messages
    class IncomingMessagesHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case LOG_OP:

                    Log.i(TAG, msg.getData().getString(MESSAGE_KEY));

                    break;

                default:

                    super.handleMessage(msg);

            }
        }
    }

    // Returns the Binder for the mMessenger, which allows
    // the client to send Messages through the Messenger
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "We are bounded");
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "We are unbounded");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "We are destroyed");
        super.onDestroy();
    }

}
