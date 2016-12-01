package qa.edu.qu.cse.cmps312.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;

public class MyJobService extends JobService {


    private final static String TAG = "JobService";
    private MyTask mTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job start with ID: " + jobParameters.getJobId());

        mTask = new MyTask();


        mTask.execute(jobParameters);


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        Log.d(TAG, "onStopJob is executed. It wants me to stop job id: " + jobParameters.getJobId());

        if (!mTask.isCancelled()) {
            mTask.cancel(false);
            return true;
        }

        //return false to not reschedule. True to reschedule if you haven't finished and you should
        //quit your task
        return false;
    }


    private class MyTask extends AsyncTask<JobParameters, Void, JobParameters> {

        @Override
        protected JobParameters doInBackground(JobParameters... jobParameterses) {

            Log.d(TAG, "We are running in MyTask in Job with ID: " + jobParameterses[0].getJobId());

            int retry = 0;

            while (!isCancelled() && retry < 10) {
                try {
                    retry++;
                    Log.d(TAG, "Sleeping " + retry + "/10");
                    Thread.sleep(2 * 1000);
                } catch (Exception e) {
                    Log.d(TAG, "Sleep has been interrupted");
                }
            }

            Log.d(TAG, "We are " + (isCancelled()?"not ":"") + "done with Job");
            if (isCancelled()) jobFinished(jobParameterses[0], true);

            return jobParameterses[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            Log.d(TAG, "Task finished successfully");
            jobFinished(jobParameters, false);
            super.onPostExecute(jobParameters);
        }
    }

}
