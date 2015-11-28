package qa.edu.qu.cse.cmps497.location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.locationmanagerbutton:
                startActivity(new Intent(this, LocationManagerActivity.class));
                break;
            case R.id.playservicesbutton:
                startActivity(new Intent(this, LocationServicesActivity.class));
                break;
        }
    }
}
