package qa.edu.qu.cse.cmps312.earthquakemap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsEarthquakeMapActivity extends Activity {

	// Coordinates used for centering the Map

	private static final double CAMERA_LNG = 87.0;
	private static final double CAMERA_LAT = 17.0;

	// URL for getting the earthquake
	// replace with your own user name

	private final static String UNAME = "demo";
	private final static String URL = "http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
			+ UNAME;

	public static final String TAG = "MapsEarthquakeMap";

	// Set up UI and get earthquake data
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		new HttpGetTask().execute(URL);

	}

	private class HttpGetTask extends
			AsyncTask<String, Void, List<EarthQuakeRec>> {

		HttpURLConnection httpUrlConnection;

		@Override
		protected List<EarthQuakeRec> doInBackground(String... params) {

			String data = null;
			try {

				httpUrlConnection = (HttpURLConnection) new URL(params[0])
						.openConnection();

				InputStream in = new BufferedInputStream(
						httpUrlConnection.getInputStream());

				data = readStream(in);

			} catch (MalformedURLException exception) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				Log.e(TAG, "IOException");
			} finally {
				if (httpUrlConnection != null)
					httpUrlConnection.disconnect();
			}

			if (data != null) {
				return JSONResponseHandler.getJSON(data);
			}
			return null;

		}

		private String readStream(InputStream in) {
			BufferedReader reader = null;
			StringBuilder data = new StringBuilder();
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					data.append(line);
				}
			} catch (IOException e) {
				Log.e(TAG, "IOException");
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return data.toString();
		}

		@Override
		protected void onPostExecute(List<EarthQuakeRec> result) {

            GoogleMap mMap;
			// Get Map Object
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			if (null != mMap) {

				// Add a marker for every earthquake

				for (EarthQuakeRec rec : result) {

					// Add a new marker for this earthquake
					mMap.addMarker(new MarkerOptions()

							// Set the Marker's position
							.position(new LatLng(rec.getLat(), rec.getLng()))

							// Set the title of the Marker's information window
							.title(String.valueOf(rec.getMagnitude()))

							// Set the color for the Marker
							.icon(BitmapDescriptorFactory
									.defaultMarker(getMarkerColor(rec
											.getMagnitude()))));

				}

				// Center the map 
				// Should compute map center from the actual data
				
				mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
						CAMERA_LAT, CAMERA_LNG)));

			}

		}

		// Assign marker color
		private float getMarkerColor(double magnitude) {

			if (magnitude < 6.0) {
				magnitude = 6.0;
			} else if (magnitude > 9.0) {
				magnitude = 9.0;
			}

			return (float) (120 * (magnitude - 6));
		}

	}

}