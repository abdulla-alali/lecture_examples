package qa.edu.qu.cse.cmps497.networking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.networking_url:
                startActivity(new Intent(this, NetworkingURLActivity.class));
                break;
            case R.id.networking_socket:
                startActivity(new Intent(this, NetworkingSocketActivity.class));
                break;
            case R.id.networking_json:
                startActivity(new Intent(this, NetworkingJSONActivity.class));
                break;
            case R.id.networking_xml:
                startActivity(new Intent(this, NetworkingXMLActivity.class));
                break;
        }
    }
}
