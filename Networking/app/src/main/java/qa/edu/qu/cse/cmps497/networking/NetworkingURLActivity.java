package qa.edu.qu.cse.cmps497.networking;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import qa.edu.qu.cse.cmps497.networking.Consts.Consts;

public class NetworkingURLActivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_networking_url);
        mTextView = (TextView) findViewById(R.id.textView1);

        final Button loadButton = (Button) findViewById(R.id.button1);
        loadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new HttpGetTask().execute();
            }
        });
    }

    private class HttpGetTask extends AsyncTask<Void, Void, String> {

        private static final String TAG = "HttpGetTask";

        @Override
        protected String doInBackground(Void... params) {
            String data = "";
            HttpURLConnection httpUrlConnection = null;

            try {
                //This is using my username and password
                httpUrlConnection = (HttpURLConnection) new URL(Consts.URL + Consts.USERNAME)
                        .openConnection();

                InputStream in = new BufferedInputStream(
                        httpUrlConnection.getInputStream());

                data = readStream(in);

            } catch (MalformedURLException exception) {
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                Log.e(TAG, "IOException");
            } finally {
                if (null != httpUrlConnection)
                    httpUrlConnection.disconnect();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            mTextView.setText(result);
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
}