package qa.edu.qu.cse.cmps497.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchNotification(View view) {

        final NotificationManager nManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder mBuilder = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("TITLE")
                .setContentText("This is the text");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.qu.edu.qa"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 31, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);


        nManager.notify(31, mBuilder.build());

    }

    public void customToastClicked(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("This is a custom toast");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }

    public void launchToast(View view) {
        Toast.makeText(MainActivity.this, "This is a toast !", Toast.LENGTH_LONG).show();
    }

    public void progressNotifClicked(View view) {

        final int max=100;
        final NotificationManager nManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder mBuilder = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("TITLE")
                .setContentText("This is the text");

        //set the progress of the progressBar inside the notification
        //Here, max is 100, initial value is 20, and is a determinate progressbar
        mBuilder.setProgress(max, 0, false);

        //set ongoing so the user cannot dismiss the notification
        mBuilder.setOngoing(true);

        //This thread increments the progress until max is reached.
        //it sleeps for one more second, then removes the progress bar
        //and sets the text to "download complete"
        new Thread(new Runnable() {
            @Override
            public void run() {

                int prog = 0;
                while (prog <= max) {
                    try {
                        Thread.sleep(1500);  //sleep for 1.5 seconds
                    } catch (Exception e) {

                    }
                    mBuilder.setProgress(max, prog, false);
                    //notify again through the notification manager
                    nManager.notify(32, mBuilder.build());
                    prog+=20;

                }

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {}

                //when the progress reached 100%, remove progress bar
                mBuilder.setProgress(0, 0, false);
                //change text to download is complete !
                mBuilder.setContentText("Download is complete !");
                //set ongoing back to false, so user can swipe or dismiss it
                mBuilder.setOngoing(false);
                //always notify through the notification manager to update the
                //notification view
                nManager.notify(32, mBuilder.build());

            }
        }).start();


        //Notice that we do not have a pending intent associated here.
        //i.e. nothing happens when the user clicks on the notification

        nManager.notify(32, mBuilder.build());
    }
}
