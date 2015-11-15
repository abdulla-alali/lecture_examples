package qa.edu.qu.cse.cmps497.explicitintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ActivityOne extends AppCompatActivity {

    final static int REQUEST_CODE = 53;
    final static String TAG = "ExplicitIntent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
    }

    public void launchActivityClicked(View view) {
        //explicit intent to launch activitytwo implementation
        Intent myIntent = new Intent(this, ActivityTwo.class);
        myIntent.putExtra("VALUE", "THIS IS THE VALUE");
        startActivityForResult(myIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Log.i(TAG, "Result is ok");

            switch(requestCode) {
                case REQUEST_CODE:
                    String returned = data.getExtras().getString("COOKIE");
                    Log.i(TAG, returned);
                    TextView textView = (TextView)findViewById(R.id.textView);
                    textView.setText(returned);
                    break;
            }


        } else {
            Log.i(TAG, "Result is not ok");
        }

        super.onActivityResult(requestCode, resultCode, data);


    }

}
