package qa.edu.qu.cse.cmps312.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;

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
            FragmentTwo fragment = new FragmentTwo();
            Bundle args = new Bundle();
            args.putInt(FragmentTwo.COLOR, color);
            //Notice the set arguments instead of putExtras !
            fragment.setArguments(args);

            getFragmentManager().beginTransaction().add(R.id.frame_layout, fragment).commit();
        }

    }

}
