package qa.edu.qu.cse.cmps497.threading;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class NoThreading extends Activity {
    private Bitmap mBitmap;
    private ImageView mIView;
    private int mDelay = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_threading);
        mIView = (ImageView) findViewById(R.id.imageView);

        final Button loadButton = (Button) findViewById(R.id.loadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIcon();
                if (null != mBitmap) {
                    mIView.setImageBitmap(mBitmap);
                }
            }
        });

        final Button otherButton = (Button) findViewById(R.id.otherButton);
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NoThreading.this, "I'm Working",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadIcon() {
        try {
            Thread.sleep(mDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.painter);
    }
}