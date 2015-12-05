package qa.edu.qu.cse.cmps497.datamanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.shared_prefs_button:
                startActivity(new Intent(this, SharedPreferencesActivity.class));
                break;
            case R.id.prefs_fragment_button:
                startActivity(new Intent(this, PreferenceFragmentActivity.class));
                break;
            case R.id.file_internal_button:
                startActivity(new Intent(this, FileInternalActivity.class));
                break;
            case R.id.file_external_button:
                startActivity(new Intent(this, FileExternalActivity.class));
                break;
            case R.id.sql_database_button:
                startActivity(new Intent(this, SQLDatabaseActivity.class));
                break;
        }
    }
}
