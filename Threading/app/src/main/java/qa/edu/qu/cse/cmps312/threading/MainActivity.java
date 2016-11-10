package qa.edu.qu.cse.cmps312.threading;

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

    public void buttonPressed(View view) {
        switch (view.getId()) {
            case R.id.threadingnothreading_button:
                startActivity(new Intent(this, NoThreading.class));
                break;
            case R.id.threadingsimple_button:
                startActivity(new Intent(this, SimpleThreading.class));
                break;
            case R.id.threadingrunonuithread_button:
                startActivity(new Intent(this, RunOnUi.class));
                break;
            case R.id.threadingviewpost_button:
                startActivity(new Intent(this, ViewPost.class));
                break;
            case R.id.threadingasynctask_button:
                startActivity(new Intent(this, AsyncTaskExample.class));
                break;
            case R.id.handler_runnables_button:
                startActivity(new Intent(this, HandlerRunnables.class));
                break;
            case R.id.handler_messages_button:
                startActivity((new Intent(this, HandlerMessages.class)));
                break;
        }
    }
}
