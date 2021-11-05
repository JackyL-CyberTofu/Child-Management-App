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


        startButton.setOnClickListener(v -> {
            firstState = true;

            setting.setVisibility(View.GONE);
            timer.setVisibility(View.VISIBLE);
            startStop();
        });

        stopButton.setOnClickListener(v -> startStop());

        cancelButton.setOnClickListener(v -> {
            setting.setVisibility(View.VISIBLE);
            timer.setVisibility(View.GONE);
            firstState = true;
            stopTimer();
        });

        updateTimer();
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
            time = (Long.parseLong(sHour) * 3600000) + (Long.parseLong(sMin) * 60000) + (Long.parseLong(sSecond) * 1000) + 1000;
        } else {
            time = tempTime;
        }

        countDownTimer = new CountDownTimer(time, 1000) {
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

        int hour = (int) tempTime / 3600000;
        int minutes = (int) tempTime % 3600000 / 60000;
        int seconds = (int) tempTime % 3600000 % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + hour + ":";

        if (minutes < 10) timeLeftText += "0";
        timeLeftText += minutes + ":";

        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
    }
}