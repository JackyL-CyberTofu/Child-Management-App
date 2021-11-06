package ca.sfu.cmpt276.be.parentapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.cmpt276.be.parentapp.model.DataManager;

public class MainActivity extends AppCompatActivity {
    SharedPreferences saveSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpSaving();
        loadData();
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

    private void setUpSaving() {
        DataManager dataManager = DataManager.getInstance();
        dataManager.setSaveOption(new DataManager.SaveManager() {
            @Override
            public String load(String saveName) {
                saveSP = getApplicationContext().getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                return saveSP.getString(saveName, "");
            }

            @Override
            public void save(String saveName, String saveJson) {
                saveSP = getApplicationContext().getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveSP.edit();
                editor.putString(saveName, saveJson);
                editor.apply();
            }
        });
    }

    private void loadData() {
        DataManager.getInstance().deserializeData();
    }
}