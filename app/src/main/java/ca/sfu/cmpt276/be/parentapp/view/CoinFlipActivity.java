package ca.sfu.cmpt276.be.parentapp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.databinding.ActivityCoinflipBinding;
import ca.sfu.cmpt276.be.parentapp.controller.ChildManager;
import ca.sfu.cmpt276.be.parentapp.controller.CoinFlipManager;

/**
Activity with animations to simulate a real coin flip
 Sends and process info from CoinFLipManager
 */

public class CoinFlipActivity extends AppCompatActivity implements CoinFlipManager.CoinObserver {

    MediaPlayer player;
    CoinFlipManager coinFlipManager = CoinFlipManager.getInstance();
    ChildManager childManager = new ChildManager();
    private AppBarConfiguration appBarConfiguration;
    private ActivityCoinflipBinding binding;
    private ImageView coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCoinflipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setupButton();
    }

    private void setupButton() {

        coin = findViewById(R.id.coin_image);

        Button flipButton = findViewById(R.id.flip_coin_button);
        flipButton.setOnClickListener(view ->
            flipCoin("Null")
        );

        Button headsButton = findViewById(R.id.heads_button);
        headsButton.setOnClickListener(view ->
            flipCoin("Heads")
        );

        Button tailsButton = findViewById(R.id.tails_button);
        tailsButton.setOnClickListener(view ->
            flipCoin("Tails")
        );

        TextView nextChild = findViewById(R.id.coin_next_child);

        if (!childNotEmpty()) {
            headsButton.setVisibility(View.GONE);
            tailsButton.setVisibility(View.GONE);
            nextChild.setVisibility(View.GONE);
        } else {
            flipButton.setVisibility(View.GONE);

        }

    }

    private void flipCoin(String userChoice) {
        coin.animate().setDuration(3100).rotationXBy(2160).setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                String result = coinFlipManager.flipRandomCoin(userChoice);
                switch (result) {
                    case "Heads":
                        coin.setImageResource(R.drawable.coin_heads);
                        break;
                    case "Tails":
                        coin.setImageResource(R.drawable.coin_tails);
                        break;
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                coin.setImageResource(R.drawable.coin_blur);
                player = MediaPlayer.create(CoinFlipActivity.this, R.raw.coin_sound);
                player.start();
            }
        });
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

    private void updateDisplayCoinResult() {

        TextView tv = findViewById(R.id.flip_result);
        if (coinFlipManager.getCoinList().size() > 0) {
            tv.setText(coinFlipManager.getCoinFlipGame(0).getResult());
        }

        TextView nextChild = findViewById(R.id.coin_next_child);
        if (childNotEmpty()) {
            if (coinFlipManager.getChildIndex() >= childManager.size()) {
                Toast.makeText(this, "Child Deleted. Order is reset.", Toast.LENGTH_SHORT).show();
                coinFlipManager.setChildIndex(0);
            }
            nextChild.setText(String.format("%s%s", getString(R.string.next_child), childManager.get(coinFlipManager.getChildIndex()).getName()));
        }

    }

    private boolean childNotEmpty() {
        return !childManager.isEmpty();
    }

    @Override
    protected void onResume() {
        super.onResume();
        coinFlipManager.registerChangeCallback(this);
        updateDisplayCoinResult();
    }

    @Override
    protected void onPause() {
        super.onPause();
        coinFlipManager.unRegisterChangeCallback(this);
    }

    @Override
    public void notifyCounterChanged() {
        updateDisplayCoinResult();
    }
}