package qa.edu.qu.cse.cmps497.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import qa.edu.qu.cse.cmps497.services.aidl.KeyGenerator;

public class AIDLBoundActivity extends AppCompatActivity {

    protected static final String TAG = "KeyServiceUser";
    private KeyGenerator mKeyGeneratorService;
    private boolean mIsBound;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_aidl_bound);

        final TextView output = (TextView) findViewById(R.id.output);

        final Button goButton = (Button) findViewById(R.id.go_button);
        goButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                try {

                    // Call KeyGenerator and get a new ID
                    if (mIsBound)
                        output.setText(mKeyGeneratorService.getKey());

                } catch (RemoteException e) {

                    Log.e(TAG, e.toString());

                }
            }
        });
    }

    // Bind to KeyGenerator Service
    @Override
    protected void onResume() {
        super.onResume();

        if (!mIsBound) {

            Intent intent = new Intent("qa.edu.qu.cse.cmps497.servicesremote.aidl_service_action");
            intent.setPackage("qa.edu.qu.cse.cmps497.servicesremote");
            bindService(intent, this.mConnection, Context.BIND_AUTO_CREATE);

        }
    }

    // Unbind from KeyGenerator Service
    @Override
    protected void onPause() {

        if (mIsBound) {

            unbindService(this.mConnection);

        }

        super.onPause();
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iservice) {

            mKeyGeneratorService = KeyGenerator.Stub.asInterface(iservice);

            mIsBound = true;

        }

        public void onServiceDisconnected(ComponentName className) {

            mKeyGeneratorService = null;

            mIsBound = false;

        }
    };
}
