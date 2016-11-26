package qa.edu.qu.cse.cmps312.services;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import qa.edu.qu.cse.cmps312.services.services.MusicPlayerService;

public class MusicPlayerClient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_client);
    }

    public void buttonClicked(View view) {
        final Intent musicServiceIntent = new Intent(getApplicationContext(),
                MusicPlayerService.class);
        switch (view.getId()) {
            case R.id.play_button:

                // Start the MusicService using the Intent
                startService(musicServiceIntent);

                break;
            case R.id.stop_button:

                // Stop the MusicService using the Intent
                stopService(musicServiceIntent);

                break;

        }
    }
}
