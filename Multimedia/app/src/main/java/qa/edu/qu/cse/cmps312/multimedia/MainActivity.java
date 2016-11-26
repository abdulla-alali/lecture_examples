package qa.edu.qu.cse.cmps312.multimedia;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class MainActivity extends Activity {

    private static final int MY_REQUEST_CODE = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.audiomanager:
                startActivity(new Intent(this, AudioManagerActivity.class));
                break;
            case R.id.ringtonemanager:
                startActivity(new Intent(this, RingtoneManagerActivity.class));
                break;
            case R.id.video_play:
                startActivity(new Intent(this, VideoPlayActivity.class));
                break;
            case R.id.audio_recording:
                startActivity(new Intent(this, AudioRecordingActivity.class));
                break;
            case R.id.camera:
                /** Camera activity is a work in progress. Current implementation
                 * is deprecated and therefore this button should be disabled for now.
                 * Students also need to understand SurfaceHolder class as a pre-requisite
                 */
                int permissionCheck = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(this, CameraActivity.class));
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CODE);
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(this, CameraActivity.class));
                }
        }
    }
}
