package ca.sfu.cmpt276.be.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpChildButton();
        setUpTimeoutButton();
    }

    private void setUpTimeoutButton() {
        Button button_timeout = findViewById(R.id.button3);
        button_timeout.setOnClickListener(view -> {
            Intent timeoutActivity = TimeoutActivity.makeIntent(MainActivity.this);
            startActivity(timeoutActivity);
        });
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