package ca.sfu.cmpt276.be.parentapp.controller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.model.TimeConverter;
import ca.sfu.cmpt276.be.parentapp.view.TimeoutActivity;

/**
 * TimeoutService deals with calculating time left in the background.
 * Time is calculated by using CountDownTimer class.
 * UI and notification is updated everytime COUNT_DOWN_INTERVAL is passed.
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
        Bundle bundle = null;
        if (intent.getExtras() != null) {
            bundle = intent.getExtras();
        }
        if (bundle != null) {
            this.milliseconds = (long) bundle.get("Time");
            Log.i("Receiving Time", String.valueOf(milliseconds));
            if (intent.getAction().equals("STOP_ALARM")) {
                stopSelf();
            }
        }

        countDownTimer = new CountDownTimer(milliseconds, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisecondsLeft) {
                // Send Intent to the TimeoutActivity so that time left is updated.
                Intent tickIntent = new Intent("TIME_TICKED");
                tickIntent.putExtra("TimeLeft", millisecondsLeft);
                sendBroadcast(tickIntent);

                // Create an intent for moving to TimeoutActivity in your app
                Intent intent = new Intent(getApplicationContext(), TimeoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.setAction("NOTIFICATION_CLICKED");
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addNextIntentWithParentStack(intent);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                // Calculate the time left
                String updatedTime = TimeConverter.toStringForMilSeconds(millisecondsLeft + TimeConverter.getSecondInMilSeconds());

                // Create a notification for Showing time left.
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "TIMER")
                        .setSmallIcon(R.drawable.ic_baseline_timer_24)
                        .setContentTitle("Timeout")
                        .setContentText(updatedTime)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setAutoCancel(true);
                createNotificationChannel();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                notificationManager.notify(0, builder.build());
            }

            @Override
            public void onFinish() {
                // Update data for timeout manager and send intent for starting AlarmService.
                TimeoutManager timeoutManager = TimeoutManager.getInstance();
                timeoutManager.setAlarmRunning(true);
                timeoutManager.setTimerRunning(false);
                Intent finishIntent = new Intent("TIME_OUT");
                sendBroadcast(finishIntent);

                //Remove Notifications For time left
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.cancelAll();

                // Intent for moving onto TimeoutActivity when clicking notifications
                Intent intent = new Intent(getApplicationContext(), TimeoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.setAction("NOTIFICATION_CLICKED");
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addNextIntentWithParentStack(intent);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                // Intent for quitting alarm service when clicking dismiss button in the notification.
                Intent stopAlarmIntent = new Intent(getApplicationContext(), AlarmService.class);
                stopAlarmIntent.setAction("STOP_ALARM");
                PendingIntent stopAlarmPendingIntent =
                        PendingIntent.getService(getApplicationContext(), 0, stopAlarmIntent, PendingIntent.FLAG_IMMUTABLE);

                // Create a notification for time-out!
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "TIMER")
                        .setSmallIcon(R.drawable.ic_baseline_timer_24)
                        .setContentTitle(getString(R.string.notif_timeout_title))
                        .setContentText(getString(R.string.notif_timeout_body))
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .addAction(R.drawable.ic_stop_alarm, getString(R.string.notification_dismiss_title), stopAlarmPendingIntent)
                        .setAutoCancel(true);
                // Notification Channel
                createNotificationChannel();
                notificationManager = NotificationManagerCompat.from(getApplicationContext());
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

    // Notification Guideline from Android Reference
    // https://developer.android.com/training/notify-user/build-notification
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
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
