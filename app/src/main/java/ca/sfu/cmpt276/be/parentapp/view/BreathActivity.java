package ca.sfu.cmpt276.be.parentapp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import ca.sfu.cmpt276.be.parentapp.R;


public class BreathActivity extends AppCompatActivity {
    private State currentState = new InhaleState(this);

    private State idleState = new IdleState(this);
    private State inhaleState = new InhaleState(this);
    private State exhaleState = new ExhaleState(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);

        setUpBreathButton();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpBreathButton() {
        Button breath = findViewById(R.id.button_breath);

        setState(idleState);
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

    protected void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }


    private abstract class State {
        public BreathActivity context;
        public Timer timer = new Timer();
        public State(BreathActivity context) {
            this.context = context;
        }

        public void handleEnter() {
            timer = new Timer();
        }

        public void handleExit() {
            try {
                timer.cancel();
            } catch (IllegalStateException e) {
                //this is fine it means the timer already stopped
                e.printStackTrace();
            }
        }

        public void handlePress() {
        }

        public void handleRelease() {
        }

        public BreathActivity getContext() {
            return context;
        }

    }

    private class IdleState extends State {

        public IdleState(BreathActivity context) {
            super(context);
        }

        @Override
        public void handleEnter() {
            super.handleEnter();
            context.setState(inhaleState);
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
            super.handleEnter();
            Button breathButton = findViewById(R.id.button_breath);
            breathButton.setText("Inhale");
        }

        @Override
        public void handleExit() {
            super.handleExit();
        }

        @Override
        public void handlePress() {
            super.handlePress();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        InhaleState.super.context.makeToast("Now you can breath out");
                    });

                }
            }, 3000);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        context.makeToast("You should really stop now");
                    });

                }
            }, 10000);
        }

        @Override
        public void handleRelease() {
            super.handleRelease();
            context.setState(exhaleState);
        }
    }



    private class ExhaleState extends State {
        public ExhaleState(BreathActivity context) {
            super(context);
        }

        @Override
        public void handleEnter() {
            super.handleEnter();
            Button breathButton = findViewById(R.id.button_breath);
            breathButton.setText("Exhale");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        ExhaleState.super.context.makeToast("Inhale now");
                        Button breathButton = findViewById(R.id.button_breath);
                        breathButton.setText("Inhale");
                        context.setState(inhaleState);
                    });
                }
            }, 3000);
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



