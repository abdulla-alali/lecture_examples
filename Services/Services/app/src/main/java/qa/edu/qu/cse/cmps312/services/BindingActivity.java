package qa.edu.qu.cse.cmps312.services;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.binder_bound_button:
                startActivity(new Intent(this, BinderBoundActivity.class));
                break;

            case R.id.messenger_bound_button:
                startActivity(new Intent(this, MessengerBoundActivity.class));
                break;

            case R.id.aidl_bound_button:
                startActivity(new Intent(this, AIDLBoundActivity.class));
                break;
        }
    }
}
