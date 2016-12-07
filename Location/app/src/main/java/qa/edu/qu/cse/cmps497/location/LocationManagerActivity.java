package qa.edu.qu.cse.cmps497.location;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class LocationManagerActivity extends AppCompatActivity {

    private static final long ONE_MIN = 1000 * 60;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long MEASURE_TIME = 1000 * 30;
    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;
    private static final float MIN_DISTANCE = 10.0f;

    //workaround for bug with LocationManager's "locations" getTime() function
    //You must find your own skew from Logcat if you want to use the emulator
    //Some emulator instances don't have that bug, so keep as 0 first
    private static final long SKEW = 0L;

    // Views for display location information
    private TextView mAccuracyView;
    private TextView mTimeView;
    private TextView mLatView;
    private TextView mLngView;

    // Current best location estimate
    private Location mBestReading;

    // Reference to the LocationManager and LocationListener
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private final String TAG = "LocationManager";

    private boolean mFirstUpdate = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_manager);

        mAccuracyView = (TextView) findViewById(R.id.accuracy_view);
        mTimeView = (TextView) findViewById(R.id.time_view);
        mLatView = (TextView) findViewById(R.id.lat_view);
        mLngView = (TextView) findViewById(R.id.lng_view);

        // Acquire reference to the LocationManager
        if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
            finish();

        // Get best last location measurement
        mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN);

        // Display last reading information
        if (null != mBestReading) {

            updateDisplay(mBestReading);

        } else {

            mAccuracyView.setText(R.string.no_init_readings);

        }

        mLocationListener = new LocationListener() {

            // Called back when location changes

            public void onLocationChanged(Location location) {

                ensureColor();

                // Determine whether new location is better than current best
                // estimate
                try {
                    Log.d(TAG, "Skew is: " + (System.currentTimeMillis() - location.getTime()));
                    Log.d(TAG, "Provider is: " + location.getProvider());
                    if (null == mBestReading
                            || location.getAccuracy() <= mBestReading.getAccuracy()) {

                        // Update best estimate
                        mBestReading = location;

                        mBestReading.setTime(mBestReading.getTime() + SKEW);

                        // Update display
                        updateDisplay(location);

                        if (mBestReading.getAccuracy() < MIN_ACCURACY)
                            mLocationManager.removeUpdates(mLocationListener);

                    }
                } catch (SecurityException e) {}
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // NA
            }

            public void onProviderEnabled(String provider) {
                // NA
            }

            public void onProviderDisabled(String provider) {
                // NA
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Determine whether initial reading is
        // "good enough". If not, register for
        // further location updates

        if (null == mBestReading
                || mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
                || mBestReading.getTime() < System.currentTimeMillis()
                - ONE_MIN) {
            Log.d(TAG, "Location needs updating");

            try {
                // Register for network location updates
                if (null != mLocationManager
                        .getProvider(LocationManager.NETWORK_PROVIDER)) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, POLLING_FREQ,
                            MIN_DISTANCE, mLocationListener);
                }

                // Register for GPS location updates
                if (null != mLocationManager
                        .getProvider(LocationManager.GPS_PROVIDER)) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, POLLING_FREQ,
                            MIN_DISTANCE, mLocationListener);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "location updates cancelled");
                        try {
                            mLocationManager.removeUpdates(mLocationListener);
                        } catch (SecurityException e) {}
                    }
                }, MEASURE_TIME);
            } catch (SecurityException e) {
                //code should never reach here
            }

        } else {
            Log.d(TAG, "Location is good enough");
        }
    }

    // Unregister location listeners
    @Override
    protected void onPause() {
        super.onPause();

        try {
            mLocationManager.removeUpdates(mLocationListener);
        } catch (SecurityException e) {}

    }

    // Get the last known location from all providers
    // return best reading that is as accurate as minAccuracy and
    // was taken no longer then minAge milliseconds ago. If none,
    // return null.

    private Location bestLastKnownLocation(float minAccuracy, long maxAge) {

        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestAge = Long.MIN_VALUE;

        List<String> matchingProviders = mLocationManager.getAllProviders();

        for (String provider : matchingProviders) {

            try {
                Location location = mLocationManager.getLastKnownLocation(provider);

                if (location != null) {

                    location.setTime(location.getTime() + SKEW);
                    float accuracy = location.getAccuracy();
                    long time = location.getTime();

                    if (accuracy < bestAccuracy) {

                        bestResult = location;
                        bestAccuracy = accuracy;
                        bestAge = time;

                    }
                }
            } catch (SecurityException e) {

            }
        }

        // Return best reading or null
        if (bestAccuracy > minAccuracy
                || (System.currentTimeMillis() - bestAge) > maxAge) {
            return null;
        } else {
            return bestResult;
        }
    }

    // Update display
    private void updateDisplay(Location location) {

        mAccuracyView.setText(String.format("%s %.2f",getString(R.string.accuracy) , location.getAccuracy()));

        mTimeView.setText(String.format("%s %s",getString(R.string.time),
                getFormattedTime(location.getTime())));

        mLatView.setText(String.format("%s %.2f",getString(R.string.longitude), location.getLongitude()));

        mLngView.setText(String.format("%s %.2f", getString(R.string.latitude), location.getLatitude()));

    }

    private String getFormattedTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(time);
    }

    private void ensureColor() {
        if (mFirstUpdate) {
            setTextViewColor(Color.GRAY);
            mFirstUpdate = false;
        }
    }

    private void setTextViewColor(int color) {

        mAccuracyView.setTextColor(color);
        mTimeView.setTextColor(color);
        mLatView.setTextColor(color);
        mLngView.setTextColor(color);

    }

}