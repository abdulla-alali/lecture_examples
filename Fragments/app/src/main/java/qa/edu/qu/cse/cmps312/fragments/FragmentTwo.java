package qa.edu.qu.cse.cmps312.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentTwo extends Fragment {
    public static final String COLOR = "color";

    private int mColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColor = getArguments().getInt(COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView =  inflater.inflate(R.layout.fragment_two, container, false);

        // change the background color of the layout and the textViews's text
        LinearLayout linearLayout = (LinearLayout) mainView.findViewById(R.id.linear_layout);
        linearLayout.setBackgroundColor(mColor);
        TextView textView = (TextView)mainView.findViewById(R.id.textView);
        textView.setText(getResources().getString(R.string.color_is) + " " +
                String.format("#%06X", 0xFFFFFF & mColor));

        //must return the inflated layout of the fragment
        return mainView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
