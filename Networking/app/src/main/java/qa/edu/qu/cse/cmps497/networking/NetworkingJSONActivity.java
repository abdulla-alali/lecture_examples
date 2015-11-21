package qa.edu.qu.cse.cmps497.networking;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import qa.edu.qu.cse.cmps497.networking.Consts.Consts;

public class NetworkingJSONActivity extends AppCompatActivity {

    ListView mListView;
    TextView mTextview;
    ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networking_json);
        mListView = (ListView)findViewById(R.id.listView);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mTextview = (TextView)findViewById(R.id.textView1);
        new HttpGetTask().execute(Consts.URL + Consts.USERNAME);
    }

    private class HttpGetTask extends AsyncTask<String, Void, List<String>> {

        HttpURLConnection httpUrlConnection;
        private String TAG = "HttpGetTask";

        @Override
        protected void onPreExecute() {
            mTextview.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }

        @Override
        protected List<String> doInBackground(String... params) {
            String data = null;
            try {

                Thread.sleep(2000);
                httpUrlConnection = (HttpURLConnection) new URL(params[0])
                        .openConnection();

                InputStream in = new BufferedInputStream(
                        httpUrlConnection.getInputStream());

                data = readStream(in);

            } catch (InterruptedException e) {
                Log.e(TAG, "InterruptedException occured");
            } catch (MalformedURLException exception) {
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                Log.e(TAG, "IOException");
            } finally {
                if (httpUrlConnection != null)
                    httpUrlConnection.disconnect();
            }

            if (data != null) {
                return getJSON(data);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (result==null) {
                //setup if error occured
            } else {
                mTextview.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mListView.setAdapter(new ArrayAdapter<String>(
                        NetworkingJSONActivity.this,
                        android.R.layout.simple_list_item_1, result));
            }
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
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
    }

    private List<String> getJSON(String data) {
        final String LONGITUDE_TAG = "lng";
        final String LATITUDE_TAG = "lat";
        final String MAGNITUDE_TAG = "magnitude";
        final String EARTHQUAKE_TAG = "earthquakes";

        List<String> result = new ArrayList<String>();

        try {

            // Get top-level JSON Object - a Map
            JSONObject responseObject = (JSONObject) new JSONTokener(
                    data).nextValue();

            // Extract value of "earthquakes" key -- a List
            JSONArray earthquakes = responseObject
                    .getJSONArray(EARTHQUAKE_TAG);

            // Iterate over earthquakes list
            for (int idx = 0; idx < earthquakes.length(); idx++) {

                // Get single earthquake data - a Map
                JSONObject earthquake = (JSONObject) earthquakes.get(idx);

                // Summarize earthquake data as a string and add it to
                // result
                result.add(MAGNITUDE_TAG + ":"
                        + earthquake.get(MAGNITUDE_TAG) + ","
                        + LATITUDE_TAG + ":"
                        + earthquake.get(LATITUDE_TAG) + ","
                        + LONGITUDE_TAG + ":"
                        + earthquake.get(LONGITUDE_TAG));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}