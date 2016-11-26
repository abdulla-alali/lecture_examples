package qa.edu.qu.cse.cmps312.services;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import qa.edu.qu.cse.cmps312.services.services.LocalLoggingService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {


            case R.id.local_logging_button:
                // Create an Intent for starting the LoggingService
                Intent startServiceIntent = new Intent(getApplicationContext(),
                        LocalLoggingService.class);

                // Put Logging message in intent
                startServiceIntent.putExtra(LocalLoggingService.EXTRA_LOG,
                        "Log this message");

                // Start the Service
                startService(startServiceIntent);
                break;


            case R.id.music_button:
                startActivity(new Intent(this, MusicPlayerClient.class));
                break;

            case R.id.bound_button:
                startActivity(new Intent(this, BindingActivity.class));
                break;
        }
    }
}
