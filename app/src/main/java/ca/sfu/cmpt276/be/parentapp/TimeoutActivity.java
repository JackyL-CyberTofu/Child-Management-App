package ca.sfu.cmpt276.be.parentapp;

/**
 * TimeoutActivity represents a feature of countown timer in the app.
 * Users can set their customized time.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.model.TimeConverter;
import ca.sfu.cmpt276.be.parentapp.model.TimeoutManager;

public class TimeoutActivity extends AppCompatActivity {
    private ConstraintLayout setting;
    private ConstraintLayout timer;
    private TextView countdownText;
    private Button startButton;
    private Button stopButton;
    private Button cancelButton;

    private TimeoutManager timeoutManager;

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutActivity.class);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Braodcast Received",String.valueOf(intent.getAction()));
            String action = intent.getAction();
            if(action.equals("TIME_TICKED")){
                Log.i("Ticking",String.valueOf(intent.getAction()));
                updateGUI(intent);
            } else if (action.equals("TIME_OUT")) {
                switchSettingDisplay();
                popUpAlarmTurningOffDialog();
            } else if (action.equals("NOTIFICATION_CLICKED")) {
                Log.d("Notification", "Clicked and now in activity");
                popUpAlarmTurningOffDialog();
            }
        }
    };

    private void removeNotifications() {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createIntentFilterAndRegisterReceiver();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);

        timeoutManager = TimeoutManager.getInstance();
        assignViewComponents();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NumberPicker numberPicker = findViewById(R.id.numberPicker);
        setupNumberPicker(numberPicker, 99);

        NumberPicker numberPicker2 = findViewById(R.id.numberPicker2);
        setupNumberPicker(numberPicker2, 60);

        NumberPicker numberPicker3 = findViewById(R.id.numberPicker3);
        setupNumberPicker(numberPicker3, 60);

        setupTimerShortcuts(numberPicker, numberPicker2, numberPicker3);

        startButton.setOnClickListener(v -> {
            timeoutManager.setFirstState(true);
            startOrStop();
        });

        stopButton.setOnClickListener(v -> startOrStop());

        cancelButton.setOnClickListener(v -> {
            timeoutManager.setFirstState(true);
            cancelTimer();
        });

        if(timeoutManager.isTimerRunning()) {
            switchTimerDisplay();
        } else {
            switchSettingDisplay();
        }

        if(timeoutManager.isAlarmRunning()) {
            popUpAlarmTurningOffDialog();
        }
    }

    private void popUpAlarmTurningOffDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TimeoutActivity.this);
        builder.setTitle("Time's up!");
        builder.setMessage("You can turn off the alarm by clicking Yes button");
        builder.setIcon(R.drawable.ic_baseline_timer_24);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stopService(new Intent(getApplicationContext(), AlarmService.class));
                removeNotifications();
                TimeoutManager timeoutManager = TimeoutManager.getInstance();
                timeoutManager.setAlarmRunning(false);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timeoutManager.isTimerRunning()) {
            switchTimerDisplay();
        } else {
            switchSettingDisplay();
        }
        createIntentFilterAndRegisterReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void createIntentFilterAndRegisterReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("TIME_TICKED");
        intentFilter.addAction("TIME_OUT");
        intentFilter.addAction("NOTIFICATION_CLICKED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void setupNumberPicker(NumberPicker numberPicker, int i2) {
        numberPicker.setFormatter(i -> {
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(i2);
            return String.format("%02d", i);
        });
    }

    private void setupTimerShortcuts(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        Button oneButton = findViewById(R.id.oneTimer);
        oneButton.setOnClickListener(view -> {
            numberPicker2.setValue(1);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });

        Button twoButton = findViewById(R.id.twoTimer);
        twoButton.setOnClickListener(view -> {
            numberPicker2.setValue(2);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });

        Button threeButton = findViewById(R.id.threeTimer);
        threeButton.setOnClickListener(view -> {
            numberPicker2.setValue(3);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });

        Button fiveButton = findViewById(R.id.fiveTimer);
        fiveButton.setOnClickListener(view -> {
            numberPicker2.setValue(5);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });

        Button tenButton = findViewById(R.id.tenTimer);
        tenButton.setOnClickListener(view -> {
            numberPicker2.setValue(10);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });

        Button fifteenButton = findViewById(R.id.fifteenTime);
        fifteenButton.setOnClickListener(view -> {
            numberPicker2.setValue(15);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });
    }

    private void updateGUI(Intent intent) {
        Bundle bundle = intent.getExtras();
        timeoutManager.setTempTime((long) bundle.get("TimeLeft"));
        String updatedTime = TimeConverter.toStringForMilSeconds(timeoutManager.getTempTime() + TimeConverter.getSecondInMilSeconds());
        countdownText.setText(updatedTime);
    }

    private void switchSettingDisplay() {
        setting.setVisibility(View.VISIBLE);
        timer.setVisibility(View.GONE);
    }

    private void switchTimerDisplay() {
        setting.setVisibility(View.GONE);
        timer.setVisibility(View.VISIBLE);
    }

    private void assignViewComponents() {
        countdownText = findViewById(R.id.textView_countdown);
        startButton = findViewById(R.id.button_start_countdown);
        stopButton = findViewById(R.id.button_pause_timer);
        cancelButton = findViewById(R.id.button_cancel_timer);

        setting = findViewById(R.id.setting);
        timer = findViewById(R.id.timer);

        timer.setVisibility(View.GONE);
    }

    private void startOrStop() {
        switchTimerDisplay();
        if (timeoutManager.isTimerRunning()) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        if (timeoutManager.isFirstState()) {
            NumberPicker numberPicker = findViewById(R.id.numberPicker);
            NumberPicker numberPicker2 = findViewById(R.id.numberPicker2);
            NumberPicker numberPicker3 = findViewById(R.id.numberPicker3);

            String sHour = Integer.toString(numberPicker.getValue());
            String sMin = Integer.toString(numberPicker2.getValue());
            String sSecond = Integer.toString(numberPicker3.getValue());
            timeoutManager.setTimeChosen((Long.parseLong(sHour) * TimeConverter.getHourInMilSeconds()) + (Long.parseLong(sMin) * TimeConverter.getMinInMilSeconds()) + (Long.parseLong(sSecond) * TimeConverter.getSecondInMilSeconds()));
            timeoutManager.setTempTime(timeoutManager.getTimeChosen());
        } else {
            timeoutManager.setTimeChosen(timeoutManager.getTempTime());
        }

        Intent serviceIntent = new Intent(this, TimeoutService.class);
        serviceIntent.putExtra("Time", timeoutManager.getTimeChosen());
        startService(serviceIntent);

        stopButton.setText("Pause");
        timeoutManager.setTimerRunning(true);
        timeoutManager.setFirstState(false);
    }

    private void stopTimer() {
        Log.d("stopTimer", String.valueOf(timeoutManager.getTempTime()));
        stopService(new Intent(this, TimeoutService.class));
        timeoutManager.setTimerRunning(false);
        stopButton.setText("Resume");
    }

    private void cancelTimer() {
        stopService(new Intent(this, TimeoutService.class));
        timeoutManager.setTimerRunning(false);
        countdownText.setText("");
        switchSettingDisplay();

        removeNotification();
    }

    private void removeNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.cancelAll();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}