package qa.edu.qu.cse.cmps312.multimedia;

import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AudioRecordingActivity extends Activity {
    private static final String TAG = "AudioRecordTest";
    private static final int MY_REQUEST_CODE = 55;
    private String mFileName;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private AudioManager mAudioManager;
    private ToggleButton mPlayButton;
    private ToggleButton mRecordButton;
    private int mPermCount = 0;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_audio_recording);

        mFileName = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/audiorecordtest.3gp";


        mRecordButton = (ToggleButton) findViewById(R.id.record_button);
        mPlayButton = (ToggleButton) findViewById(R.id.play_button);

        // Set up record Button
        mRecordButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                // Set enabled state
                mPlayButton.setEnabled(!isChecked);

                // Start/stop recording
                onRecordPressed(isChecked);

            }
        });

        // Set up play Button
        mPlayButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                // Set enabled state
                mRecordButton.setEnabled(!isChecked);

                // Start/stop playback
                onPlayPressed(isChecked);
            }
        });

        // Get AudioManager
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Request audio focus
        mAudioManager.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

    }

    // Toggle recording
    private void onRecordPressed(boolean shouldStartRecording) {

        if (shouldStartRecording) {
            startRecording();
        } else {
            stopRecording();
        }

    }

    // Start recording with MediaRecorder
    private void startRecording() {

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e(TAG, "Couldn't prepare and start MediaRecorder");
            }

            mRecorder.start();
        } else {
            mPermCount++;

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.error)
                        .setMessage(R.string.must_accept_permission)
                        .setIcon(android.R.drawable.stat_notify_error)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(AudioRecordingActivity.this,
                                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_REQUEST_CODE);
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(AudioRecordingActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_REQUEST_CODE);
            }
        }
    }

    // Stop recording. Release resources
    private void stopRecording() {

        if (null != mRecorder) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }

    }

    // Toggle playback
    private void onPlayPressed(boolean shouldStartPlaying) {

        if (shouldStartPlaying) {
            startPlaying();
        } else {
            stopPlaying();
        }

    }

    // Playback audio using MediaPlayer
    private void startPlaying() {

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();

            //mimic user clicking on button when audio is finished playing
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mPlayButton.performClick();
                }
            });


        } catch (IOException e) {
            Log.e(TAG, "Couldn't prepare and start MediaPlayer");
        }

    }

    // Stop playback. Release resources
    private void stopPlaying() {
        if (null != mPlayer) {
            if (mPlayer.isPlaying())
                mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    // Listen for Audio Focus changes
    OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {

            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mAudioManager.abandonAudioFocus(afChangeListener);

                // Stop playback, if necessary
                if (null != mPlayer && mPlayer.isPlaying())
                    stopPlaying();
            }

        }

    };

    // Release recording and playback resources, if necessary
    @Override
    public void onPause() {
        super.onPause();

        if (null != mRecorder) {
            mRecorder.release();
            mRecorder = null;
        }

        if (null != mPlayer) {
            mPlayer.release();
            mPlayer = null;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_REQUEST_CODE:

                if (mPermCount<3) {
                    startRecording();
                } else {
                    Toast.makeText(this, R.string.cant_record_audio, Toast.LENGTH_LONG).show();
                    mPlayButton.setEnabled(false);
                    mRecordButton.setEnabled(false);
                    mPlayButton.setEnabled(false);
                }
        }
    }
}