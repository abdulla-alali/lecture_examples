package qa.edu.qu.cse.cmps497.networking;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class NetworkingXMLActivity extends AppCompatActivity {

    ListView mListView;
    TextView mTextview;
    ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networking_json);
        mListView = (ListView) findViewById(R.id.listView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTextview = (TextView) findViewById(R.id.textView1);
        new HttpGetTask().execute(Consts.URLXML + Consts.USERNAME);
    }

    private class HttpGetTask extends AsyncTask<String, Void, List<String>> {

        HttpURLConnection httpUrlConnection;
        String TAG = "HTTPGetTask";

        @Override
        protected void onPreExecute() {
            mTextview.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }

        @Override
        protected List<String> doInBackground(String... params) {
            try {

                Thread.sleep(2000);
                httpUrlConnection = (HttpURLConnection) new URL(params[0])
                        .openConnection();

                return new XMLResponseHandler().parseContent(httpUrlConnection.getInputStream());

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

            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (result != null) {
                mTextview.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mListView.setAdapter(new ArrayAdapter<String>(
                        NetworkingXMLActivity.this,
                        android.R.layout.simple_list_item_1, result));
            } else {
                new AlertDialog.Builder(NetworkingXMLActivity.this).setTitle(R.string.error)
                        .setMessage(R.string.xml_error)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NetworkingXMLActivity.this.finish();
                                dialog.dismiss();
                            }
                        }).show();
            }
        }

    }
}