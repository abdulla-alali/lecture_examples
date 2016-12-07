package qa.edu.qu.cse.cmps497.location;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private static final int MY_REQUEST_CODE = 1;
    private boolean mIsGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //we don't have permission :~( let's request it
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_CODE);
            mIsGranted = false;

        } else {
            mIsGranted = true;
        }
    }

    public void buttonClicked(View view) {
        if (mIsGranted) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (requestCode == MY_REQUEST_CODE && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Great ! Do nothing
            mIsGranted = true;
        } else {

            mIsGranted = false;
            Toast.makeText(this, R.string.must_accept, Toast.LENGTH_LONG).show();
        }
    }
}
