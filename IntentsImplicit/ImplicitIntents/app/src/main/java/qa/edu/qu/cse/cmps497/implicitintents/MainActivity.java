package qa.edu.qu.cse.cmps497.implicitintents;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    final static String TAG = "MainActivity";
    final static int MY_REQUEST_CODE = 43;
    final static int MY_REQUEST_CODE2 = 45;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void callClicked(View view) {
        mEditText = (EditText)findViewById(R.id.editText);
        String number = mEditText.getText().toString();


        //Implicit intent
        //we are setting the action as ACTION_CALL, and data is the URI
        //of the phone number
        //Note: this Action requires a special permission. What happens if you don't have it?

        //see if we have the permission (For Marshmallow and above)
        //pay attention that CALL_PHONE is a dangerous permission,
        //therefore we need to explicitly check.
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);

        if (permissionCheck== PackageManager.PERMISSION_GRANTED) {
            //call the number if we are GRANTED the permission
            callNumber(number);
        } else {

            //we don't have permission :~( let's request it
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CALL_PHONE}, MY_REQUEST_CODE);

        }


    }

    private void callNumber(String number) {
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


        int permissionCheck = ContextCompat.checkSelfPermission(this,
                "qa.edu.qu.cse.cmps497.implicitintentslave.DANGEROUS");

        if (permissionCheck== PackageManager.PERMISSION_GRANTED) {
            //execute dangerous activity in the other slave app
            launchDangerousActivity();

        } else {

            Log.d(TAG, "Not granted");
            //we don't have permission :~( let's request it
            ActivityCompat.requestPermissions(this,
                    new String[] {"qa.edu.qu.cse.cmps497.implicitintentslave.DANGEROUS"}, MY_REQUEST_CODE2);

        }


    }


    private void launchDangerousActivity() {

        //Launch my other dangerous app's activity using implicit intents
        Intent myIntent = new Intent();
        myIntent.setAction("qa.edu.qu.cse.cmps497.implicitintentslave.ACTION_LAUNCH");


        if (myIntent.resolveActivity(getPackageManager())!=null) {
            try {
                startActivity(myIntent);
            } catch (SecurityException e) {
                Log.e(TAG, "I do not have the permission to launch the activity");
            }
        } else {
            Log.e(TAG, "no intent is found!");
        }
    }

    /**
     * User made a choice on the dialog of permission
     * This method is automatically called by the Android framework
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_REQUEST_CODE:

                //if grantResult is empty, user canceled
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //user accepted permission. Let's call
                    callNumber(mEditText.getText().toString());

                } else {

                    //the user refused to grant us permission.
                    //either request again, or give up
                    Log.i(TAG, "I don't have permission to call");
                    callClicked(null);

                }

                break;
            case MY_REQUEST_CODE2:

                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //user accepted permission. Let's execute the dangerous activity
                    launchDangerousActivity();

                } else {

                    //the user refused to grant us permission.
                    //either request again, or give up
                    Log.i(TAG, "I don't have permission to call");
                    dangerousLaunchClicked(null);

                }

                break;

        }
    }
}
