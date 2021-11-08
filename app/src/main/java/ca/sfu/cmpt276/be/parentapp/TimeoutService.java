package ca.sfu.cmpt276.be.parentapp;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ca.sfu.cmpt276.be.parentapp.model.TimeConverter;
import ca.sfu.cmpt276.be.parentapp.model.TimeoutManager;

/**
 * TimeoutoService deals with background countdown work by using service.
 */
public class TimeoutService extends Service {
    public static final int COUNT_DOWN_INTERVAL = 20;
    private CountDownTimer countDownTimer = null;
    private long milliseconds;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if(intent != null && bundle != null) {
            this.milliseconds = (long) bundle.get("Time");
            Log.i("Receiving Time",String.valueOf(milliseconds));
            if(intent.getAction().equals("STOP_ALARM")){
                stopSelf();
            }
        }

        countDownTimer = new CountDownTimer(milliseconds, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisecondsLeft) {
                Intent tickIntent = new Intent("TIME_TICKED");
                tickIntent.putExtra("TimeLeft", millisecondsLeft);
                sendBroadcast(tickIntent);

                // Create an explicit intent for an Activity in your app
                Intent intent = new Intent(getApplicationContext(), TimeoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.setAction("NOTIFICATION_CLICKED");
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addNextIntentWithParentStack(intent);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                // Previous Notifications
                String updatedTime = TimeConverter.toStringForMilSeconds(millisecondsLeft + TimeConverter.getSecondInMilSeconds());

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "TIMER")
                        .setSmallIcon(R.drawable.ic_baseline_timer_24)
                        .setContentTitle("Timeout")
                        .setContentText(String.valueOf(updatedTime))
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setAutoCancel(true);
                createNotificationChannel();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(0, builder.build());

            }

            @Override
            public void onFinish() {
                TimeoutManager timeoutManager = TimeoutManager.getInstance();
                timeoutManager.setAlarmRunning(true);
                Intent finishIntent = new Intent("TIME_OUT");
                sendBroadcast(finishIntent);
                //Log.i("Finished","TIME_OUT");

                //Remove Notifications
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.cancelAll();

                timeoutManager.setTimerRunning(false);

                // Create an explicit intent for an Activity in your app
                Intent intent = new Intent(getApplicationContext(), TimeoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.setAction("NOTIFICATION_CLICKED");
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addNextIntentWithParentStack(intent);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                // Create intent when clicking dismiss button at notifications.
                Intent stopAlarmIntent = new Intent(getApplicationContext(), AlarmService.class);
                stopAlarmIntent.setAction("STOP_ALARM");
                PendingIntent stopAlarmPendingIntent =
                        PendingIntent.getService(getApplicationContext(),0,stopAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Create a notification for time-out!
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "TIMER")
                        .setSmallIcon(R.drawable.ic_baseline_timer_24)
                        .setContentTitle("Timeout")
                        .setContentText("Time's up!")
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .addAction(R.drawable.ic_stop_alarm,getString(R.string.dismiss), stopAlarmPendingIntent)
                        .setAutoCancel(true);
                createNotificationChannel();

                notificationManager = NotificationManagerCompat.from(getApplicationContext());
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(0, builder.build());

                // Start AlarmService
                Intent alarmServiceIntent = new Intent(getApplicationContext(), AlarmService.class);
                alarmServiceIntent.setAction("START_ALARM");
                startService(alarmServiceIntent);
            }
        };
        countDownTimer.start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("TIMER", name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
