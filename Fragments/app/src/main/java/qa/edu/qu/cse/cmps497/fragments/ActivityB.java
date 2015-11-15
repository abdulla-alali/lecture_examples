package qa.edu.qu.cse.cmps497.fragments;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityB extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = getIntent().getExtras().getInt("color");

        setContentView(R.layout.activity_b);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we don't need this activity
            // this will never be executed if you setup your resource folders correctly
            // as flipping the phone to landscape should automatically load the correct
            // new layout
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in FragmentTwo
            FragmentTwo fragmentTwo = FragmentTwo.newInstance(color);
            getFragmentManager().beginTransaction().add(R.id.frame_layout, fragmentTwo).commit();
        }

    }

}
