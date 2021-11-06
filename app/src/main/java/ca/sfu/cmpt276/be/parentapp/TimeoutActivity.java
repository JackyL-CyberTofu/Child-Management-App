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
import android.widget.NumberPicker;
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

        NumberPicker numberPicker = findViewById(R.id.numberPicker);
        setupNumberPicker(numberPicker, 99);

        NumberPicker numberPicker2 = findViewById(R.id.numberPicker2);
        setupNumberPicker(numberPicker2, 60);

        NumberPicker numberPicker3 = findViewById(R.id.numberPicker3);
        setupNumberPicker(numberPicker3, 60);

        setupTimerShortcuts(numberPicker, numberPicker2, numberPicker3);

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


    private void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        if (firstState) {

            NumberPicker numberPicker = findViewById(R.id.numberPicker);
            NumberPicker numberPicker2 = findViewById(R.id.numberPicker2);
            NumberPicker numberPicker3 = findViewById(R.id.numberPicker3);

            String sHour = Integer.toString(numberPicker.getValue());
            String sMin = Integer.toString(numberPicker2.getValue());
            String sSecond = Integer.toString(numberPicker3.getValue());
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