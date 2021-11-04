package ca.sfu.cmpt276.be.parentapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import ca.sfu.cmpt276.be.parentapp.databinding.ActivityCoinflipBinding;
import ca.sfu.cmpt276.be.parentapp.model.CoinFlipManager;

public class CoinFlipActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityCoinflipBinding binding;

    private ImageView coin;
    MediaPlayer player;
    CoinFlipManager coinFlipManager = CoinFlipManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCoinflipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        initializeCoin();
        setupButton();

    }

    private void setupButton() {
        Button flipButton = (Button) findViewById(R.id.flipCoinButton);
        flipButton.setOnClickListener(view -> {
            flipCoin("Null");
        });

        Button headsButton = (Button) findViewById(R.id.headsButton);
        headsButton.setOnClickListener(view -> {
            flipCoin("Heads");
        });

        Button tailsButton = (Button) findViewById(R.id.tailsButton);
        tailsButton.setOnClickListener(view -> {
            flipCoin("Tails");
        });

    }

    private void initializeCoin() {
        coin = findViewById(R.id.coin_image);
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
                Toast.makeText(CoinFlipActivity.this,"result "+result+" at "+ coinFlipManager.getCoinFlipGame(0).getDate(),Toast.LENGTH_SHORT).show();
                int won = coinFlipManager.getCoinFlipGame(0).getPickerWon();
                switch (won) {
                    case 1:
                        Toast.makeText(CoinFlipActivity.this,"You won",Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(CoinFlipActivity.this,"you lost" ,Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                coin.setImageResource(R.drawable.coin_blur);
                player = MediaPlayer.create(CoinFlipActivity.this,R.raw.coin_sound);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.coinHistoryButton){
            Intent intent = new Intent(getApplicationContext(), FlipHistoryActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}