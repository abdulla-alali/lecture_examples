package qa.edu.qu.cse.cmps497.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import qa.edu.qu.cse.cmps497.networking.Consts.Consts;

public class NetworkingSocketActivity extends AppCompatActivity {
    TextView mTextView;

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

        private static final String HOST = "api.geonames.org";

        // Get your own user name at http://www.geonames.org/login
        private static final String HTTP_GET_COMMAND = "GET /earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
                + Consts.USERNAME
                + " HTTP/1.1"
                + "\n"
                + "Host: "
                + HOST
                + "\n"
                + "Connection: close" + "\n\n";

        private static final String TAG = "HttpGet";

        @Override
        protected String doInBackground(Void... params) {
            Socket socket = null;
            String data = "";

            try {
                socket = new Socket(HOST, 80);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                        socket.getOutputStream()), true);
                pw.println(HTTP_GET_COMMAND);

                data = readStream(socket.getInputStream());

            } catch (UnknownHostException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                if (null != socket)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "IOException");
                    }
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            mTextView.setText(result);
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer();
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
                        Log.e(TAG, "IOException");
                    }
                }
            }
            return data.toString();
        }
    }
}