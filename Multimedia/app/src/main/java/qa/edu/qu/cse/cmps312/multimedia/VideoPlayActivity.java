package qa.edu.qu.cse.cmps312.multimedia;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayActivity extends Activity {
    VideoView mVideoView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        // Get a reference to the VideoView

        mVideoView = (VideoView) findViewById(R.id.videoViewer);

        // Add a Media controller to allow forward/reverse/pause/resume

        final MediaController mMediaController = new MediaController(
                VideoPlayActivity.this, true);

        mMediaController.setEnabled(false);

        mVideoView.setMediaController(mMediaController);

        mVideoView
                .setVideoURI(Uri
                        .parse("android.resource://qa.edu.qu.cse.cmps312.multimedia/raw/theeb"));

        // Add an OnPreparedListener to enable the MediaController once the video is ready
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaController.setEnabled(true);
            }
        });
    }

    // Clean up and release resources
    @Override
    protected void onPause() {

        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }
        super.onPause();
    }
}