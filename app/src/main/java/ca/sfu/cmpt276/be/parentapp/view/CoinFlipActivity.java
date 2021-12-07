package ca.sfu.cmpt276.be.parentapp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.ChildManager;
import ca.sfu.cmpt276.be.parentapp.controller.CoinFlipManager;
import ca.sfu.cmpt276.be.parentapp.controller.DataManager;
import ca.sfu.cmpt276.be.parentapp.controller.ImageManager;
import ca.sfu.cmpt276.be.parentapp.databinding.ActivityCoinflipBinding;
import ca.sfu.cmpt276.be.parentapp.model.Child;

/**
 * Activity with animations to simulate a real coin flip
 * Sends and process info from CoinFLipManager
 */

public class CoinFlipActivity extends AppCompatActivity implements CoinFlipManager.CoinObserver {

    MediaPlayer player;
    CoinFlipManager coinFlipManager = new CoinFlipManager();
    boolean userOverride = false;

    ChildManager childManager = new ChildManager();
    Spinner spinner;
    ArrayAdapter<Child> adapter;
    ArrayList<Child> list;
    ArrayList<Child> coinFlipQueue = DataManager.getInstance().getCoinFlipQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCoinflipBinding binding = ActivityCoinflipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setupButton();
        initializeSpinner();
        setUpNavBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        coinFlipManager.registerChangeCallback(this);
        updateUIElements();
    }

    @Override
    protected void onPause() {
        super.onPause();
        coinFlipManager.unRegisterChangeCallback(this);
    }

    @Override
    public void notifyCounterChanged() {
        updateUIElements();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.coinflip, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.coinHistoryButton) {
            Intent intent = new Intent(getApplicationContext(), FlipHistoryActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void verifyListEmpty() {
        if (childManager.isEmpty()) {
            userOverride = true;
        }
    }

    private void initializeSpinner() {
        list = getSpinnerElements();
        spinner = findViewById(R.id.spinner_childQueue);
        adapter = new SpinnerAdapter(getApplicationContext(), list);
        adapter.setDropDownViewResource(R.layout.layout_standard);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerListener());
    }

    private void updateSpinner() {
        list = getSpinnerElements();
        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Child> getSpinnerElements() {
        ArrayList<Child> list = new ArrayList<>(coinFlipQueue);
        list.add(new Child("None"));
        return list;
    }

    private void setupButton() {
        findViewById(R.id.button_flip_coin).setVisibility(View.GONE);
        Button button_flipHeads = findViewById(R.id.button_heads);
        button_flipHeads.setOnClickListener(view ->
                flipCoin("Heads")
        );
        Button button_flipTails = findViewById(R.id.button_tails);
        button_flipTails.setOnClickListener(view ->
                flipCoin("Tails")
        );
    }

    private void flipCoin(String userChoice) {
        ImageView image_coin = findViewById(R.id.image_coin);
        Button tailsButton = findViewById(R.id.button_tails);
        Button headsButton = findViewById(R.id.button_heads);

        image_coin.animate().setDuration(3100).rotationXBy(2160).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                String result = coinFlipManager.flipRandomCoin(userChoice, userOverride);
                spinner.setSelection(0);
                switch (result) {
                    case "Heads":
                        image_coin.setImageResource(R.drawable.coin_heads);
                        break;
                    case "Tails":
                        image_coin.setImageResource(R.drawable.coin_tails);
                        break;
                }
                userOverride = false;
                setButtons(true, headsButton, tailsButton);
                verifyListEmpty();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                image_coin.setImageResource(R.drawable.coin_blur);
                player = MediaPlayer.create(CoinFlipActivity.this, R.raw.coin_sound);
                player.start();
                setButtons(false, headsButton, tailsButton);
            }

            private void setButtons(boolean b, Button button_heads, Button button_tails) {
                button_heads.setClickable(b);
                button_tails.setClickable(b);
            }
        });
    }

    private void updateUIElements() {
        TextView text_result = findViewById(R.id.text_flip_result);
        if (coinFlipManager.getCoinList().size() > 0) {
            text_result.setText(coinFlipManager.getCoinFlipGame(0).getResult());
        }
        updateSpinner();
    }

    private void setUpNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_coinflip);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            bottomNavigationView.postDelayed(() -> MainActivity.navigate(this, item, R.id.item_coinflip), 0);
            return true;
        });
    }

    private class SpinnerListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i < childManager.size()) {
                coinFlipManager.moveToFrontQueue(i);
                userOverride = false;
                spinner.setSelection(0);
            } else {
                userOverride = true;
            }
            verifyListEmpty();
            updateUIElements();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class SpinnerAdapter extends ArrayAdapter<Child> {
        public SpinnerAdapter(Context context, List<Child> childQueue) {
            super(context, R.layout.layout_standard, childQueue);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, parent);
        }

        public View getCustomView(int position, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View itemView = inflater.inflate(R.layout.layout_standard, parent, false);

            TextView text = itemView.findViewById(R.id.text_child);
            ImageView image = itemView.findViewById(R.id.image_child);

            text.setText(list.get(position).getName());
            ImageManager imageManager = new ImageManager();
            image.setImageBitmap(imageManager.getPortrait(CoinFlipActivity.this, list.get(position).getId()));

            return itemView;
        }
    }
}