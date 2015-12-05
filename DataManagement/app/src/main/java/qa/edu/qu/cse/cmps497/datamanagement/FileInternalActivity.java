package qa.edu.qu.cse.cmps497.datamanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FileInternalActivity extends AppCompatActivity {

    private final static String fileName = "TestFile.txt";
    private String TAG = "InternalFileWriteReadActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_internal);
        LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout);

        // Check whether fileName already exists in directory used
        // by the openFileOutput() method.
        // If the text file doesn't exist, then create it now

        if (!getFileStreamPath(fileName).exists()) {

            try {

                writeFile();

            } catch (FileNotFoundException e) {
                Log.i(TAG, "FileNotFoundException");
            }
        }


        // Read the data from the text file and display it
        try {

            readFile(ll);

        } catch (IOException e) {
            Log.i(TAG, "IOException");
        }
    }

    private void writeFile() throws FileNotFoundException {

        FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);

        PrintWriter pw = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(fos)));

        pw.println("Line 1: This is a test of the File Writing API");
        pw.println("Line 2: This is a test of the File Writing API");
        pw.println("Line 3: This is a test of the File Writing API");

        pw.close();

    }

    private void readFile(LinearLayout ll) throws IOException {

        FileInputStream fis = openFileInput(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = "";

        while (null != (line = br.readLine())) {

            TextView tv = new TextView(this);
            tv.setTextSize(18);
            tv.setText(line);

            ll.addView(tv);

        }

        br.close();

    }

}