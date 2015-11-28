package qa.edu.qu.cse.cmps497.location;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationServicesActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final long ONE_MIN = 1000 * 60;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long MEASURE_TIME = 1000 * 30;
    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;

    // Views for display location information
    private TextView mAccuracyView;
    private TextView mTimeView;
    private TextView mLatView;
    private TextView mLngView;

    // Current best location estimate
    private Location mBestReading;

    // Reference to the GoogleApiClient
    private GoogleApiClient mGoogleApiClient;

    private final String TAG = "LocationServices";

    private boolean mFirstUpdate = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_manager);

        mAccuracyView = (TextView) findViewById(R.id.accuracy_view);
        mTimeView = (TextView) findViewById(R.id.time_view);
        mLatView = (TextView) findViewById(R.id.lat_view);
        mLngView = (TextView) findViewById(R.id.lng_view);


        //make sure play services are available
        if (!isPlayServiceAvailable()) {
            setPlayServiceUnavailable();
        } else {
            //build the API client, when connected, get lastKnownLocation
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }

    }

    LocationListener mLocationListener = new LocationListener() {

        // Called back when location changes

        @Override
        public void onLocationChanged(Location location) {

            ensureColor();

            // Determine whether new location is better than current best
            // estimate

            Log.d(TAG, "Location changed. Accuracy: " + location.getAccuracy());

            if (null == mBestReading
                    || location.getAccuracy() <= mBestReading.getAccuracy()) {

                // Update best estimate
                mBestReading = location;

                // Update display
                updateDisplay(location);

                if (mBestReading.getAccuracy() <= MIN_ACCURACY)
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);

            }
        }
    };


    // Unregister location listeners
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Update display
    private void updateDisplay(Location location) {

        mAccuracyView.setText(String.format("%s %.2f",getString(R.string.accuracy) , location.getAccuracy()));

        mTimeView.setText(String.format("%s %s",getString(R.string.time),
                getFormattedTime(location.getTime())));

        mLatView.setText(String.format("%s %.2f",getString(R.string.longitude), location.getLongitude()));

        mLngView.setText(String.format("%s %.2f", getString(R.string.latitude), location.getLatitude()));

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

    private String getFormattedTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(time);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connected to Google's APIs");

        mBestReading = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mBestReading!=null)
            Log.d(TAG, "Time: " + getFormattedTime(mBestReading.getTime()) + " Accuracy: " + mBestReading.getAccuracy() + " and provider: " + mBestReading.getProvider());
        if (mBestReading == null || (System.currentTimeMillis() - mBestReading.getTime()) > FIVE_MIN ||
                mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY) {
            mBestReading = null;
        }


        if (null != mBestReading) {

            updateDisplay(mBestReading);

        } else {

            mAccuracyView.setText(R.string.no_init_readings);

        }


        if (null == mBestReading
                || mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
                || mBestReading.getTime() < System.currentTimeMillis()
                - ONE_MIN) {

            Log.d(TAG, "Need to update old data");
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(POLLING_FREQ);
            mLocationRequest.setFastestInterval(POLLING_FREQ/2);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setExpirationDuration(MEASURE_TIME);

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, mLocationListener);

        } else {
            Log.d(TAG, "Location stored is good enough");
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "connection is suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed " + connectionResult.getErrorMessage());
        setPlayServiceUnavailable();
    }

    private void setPlayServiceUnavailable() {
        mAccuracyView.setText(R.string.play_services_not_installed);
    }

    public boolean isPlayServiceAvailable() {
        // Check that Google Play Services are available
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);

        // If Google Play services is available

        return (ConnectionResult.SUCCESS == resultCode);
    }
}