package ca.sfu.cmpt276.be.parentapp.view;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import org.w3c.dom.Text;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import ca.sfu.cmpt276.be.parentapp.R;


public class BreathActivity extends AppCompatActivity {
    public static final int EXHALE_TIME = 3000;
    public static final int INHALE_TIME = 3000;
    public static final int INHALE_TIME_MAX = 10000;
    public static final int EXHALE_TIME_MAX = 10000;
    public static final int DEFAULT_BREATHS = 3;

    private State currentState = new InhaleState(this);

    private final State beginState = new BeginState(this);
    private final State inhaleState = new InhaleState(this);
    private final State exhaleState = new ExhaleState(this);

    private int breaths = DEFAULT_BREATHS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Breath Helper");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpBreathButton();
        setUpConfigButtons();
        setUpBreathField();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void setUpBreathField() {
        TextView breathField = findViewById(R.id.field_breaths);
        breathField.setText(""+DEFAULT_BREATHS);
        breathField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {setBreathCount();}

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setUpConfigButtons() {
        ImageButton decreaseButton = findViewById(R.id.button_decrease_breath);
        ImageButton increaseButton = findViewById(R.id.button_increase_breath);

        decreaseButton.setOnClickListener(v -> minusBreathCount());
        increaseButton.setOnClickListener(v -> addBreathCount());
    }

    @SuppressLint("SetTextI18n")
    private void setBreathCount() {
        TextView breathCount = findViewById(R.id.field_breaths);
        if (breathCount.getText().toString().isEmpty()) {
            breaths = 1;
        } else {
            breaths = Integer.parseInt(breathCount.getText().toString());
        }

    }

    @SuppressLint("SetTextI18n")
    private void addBreathCount() {
        TextView breathCount = findViewById(R.id.field_breaths);
        breaths = (Integer.parseInt(breathCount.getText().toString()) + 1);
        breathCount.setText("" + breaths);
    }

    @SuppressLint("SetTextI18n")
    private void minusBreathCount() {
        TextView breathCount = findViewById(R.id.field_breaths);
        int currentBreath = Integer.parseInt(breathCount.getText().toString());
        if (currentBreath > 1) {
            breaths = (Integer.parseInt(breathCount.getText().toString()));
            breaths--;
        }
        breathCount.setText("" + breaths);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpBreathButton() {
        Button breath = findViewById(R.id.button_breath);

        setState(beginState);
        breath.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                breath.performClick();
                currentState.handlePress();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                breath.getBackground().clearColorFilter();
                currentState.handleRelease();
            }

            return true;
        });
    }

    private void setLabel(String label) {
        TextView labelText = findViewById(R.id.text_directions);
        labelText.setText("" + label);
    }



    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    private void cancelAnimation() {
    }

    private void doInhaleAnimation() {
    }

    private void doExhaleAnimation() {
    }

    private void stopAnimation() {
    }

    private void stopInhaleAnimation() {
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

    private class BeginState extends State {

        public BeginState(BreathActivity context) {
            super(context);
        }

        @Override
        public void handleEnter() {
            super.handleEnter();
            Button breathButton = findViewById(R.id.button_breath);
            breathButton.setText("Begin");
            setLabel("Select number of breaths and press the button to start");

            manageGroupVisibility(View.VISIBLE);
        }

        @Override
        public void handleExit() {
            super.handleExit();
        }

        @Override
        public void handlePress() {
            super.handlePress();
            context.setState(inhaleState);
            context.currentState.handlePress();
        }

        @Override
        public void handleRelease() {
            super.handleRelease();
        }
    }

    private void manageGroupVisibility(int visibleType) {
        ImageButton upButton = findViewById(R.id.button_increase_breath);
        ImageButton downButton = findViewById(R.id.button_decrease_breath);

        upButton.setVisibility(visibleType);
        downButton.setVisibility(visibleType);

        EditText breathField = findViewById(R.id.field_breaths);
        TextView bottomText = findViewById(R.id.text_breaths_label_bot);
        TextView topText = findViewById(R.id.text_breaths_label_top);


        if (visibleType == View.INVISIBLE) {
            breathField.setEnabled(false);
            bottomText.setText(R.string.text_breath_picker_bottom_active);
            topText.setText(R.string.text_breath_picker_top_active);
        } else {
            breathField.setEnabled(true);
            bottomText.setText(R.string.text_breath_picker_bottom_idle);
            topText.setText(R.string.text_breath_picker_top_idle);
        }
    }


    private class InhaleState extends State {
        private boolean doGoExhale = false;
        public InhaleState(BreathActivity context) {
            super(context);
        }

        @Override
        public void handleEnter() {
            super.handleEnter();
            Button breathButton = findViewById(R.id.button_breath);
            breathButton.setText("Inhale");

            setLabel("Hold down the button and breath in");

            manageGroupVisibility(View.INVISIBLE);
        }

        @Override
        public void handleExit() {
            super.handleExit();
        }

        @Override
        public void handlePress() {
            super.handlePress();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        setLabel("Breath out and let go of the button");
                        doGoExhale = true;
                    });

                }
            }, INHALE_TIME);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> setLabel("You should really breath out and let go of the button now"));
                    stopInhaleAnimation();
                }
            }, INHALE_TIME_MAX);

            doInhaleAnimation();
        }

        @Override
        public void handleRelease() {
            super.handleRelease();
            if (doGoExhale) {
                context.setState(exhaleState);
                cancelAnimation();
            } else {
                try {
                    timer.cancel();
                } catch (IllegalStateException e) {
                    //do nothing as this is fine
                }
            }
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

            doExhaleAnimation();

            setLabel("Exhale slowly");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        if (breaths == 1) {
                            setState(beginState);
                            Button breathButton = findViewById(R.id.button_breath);
                            breathButton.setText("Good Job!");
                        } else {
                            setLabel("Hold down the button and breath in");
                            Button breathButton = findViewById(R.id.button_breath);
                            breathButton.setText("Inhale");
                            context.setState(inhaleState);
                            minusBreathCount();
                        }
                    });
                }
            }, EXHALE_TIME);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        stopAnimation();
                    });
                }
            }, EXHALE_TIME_MAX);
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



