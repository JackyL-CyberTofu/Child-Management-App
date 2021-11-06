package ca.sfu.cmpt276.be.parentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TimeoutActivity extends AppCompatActivity {

    public static final int HOUR_CONVERTER_FOR_MILSECONDS = 3600000;
    public static final int MIN_CONVERTER_FOR_MILSECONDS = 60000;
    public static final int SECONDS_CONVERTER_FORMILSECONDS = 1000;
    public static final int COUNT_DOWN_INTERVAL = 1000;
    public static final int ONESECOND_IN_MILSECONDS = 1000;
    private TextView countdownText;
    private Button startButton;
    private Button stopButton;
    private Button cancelButton;

    private EditText hourText;
    private EditText minText;
    private EditText secondText;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;
    private boolean firstState;

    private long time = 0;
    private long tempTime = 0;

    ConstraintLayout setting;
    ConstraintLayout timer;


    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);

        assignViewComponents();



        startButton.setOnClickListener(v -> {
            firstState = true;
            switchTimerDisplay();
            startStop();
        });

        stopButton.setOnClickListener(v -> startStop());


        cancelButton.setOnClickListener(v -> {
            switchSettingDisplay();
            firstState = true;
            stopTimer();
        });

        updateTimer();
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

        hourText = findViewById(R.id.editText_hour);
        minText = findViewById(R.id.editText_min);
        secondText = findViewById(R.id.editText_second);

        setting = findViewById(R.id.setting);
        timer = findViewById(R.id.timer);

        timer.setVisibility(View.GONE);
    }


    private void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        if (firstState) {
            String sHour = hourText.getText().toString();
            String sMin = minText.getText().toString();
            String sSecond = secondText.getText().toString();
            time = (Long.parseLong(sHour) * HOUR_CONVERTER_FOR_MILSECONDS) + (Long.parseLong(sMin) * MIN_CONVERTER_FOR_MILSECONDS) + (Long.parseLong(sSecond) * SECONDS_CONVERTER_FORMILSECONDS) + ONESECOND_IN_MILSECONDS;
        } else {
            time = tempTime;
        }

        countDownTimer = new CountDownTimer(time, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                tempTime = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
            }
        }.start();

        stopButton.setText("Pause");
        timerRunning = true;
        firstState = false;
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        stopButton.setText("Resume");
    }

    private void updateTimer() {

        int hour = (int) tempTime / HOUR_CONVERTER_FOR_MILSECONDS;
        int minutes = (int) tempTime % HOUR_CONVERTER_FOR_MILSECONDS / MIN_CONVERTER_FOR_MILSECONDS;
        int seconds = (int) tempTime % HOUR_CONVERTER_FOR_MILSECONDS % MIN_CONVERTER_FOR_MILSECONDS / SECONDS_CONVERTER_FORMILSECONDS;

        StringBuilder stringBuilderTimeLeft = new StringBuilder();

        stringBuilderTimeLeft.append("");
        stringBuilderTimeLeft.append(hour);
        stringBuilderTimeLeft.append(":");

        if (minutes < 10) {
            stringBuilderTimeLeft.append("0");
        }
        stringBuilderTimeLeft.append(minutes);
        stringBuilderTimeLeft.append(":");

        if (seconds < 10) {
            stringBuilderTimeLeft.append("0");
        }
        stringBuilderTimeLeft.append(seconds);

        countdownText.setText(stringBuilderTimeLeft.toString());
    }
}