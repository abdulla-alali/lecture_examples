package qa.edu.qu.cse.cmps497.explicitintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ActivityTwo extends AppCompatActivity {

    final static String TAG = "ExplicitIntent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        String passedValue = getIntent().getExtras().getString("VALUE");
        Log.i(TAG, passedValue);

    }

    public void finishClicked(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        intent.putExtra("COOKIE", "User selected the red cookie!");
        finish();
    }
}
