package qa.edu.qu.cse.cmps312.jobscheduler;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final int JOB_ID = 1;
    private JobScheduler mScheduler;
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);


        Button button1 = (Button)findViewById(R.id.button);
        button1.setOnClickListener(this);

        Button button2 = (Button)findViewById(R.id.cancel_button);
        button2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:

                JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,
                        new ComponentName(this, MyJobService.class));
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                        .setRequiresCharging(true);

                if (mScheduler.schedule(builder.build())<1) {
                    Log.e(TAG, "Can't schedule job for some reason. Check your JobInfo parameters");
                };

                break;
            case R.id.cancel_button:

                mScheduler.cancel(JOB_ID);

                break;
        }
    }
}
