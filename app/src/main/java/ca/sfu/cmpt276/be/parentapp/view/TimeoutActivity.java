package ca.sfu.cmpt276.be.parentapp.view;

/*
 * TimeoutActivity represents a feature of countdown timer in the app.
 * Users can set their customized time. When user start the timer, it is working on the TimeoutService.
 */

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.AlarmService;
import ca.sfu.cmpt276.be.parentapp.controller.TimeoutManager;
import ca.sfu.cmpt276.be.parentapp.controller.TimeoutService;
import ca.sfu.cmpt276.be.parentapp.model.SpeedRate;
import ca.sfu.cmpt276.be.parentapp.model.TimeConverter;

/**
 * TimeoutActivity manages the timeout screen in the app.
 */
public class TimeoutActivity extends AppCompatActivity {
    public static final String String_Speed_Rate = "Speed Rate : ";
    private ConstraintLayout setting;
    private ConstraintLayout timer;
    private TextView countdownText;
    private Button startButton;
    private Button stopButton;
    private Button cancelButton;
    private ProgressBar progressBar;
    private SeekBar seekBar;
    private TimeoutManager timeoutManager;

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutActivity.class);
    }

    // Broadcast Receiver from TimeoutService when the time is ticking
    // It updates UI on the TimeoutActivity(How much time is left)
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch(action) {
                case "TIME_TICKED":
                    updateGUI(intent);
                    break;
                case "TIME_OUT":
                    switchSettingDisplay();
                    popUpAlarmTurningOffDialog();
                    break;
                case "NOTIFICATION_CLICKED":
                    Log.d("Notification", "Clicked and now in activity");
                    popUpAlarmTurningOffDialog();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createIntentFilterAndRegisterReceiver();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);

        timeoutManager = TimeoutManager.getInstance();
        assignViewComponents();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NumberPicker numberPicker = findViewById(R.id.number_picker_hour);
        setupNumberPicker(numberPicker, 99);

        NumberPicker numberPicker2 = findViewById(R.id.number_picker_min);
        setupNumberPicker(numberPicker2, 60);

        NumberPicker numberPicker3 = findViewById(R.id.number_picker_sec);
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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SpeedRate rateChosen = SpeedRate.values()[i];

                // Update speed rate text in the activity
                updateTextOfSpeedRate(rateChosen);

                // Kill Service First
                stopTimer();

                // Create new service depending no the speed.
                if(!timeoutManager.isFirstState()){
                    Log.d("get Temp Time",String.valueOf((long) (TimeConverter.getRelativeTimeLeft(timeoutManager.getCurrentRate(), rateChosen)) * timeoutManager.getTempTime()));
                    long tempTimeConverted = (long) (TimeConverter.getRelativeTimeLeft(timeoutManager.getCurrentRate(), rateChosen) * timeoutManager.getTempTime());
                    timeoutManager.setCurrentRate(rateChosen);
                    startServiceSpeedRateChanged(tempTimeConverted);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (timeoutManager.isTimerRunning() || timeoutManager.isPauseClicked()) {
            switchTimerDisplay();
            setProgressBar();

            long timeLeftConverted = (long) TimeConverter.getRelativeTimeLeft(timeoutManager.getTempTime(), timeoutManager.getCurrentRate());
            updateProgressBar(timeLeftConverted);
        } else {
            switchSettingDisplay();
        }

        if (timeoutManager.isAlarmRunning()) {
            popUpAlarmTurningOffDialog();
        }
        setUpNavBar();
    }

    private void updateTextOfSpeedRate(SpeedRate rateChosen) {
        TextView textView = findViewById(R.id.text_speed_rate);
        String rateInText = String_Speed_Rate + rateChosen.toString();
        textView.setText(rateInText);
    }

    private void startServiceSpeedRateChanged(long tempTimeConverted) {
        Intent serviceIntent = new Intent(getApplicationContext(), TimeoutService.class);
        serviceIntent.setAction("START_TIMING");
        serviceIntent.putExtra("Time", tempTimeConverted);
        startService(serviceIntent);

        stopButton.setText(R.string.button_stop_time);
        timeoutManager.setTimerRunning(true);
        timeoutManager.setFirstState(false);
        timeoutManager.setPauseClicked(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        seekBar.setProgress(timeoutManager.getCurrentRate().ordinal());

        Long timeLeftConverted = timeoutManager.getTempTime();
        timeLeftConverted = (long) TimeConverter.getRelativeTimeLeft(timeLeftConverted, timeoutManager.getCurrentRate());
        String updatedTime = TimeConverter.toStringForMilSeconds(timeLeftConverted +
                TimeConverter.getSecondInMilSeconds());
        countdownText.setText(updatedTime);

        if (timeoutManager.isTimerRunning() || timeoutManager.isPauseClicked()) {
            switchTimerDisplay();
        } else {
            switchSettingDisplay();
        }
        createIntentFilterAndRegisterReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    private void popUpAlarmTurningOffDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TimeoutActivity.this);
        builder.setTitle("Time's up!");
        builder.setMessage("You can turn off the alarm by clicking Yes button");
        builder.setIcon(R.drawable.ic_baseline_timer_24);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            stopService(new Intent(getApplicationContext(), AlarmService.class));
            removeNotifications();
            TimeoutManager timeoutManager = TimeoutManager.getInstance();
            timeoutManager.setAlarmRunning(false);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void createIntentFilterAndRegisterReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("TIME_TICKED");
        intentFilter.addAction("TIME_OUT");
        intentFilter.addAction("NOTIFICATION_CLICKED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @SuppressLint("DefaultLocale")
    private void setupNumberPicker(NumberPicker numberPicker, int i2) {
        numberPicker.setFormatter(i -> {
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(i2);
            return String.format("%02d", i);
        });
    }

    private void setupTimerShortcuts(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        Button oneButton = findViewById(R.id.one_minute_timer);
        oneButton.setOnClickListener(view -> {
            numberPicker2.setValue(1);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });

        Button twoButton = findViewById(R.id.two_minute_timer);
        twoButton.setOnClickListener(view -> {
            numberPicker2.setValue(2);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });

        Button threeButton = findViewById(R.id.three_minute_timer);
        threeButton.setOnClickListener(view -> {
            numberPicker2.setValue(3);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });

        Button fiveButton = findViewById(R.id.five_minute_timer);
        fiveButton.setOnClickListener(view -> {
            numberPicker2.setValue(5);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });

        Button tenButton = findViewById(R.id.ten_minute_timer);
        tenButton.setOnClickListener(view -> {
            numberPicker2.setValue(10);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });

        Button fifteenButton = findViewById(R.id.fifteen_minute_timer);
        fifteenButton.setOnClickListener(view -> {
            numberPicker2.setValue(15);
            numberPicker.setValue(0);
            numberPicker3.setValue(0);
        });
    }

    private void updateGUI(Intent intent) {
        Bundle bundle = intent.getExtras();
        long timeLeftConverted = (long) bundle.get("TimeLeft");
        timeoutManager.setTempTime(timeLeftConverted);
        timeLeftConverted = (long) TimeConverter.getRelativeTimeLeft(timeLeftConverted, timeoutManager.getCurrentRate());
        String updatedTime = TimeConverter.toStringForMilSeconds(timeLeftConverted +
                TimeConverter.getSecondInMilSeconds());
        countdownText.setText(updatedTime);
        updateProgressBar(timeLeftConverted);
    }

    private void switchSettingDisplay() {
        setting.setVisibility(View.VISIBLE);
        timer.setVisibility(View.GONE);
    }

    private void switchTimerDisplay() {
        setting.setVisibility(View.GONE);
        timer.setVisibility(View.VISIBLE);
    }

    private void setProgressBar(){
        progressBar.setMax((int) timeoutManager.getTimeChosen());
        progressBar.setProgress((int) timeoutManager.getTimeChosen());
    }

    private void updateProgressBar(long timeLeftConverted){
        progressBar.setProgress((int) timeLeftConverted);
    }

    private void assignViewComponents() {
        countdownText = findViewById(R.id.text_view_countdown);
        startButton = findViewById(R.id.button_start_countdown);
        stopButton = findViewById(R.id.button_pause_timer);
        cancelButton = findViewById(R.id.button_cancel_timer);
        progressBar = findViewById(R.id.progress_bar_timer);
        seekBar = findViewById(R.id.seek_bar_speed_rate);

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
            seekBar.setProgress(timeoutManager.getCurrentRate().ordinal());
            NumberPicker numberPicker = findViewById(R.id.number_picker_hour);
            NumberPicker numberPicker2 = findViewById(R.id.number_picker_min);
            NumberPicker numberPicker3 = findViewById(R.id.number_picker_sec);

            String sHour = Integer.toString(numberPicker.getValue());
            String sMin = Integer.toString(numberPicker2.getValue());
            String sSecond = Integer.toString(numberPicker3.getValue());
            timeoutManager.setTimeChosen((Long.parseLong(sHour) * TimeConverter.getHourInMilSeconds()) +
                    (Long.parseLong(sMin) * TimeConverter.getMinInMilSeconds()) +
                    (Long.parseLong(sSecond) * TimeConverter.getSecondInMilSeconds()));
            timeoutManager.setTempTime(timeoutManager.getTimeChosen());
            setProgressBar();
        }

        startServiceSpeedRateChanged(timeoutManager.getTempTime());
    }

    private void stopTimer() {
        stopService(new Intent(getApplicationContext(), TimeoutService.class));
        timeoutManager.setTimerRunning(false);
        timeoutManager.setPauseClicked(true);
        stopButton.setText(R.string.button_resume_time);
    }

    private void cancelTimer() {
        stopService(new Intent(getApplicationContext(), TimeoutService.class));
        timeoutManager.setTimerRunning(false);
        timeoutManager.setPauseClicked(false);
        timeoutManager.setCurrentRate(SpeedRate.HUNDRED);
        seekBar.setProgress(timeoutManager.getCurrentRate().ordinal());
        countdownText.setText("");
        switchSettingDisplay();
        removeNotification();
        Log.d("Cancel Timer", String.valueOf(timeoutManager.getTempTime()));
    }

    private void removeNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.cancelAll();
    }

    private void setUpNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_timeout);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            bottomNavigationView.postDelayed(() -> MainActivity.navigate(this, item, R.id.item_timeout), 0);
            return true;
        });
    }
}