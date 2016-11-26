package qa.edu.qu.cse.cmps312.startedboundservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {

    MyBinder mBinder = new MyBinder();

    Notification.Builder mNotif;
    private static final int NOTIF_ID = 1;
    private int mProgress;
    private static final String TAG = "Service";
    private Thread mThread;
    private Messenger mClientMessenger;

    public int getCurrentProgress() {
        return mProgress;
    }

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "I started");
        mNotif =new Notification.Builder(getApplicationContext())
                .setAutoCancel(false)
                .setOngoing(false)
                .setContentTitle("Service")
                .setContentText("Running the service man")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(2000, 0, false);
        startForeground(NOTIF_ID, mNotif.build());
        startBackgroundThread();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "I am destroyed");
        stopForeground(true);
        if (mThread!=null) mThread.interrupt();
        super.onDestroy();
    }

    private void startBackgroundThread() {
        final NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int max=2000;
                mProgress = 0;
                while (mProgress<=max && !Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(500);
                        mNotif.setProgress(max, mProgress, false);
                        notifManager.notify(NOTIF_ID, mNotif.build());
                        mProgress+=20;
                        try {
                            if (mClientMessenger!=null)
                                mClientMessenger.send(Message.obtain(null, MainActivity.POST_UPDATE, mProgress));
                        } catch (RemoteException e) {
                            Log.e(TAG, "REMOTE EXCEPTION");
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                stopSelf();
            }
        });
        mThread.start();
    }

    public class MyBinder extends Binder {
        public MyService getServerInstance() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Binded at server side");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "Unbound from server side");
        mClientMessenger = null;
        return super.onUnbind(intent);
    }

    public void setMessenger(Messenger messenger) {
        mClientMessenger = messenger;
    }

}
