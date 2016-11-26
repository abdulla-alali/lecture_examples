package qa.edu.qu.cse.cmps312.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MessengerBoundActivity extends AppCompatActivity {

    private final static String MESSAGE_KEY = "qa.edu.qu.cse.cmps312.servicesremotemessenger.MESSAGE";
    private final static int LOG_OP = 1;

    private final static String TAG = "LoggingServiceClient";

    // Intent used for binding to LoggingService
    private Intent mLoggingServiceIntent;

    private Messenger mMessengerToLoggingService;
    private boolean mIsBound;

    // Object implementing Service Connection callbacks
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {

            Log.d(TAG, "We are bound successfully");
            // Messenger object connected to the LoggingService
            mMessengerToLoggingService = new Messenger(service);

            mIsBound = true;

        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "We are unbound successfully");
            mMessengerToLoggingService = null;

            mIsBound = false;

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messenger_bound);

        mLoggingServiceIntent = new Intent(
                "qa.edu.qu.cse.cmps312.servicesremote.messenger_service_action");

        mLoggingServiceIntent.setPackage("qa.edu.qu.cse.cmps312.servicesremote");

        final Button buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (mIsBound) {

                    // Send Message to the Logging Service
                    logMessageToService();

                }

            }
        });
    }

    // Create a Message and sent it to the LoggingService
    // via the mMessenger Object

    private void logMessageToService() {

        // Create Message
        Message msg = Message.obtain(null, LOG_OP);
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE_KEY, "Log This Message");
        msg.setData(bundle);

        try {

            // Send Message to LoggingService using Messenger
            mMessengerToLoggingService.send(msg);

        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
    }

    // Bind to LoggingService
    @Override
    protected void onResume() {
        super.onResume();

        bindService(mLoggingServiceIntent, mConnection,
                Context.BIND_AUTO_CREATE);

    }

    // Unbind from the LoggingService
    @Override
    protected void onPause() {

        if (mIsBound) {
            unbindService(mConnection);
        }

        super.onPause();
    }
}
