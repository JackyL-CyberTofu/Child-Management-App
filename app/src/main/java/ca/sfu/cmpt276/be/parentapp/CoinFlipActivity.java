package ca.sfu.cmpt276.be.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import ca.sfu.cmpt276.be.parentapp.model.CoinFlipManager;

public class CoinFlipActivity extends AppCompatActivity {

    private ImageView coin;
    MediaPlayer player;

    CoinFlipManager coinFlipManager = CoinFlipManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conflip);

        initializeCoin();
        setupButton();
    }

    private void setupButton() {
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CoinFlipHistory.class);
            startActivity(intent);
        });
    }

    private void initializeCoin() {
        coin = findViewById(R.id.coin_image);
        coin.setOnClickListener(v -> flipCoin());
    }

    private void flipCoin() {
        coin.animate().setDuration(3100).rotationXBy(2160).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                String result = coinFlipManager.flipRandomCoin("Heads");
                switch (result) {
                    case "Heads":
                        coin.setImageResource(R.drawable.coin_heads);
                        break;
                    case "Tails":
                        coin.setImageResource(R.drawable.coin_tails);
                        break;
                }
                Toast.makeText(CoinFlipActivity.this,"result "+result+" at "+ coinFlipManager.getCoinFlipGame(0).getDate(),Toast.LENGTH_SHORT).show();
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
}