package qa.edu.qu.cse.cmps312.threading;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HandlerRunnables extends AppCompatActivity {

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Bitmap mBitmap;
    private int mDelay = 500;
    private final Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_runnables);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        final Button button = (Button) findViewById(R.id.loadButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new LoadIconTask(R.drawable.painter)).start();
            }
        });

        final Button otherButton = (Button) findViewById(R.id.otherButton);
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HandlerRunnables.this, "I'm Working",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class LoadIconTask implements Runnable {
        int resId;

        LoadIconTask(int resId) {
            this.resId = resId;
        }

        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            mBitmap = BitmapFactory.decodeResource(getResources(), resId);

            // Simulating long-running operation

            for (int i = 1; i < 11; i++) {
                sleep();
                final int step = i;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setProgress(step * 10);
                    }
                });
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageBitmap(mBitmap);
                }
            });

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            });
        }
    }

    private void sleep() {
        try {
            Thread.sleep(mDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}