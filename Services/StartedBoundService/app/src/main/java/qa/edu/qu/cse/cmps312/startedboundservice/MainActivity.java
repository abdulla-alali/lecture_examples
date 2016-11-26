package qa.edu.qu.cse.cmps312.startedboundservice;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Client";
    boolean mBounded;
    MyService mServer;
    MyService.MyBinder mMyBinder;
    ProgressDialog progressDialog;
    public static final int POST_UPDATE = 1;

    //this handler is used by service to update local progress bar
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POST_UPDATE:
                    if (progressDialog.isShowing()) {
                        progressDialog.setProgress((int) msg.obj);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(2000);
        progressDialog.setMessage("Downloading file");
        progressDialog.setCancelable(true); //so you can cancel manually
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, 0);
        Log.i(TAG, "Binding");
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "On stop unbinding");
        mBounded = false;
        unbindService(mConnection);
        Log.i(TAG, "UnBinding");
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    public void buttonClicked(View view) {
        final Intent intent = new Intent(this, MyService.class);
        switch (view.getId()) {
            case R.id.runButton:
                //start service and bind to it
                startService(intent);
                if (!mBounded) {
                    bindService(intent, mConnection, 0);
                    Log.i(TAG, "Binding");
                }
                break;
            case R.id.stopButton:
                //if we are bound, unbind and then stop the service
                //this will fire an Interrupt on the thread in the service
                if (mBounded) { mBounded = false; }
                stopService(intent);
                mServer = null;
                break;
            case R.id.statusButton:
                if (mServer!=null)
                    Toast.makeText(MainActivity.this, "Status: " + mServer.getCurrentProgress(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MyService.MyBinder)service;
            mServer = mMyBinder.getServerInstance();
            mBounded = true;
            mServer.setMessenger(new Messenger(new MyHandler()));

            if (!progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setProgress(mServer.getCurrentProgress());
            }
            Toast.makeText(MainActivity.this, "Bounded to service", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServer = null;
            mBounded = false;
            Log.d(TAG, "Unbounded from service");
            if (progressDialog.isShowing()) progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Unbounded from service", Toast.LENGTH_SHORT).show();
        }
    };
}
