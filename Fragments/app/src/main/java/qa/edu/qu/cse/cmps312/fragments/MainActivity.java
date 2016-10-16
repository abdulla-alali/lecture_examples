package qa.edu.qu.cse.cmps312.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity implements FragmentOne.OnFragmentInteractionListener {

    public static final String TAG = "Fragments";
    private int mCurSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        if (savedInstanceState!=null) { //wait, we are restoring from a rotation !
            mCurSelection = savedInstanceState.getInt("position");
            displayDetails(mCurSelection);
        }
    }

    @Override
    public void onButtonPressed(View v) {
        switch (v.getId()) {
            case R.id.button:
                displayDetails(1);
                break;
            case R.id.button2:
                displayDetails(2);
                break;
        }
    }


    //Notice how we save what we last clicked here in case we switch from Landscape to Portrait
    //and vice versa
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mCurSelection);
    }

    private void displayDetails(int i) {
        boolean dualPane = getResources().getBoolean(R.bool.two_pane);
        switch (i) {
            case 1:
                mCurSelection = 1;
                Log.i(TAG, "button 1 clicked");
                if (!dualPane) {
                    startActivity(1);
                } else {
                    startFragment(1);
                }
                break;
            case 2:
                mCurSelection = 2;
                Log.i(TAG, "button 2 clicked");
                if (!dualPane) {
                    startActivity(2);
                } else {
                    startFragment(2);
                }
                break;
        }
    }

    private void startFragment(int i) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        FragmentTwo fragment = new FragmentTwo();
        Bundle args = new Bundle();

        switch (i) {
            case 1:
                //Color is set as a Bundle and pushed using setArguments()
                args.putInt(FragmentTwo.COLOR, getResources().getColor(R.color.colorAccent));
                break;
            case 2:
                args.putInt(FragmentTwo.COLOR, getResources().getColor(R.color.colorPrimary));
                break;
        }

        //Notice the set arguments instead of putExtras !
        fragment.setArguments(args);

        ft.replace(R.id.fragment2, fragment);

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void startActivity(int index) {
        Intent myIntent = new Intent(this, ActivityB.class);
        switch (index) {
            case 1:
                myIntent.putExtra("color", getResources().getColor(R.color.colorAccent));
                startActivity(myIntent);
                break;
            case 2:
                myIntent.putExtra("color", getResources().getColor(R.color.colorPrimary));
                startActivity(myIntent);
                break;
        }
    }
}
