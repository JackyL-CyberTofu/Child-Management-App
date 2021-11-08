package ca.sfu.cmpt276.be.parentapp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * AlarmService deals with alarm and vibration when time is up.
 * It is run in the background by using Service.
 */

public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("STOP_ALARM")){
                removeNotifications();
                mediaPlayer.stop();
                vibrator.cancel();
                stopSelf();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
        mediaPlayer.setLooping(true);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("STOP_ALARM");
        registerReceiver(broadcastReceiver, intentFilter);

        Log.d("AlarmService", "AlarmAlarm");
        if(intent.getAction().equals("START_ALARM")){
            mediaPlayer.start();
            vibrator.vibrate(new long[]{100,1000,100,500,100,500,100,1000}, 0);
        } else if(intent.getAction().equals("STOP_ALARM")){
            removeNotifications();
            mediaPlayer.stop();
            vibrator.cancel();
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void removeNotifications() {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        vibrator.cancel();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
