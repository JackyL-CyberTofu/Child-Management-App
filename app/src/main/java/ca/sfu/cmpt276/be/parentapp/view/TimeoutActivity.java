package ca.sfu.cmpt276.be.parentapp.view;

/*
 * TimeoutActivity represents a feature of countdown timer in the app.
 * Users can set their customized time. When user start the timer, it is working on the TimeoutService.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;

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
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.controller.AlarmService;
import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.TimeoutService;
import ca.sfu.cmpt276.be.parentapp.model.TimeConverter;
import ca.sfu.cmpt276.be.parentapp.controller.TimeoutManager;

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


    // Broadcast Receiver from TimeouService when the time is ticking
    // It updates UI on the TimeoutActivity(How much time is left)

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Braodcast Received", String.valueOf(intent.getAction()));
            String action = intent.getAction();
            switch(action) {
                case "TIME_TICKED":
                    Log.i("Ticking", String.valueOf(intent.getAction()));
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

    private void removeNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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

        NumberPicker numberPicker = findViewById(R.id.number_picker);
        setupNumberPicker(numberPicker, 99);

        NumberPicker numberPicker2 = findViewById(R.id.number_picker_2);
        setupNumberPicker(numberPicker2, 60);

        NumberPicker numberPicker3 = findViewById(R.id.number_picker_3);
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

        if (timeoutManager.isTimerRunning()) {
            switchTimerDisplay();
        } else {
            switchSettingDisplay();
        }

        if (timeoutManager.isAlarmRunning()) {
            popUpAlarmTurningOffDialog();
        }

        setUpNavBar();
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
        countdownText = findViewById(R.id.text_view_countdown);
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
            NumberPicker numberPicker = findViewById(R.id.number_picker);
            NumberPicker numberPicker2 = findViewById(R.id.number_picker_2);
            NumberPicker numberPicker3 = findViewById(R.id.number_picker_3);

            String sHour = Integer.toString(numberPicker.getValue());
            String sMin = Integer.toString(numberPicker2.getValue());
            String sSecond = Integer.toString(numberPicker3.getValue());
            timeoutManager.setTimeChosen((Long.parseLong(sHour) * TimeConverter.getHourInMilSeconds()) + (Long.parseLong(sMin) * TimeConverter.getMinInMilSeconds()) + (Long.parseLong(sSecond) * TimeConverter.getSecondInMilSeconds()));
            timeoutManager.setTempTime(timeoutManager.getTimeChosen());
        } else {
            timeoutManager.setTimeChosen(timeoutManager.getTempTime());
        }

        Intent serviceIntent = new Intent(this, TimeoutService.class);
        serviceIntent.setAction("START_TIMING");
        serviceIntent.putExtra("Time", timeoutManager.getTimeChosen());
        startService(serviceIntent);

        stopButton.setText(R.string.pause);
        timeoutManager.setTimerRunning(true);
        timeoutManager.setFirstState(false);
    }

    private void stopTimer() {
        Log.d("stopTimer", String.valueOf(timeoutManager.getTempTime()));
        stopService(new Intent(this, TimeoutService.class));
        timeoutManager.setTimerRunning(false);
        stopButton.setText(R.string.resume);
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

    private void setUpNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_timeout);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            bottomNavigationView.postDelayed(() -> {
                int id = item.getItemId();
                if (id == R.id.item_home){
                    finish();
                    overridePendingTransition(0, 0);
                } else if (id == R.id.item_tasks){
                    startActivity(new Intent(getApplicationContext(), TaskListActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                } else if (id == R.id.item_child){
                    startActivity(new Intent(getApplicationContext(), ChildListActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                } else if (id == R.id.item_coinflip){
                    startActivity(new Intent(getApplicationContext(), CoinFlipActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
            },0);
            return true;
        });
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