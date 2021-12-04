package ca.sfu.cmpt276.be.parentapp.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.DataManager;


public class BreathActivity extends AppCompatActivity {
    public static final int EXHALE_TIME = 3000;
    public static final int INHALE_TIME = 3000;
    public static final int INHALE_TIME_MAX = 10000;
    public static final int EXHALE_TIME_MAX = 10000;
    public static final int DEFAULT_BREATHS = 1;
    public static final int FADE_ANIMATION_TIME = 600;
    public static final int MAX_BREATHS = 10;
    public static final int SHADOW_SCALE = 2;

    private State currentState = new InhaleState(this);

    private final State beginState = new BeginState(this);
    private final State inhaleState = new InhaleState(this);
    private final State exhaleState = new ExhaleState(this);

    private int breaths = DEFAULT_BREATHS;

    DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_breath_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpBreathButton();
        setUpConfigButtons();
        setUpBreathField();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataManager.serializeBreaths();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.coinflip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.coinHistoryButton) {
            FragmentManager manager = getSupportFragmentManager();
            HistoryFragment history = new HistoryFragment();
            history.show(manager, "HistoryDialog");
        }
        return super.onOptionsItemSelected(item);
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

    @SuppressLint("SetTextI18n")
    private void setBreathCount() {
        TextView breathCount = findViewById(R.id.field_breaths);
        if (breathCount.getText().toString().isEmpty()) {
            breaths = 1;
        } else {
            breaths = Integer.parseInt(breathCount.getText().toString());
        }

        if (breaths > MAX_BREATHS) {
            breathCount.setText("" + MAX_BREATHS);
        }

    }

    @SuppressLint("SetTextI18n")
    private void addBreathCount() {
        TextView breathCount = findViewById(R.id.field_breaths);
        int currentBreath = Integer.parseInt(breathCount.getText().toString());
        if (currentBreath <= MAX_BREATHS) {
            breaths = (Integer.parseInt(breathCount.getText().toString()));
            breaths++;
        }
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

    private void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    private void setLabel(int labelId) {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);

        fadeOut.setDuration(FADE_ANIMATION_TIME);
        fadeIn.setDuration(FADE_ANIMATION_TIME);

        TextView labelText = findViewById(R.id.text_directions);
        labelText.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                labelText.setText(labelId);
                labelText.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setButtonText(int stringId) {
        Button breathButton = findViewById(R.id.button_breath);
        breathButton.setText(stringId);
    }

    private void cancelAnimation() {
        ImageView breathCircle = findViewById(R.id.image_breath_circle);
        breathCircle.animate().cancel();
    }

    private void doInhaleAnimation() {
        ImageView breathCircle = findViewById(R.id.image_breath_circle);
        ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(breathCircle,
                PropertyValuesHolder.ofFloat("scaleX", SHADOW_SCALE),
                PropertyValuesHolder.ofFloat("scaleY", SHADOW_SCALE));
        animation.setDuration(EXHALE_TIME);
        animation.start();
    }

    private void doExhaleAnimation() {
        ImageView breathCircle = findViewById(R.id.image_breath_circle);

        ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(breathCircle,
                PropertyValuesHolder.ofFloat("scaleX", 1),
                PropertyValuesHolder.ofFloat("scaleY", 1));
        animation.setDuration(EXHALE_TIME);
        animation.start();

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

    private abstract static class State {
        public BreathActivity context;
        public Timer timer = new Timer();

        public State(BreathActivity context) {
            this.context = context;
        }

        public void handleEnter() {
        }

        public void handleExit() {
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
            setButtonText(R.string.button_breath_begin_label);
            setLabel(R.string.text_breath_help_begin);
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

    private class InhaleState extends State {
        private boolean doGoExhale = false;
        public InhaleState(BreathActivity context) {
            super(context);
        }

        @Override
        public void handleEnter() {
            super.handleEnter();
            setButtonText(R.string.button_breath_inhale_label);
            setLabel(R.string.text_breath_help_inhale_start);
            manageGroupVisibility(View.INVISIBLE);
        }

        @Override
        public void handleExit() {
            timer.cancel();
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
                        setLabel(R.string.text_breath_help_inhale_finish);
                        doGoExhale = true;
                    });

                }
            }, INHALE_TIME);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> setLabel(R.string.text_breath_help_inhale_long));
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
                timer.cancel();
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
            doExhaleAnimation();
            setButtonText(R.string.button_breath_exhale_label);
            setLabel(R.string.text_breath_help_exhale_begin);

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        dataManager.incrementBreath();
                        if (breaths == 1) {
                            setState(beginState);
                            setButtonText(R.string.button_breath_finish_label);
                        } else {
                            context.setState(inhaleState);
                            setLabel(R.string.text_breath_help_exhale_end);
                            minusBreathCount();
                        }
                    });
                }
            }, EXHALE_TIME);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> BreathActivity.this.cancelAnimation());
                }
            }, EXHALE_TIME_MAX);
        }

        @Override
        public void handleExit() {
            timer.cancel();
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



