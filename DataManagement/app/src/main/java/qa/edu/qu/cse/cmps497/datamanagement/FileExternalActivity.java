package qa.edu.qu.cse.cmps497.datamanagement;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileExternalActivity extends AppCompatActivity {
    private final String fileName = "painter.png";
    private String TAG = "ExternalFile";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_external);

        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            Log.i(TAG, "Media is mounted");

            File outFile = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    fileName);

            if (!outFile.exists())
                copyImageToMemory(outFile);

            ImageView imageview = (ImageView) findViewById(R.id.image);
            imageview.setImageURI(Uri.parse("file://" + outFile.getAbsolutePath()));

        }
    }

    private void copyImageToMemory(File outFile) {
        try {

            BufferedOutputStream os = new BufferedOutputStream(
                    new FileOutputStream(outFile));

            BufferedInputStream is = new BufferedInputStream(getResources()
                    .openRawResource(R.raw.painter));

            copy(is, os);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
        }
    }

    private void copy(InputStream is, OutputStream os) {
        final byte[] buf = new byte[1024];
        int numBytes;
        try {
            while (-1 != (numBytes = is.read(buf))) {
                os.write(buf, 0, numBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException");

            }
        }
    }
}