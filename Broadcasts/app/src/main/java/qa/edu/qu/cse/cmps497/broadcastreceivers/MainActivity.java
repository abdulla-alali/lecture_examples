package qa.edu.qu.cse.cmps497.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static String MY_ACTION = "edu.qa.MY_ACTION";
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView)findViewById(R.id.textView);
    }

    //in onResume, dynamically register our broadcast receiver (mReceiver) which accepts
    //two actions. AIRPLANE MODE CHANGE, and my custom one which is fired by a button click
    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(MY_ACTION);
        registerReceiver(mReceiver, filter);
        super.onResume();
    }

    //unregister any dynamic receivers
    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    //create the dynamic receiver and assign it to a variable
    //this variable now has access to this Activity's context (so can manipulate UI elements)
    //It can handle two actions. AIRPLANE mode changed
    //or the custom click
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()==Intent.ACTION_AIRPLANE_MODE_CHANGED)
                mTextView.setText(getResources().getString(R.string.airplane_mode) + " " + intent.getBooleanExtra("state",false));
            else if (intent.getAction()==MY_ACTION)
                Toast.makeText(MainActivity.this, "My Action", Toast.LENGTH_LONG).show();
        }
    };

    public void sendBroadcastClicked(View view) {
        sendBroadcast(new Intent(MY_ACTION));
    }
}
