package qa.edu.qu.cse.cmps497.implicitintents;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void callClicked(View view) {
        EditText editText = (EditText)findViewById(R.id.editText);
        String number = editText.getText().toString();


        //Implicit intent
        //we are setting the action as ACTION_CALL, and data is the URI
        //of the phone number
        //Note: this Action requires a special permission. What happens if you don't have it?

        Intent myIntent = new Intent();
        myIntent.setAction(Intent.ACTION_CALL);
        myIntent.setData(Uri.parse("tel:" + number));

        //make sure there is a dialer app (Intent can be resolved) before running it.
        if (myIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(myIntent);
        } else {
            Log.e(TAG, "No intent is found !");
        }

    }

    public void dangerousLaunchClicked(View view) {
        //Launch my other dangerous app's activity using implicit intents
        Intent myIntent = new Intent();
        myIntent.setAction("qa.edu.qu.cse.cmps497.implicitintentslave.ACTION_LAUNCH");
        if (myIntent.resolveActivity(getPackageManager())!=null) {
            startActivity(myIntent);
        } else {
            Log.e(TAG, "no intent is found!");
        }
    }
}
