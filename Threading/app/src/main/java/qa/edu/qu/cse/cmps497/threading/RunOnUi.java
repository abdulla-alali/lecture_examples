package qa.edu.qu.cse.cmps497.threading;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class RunOnUi extends Activity {
    private Bitmap mBitmap;
    private ImageView mImageView;
    private int mDelay = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_on_ui);

        mImageView = (ImageView) findViewById(R.id.imageView);

        final Button button = (Button) findViewById(R.id.loadButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIcon();
            }
        });

        final Button otherButton = (Button) findViewById(R.id.otherButton);
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RunOnUi.this, "I'm Working",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadIcon() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(mDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mBitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.painter);
                RunOnUi.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(mBitmap);
                    }
                });
            }
        }).start();
    }
}
