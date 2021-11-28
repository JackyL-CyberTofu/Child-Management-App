package ca.sfu.cmpt276.be.parentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;

import ca.sfu.cmpt276.be.parentapp.R;


public class BreathActivity extends AppCompatActivity {
    private State currentState = new IdleState(this);

    private final long TIME_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);

        setUpBreathButton();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpBreathButton() {
        Button breath = findViewById(R.id.button_breath);
        breath.setOnTouchListener((v, event) -> {
            v.performClick();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                currentState.handlePress();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                currentState.handleRelease();
            }

            return true;
        });
    }


    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }


    private abstract class State {
        private BreathActivity context;
        public State(BreathActivity context) {
            this.context = context;
        }

        public void handleEnter() {}

        public void handleExit() {}

        public void handlePress() {
            Toast.makeText(context, "I have been pressed", Toast.LENGTH_SHORT).show();
        }

        public void handleRelease() {
            Toast.makeText(context, "I have been released", Toast.LENGTH_SHORT).show();
        }

    }

    private class IdleState extends State {
        public IdleState(BreathActivity context) {
            super(context);
        }

        @Override
        public void handleEnter() {
            super.handleEnter();
        }

        @Override
        public void handleExit() {
            super.handleExit();
        }

        @Override
        public void handlePress() {
            super.handlePress();
        }

        @Override
        public void handleRelease() {
            super.handleRelease();
        }
    }

    private class InhaleState extends State {
        public InhaleState(BreathActivity context) {
            super(context);
        }

        @Override
        public void handleEnter() {
        }

        @Override
        public void handleExit() {
            super.handleExit();
        }

        @Override
        public void handlePress() {

        }

        @Override
        public void handleRelease() {
            super.handleRelease();
        }
    }

    private class ExhaleState extends State {
        public ExhaleState(BreathActivity context) {
            super(context);
        }

        @Override
        public void handleEnter() {
            super.handleEnter();
        }

        @Override
        public void handleExit() {
            super.handleExit();
        }

        @Override
        public void handlePress() {
            super.handlePress();
        }

        @Override
        public void handleRelease() {
            super.handleRelease();
        }
    }
}



