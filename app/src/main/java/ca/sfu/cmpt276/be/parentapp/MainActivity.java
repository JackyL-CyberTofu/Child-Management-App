package ca.sfu.cmpt276.be.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpChildButton();
    }

    private void setUpChildButton() {
        Button toChildren = findViewById(R.id.button);
        toChildren.setOnClickListener(v -> {
            Intent childActivity = ChildListActivity.makeIntent(MainActivity.this);
            startActivity(childActivity);
        });

        Button button2 = findViewById(R.id.button4);

        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CoinFlipActivity.class);
            startActivity(intent);
        });
    }
}