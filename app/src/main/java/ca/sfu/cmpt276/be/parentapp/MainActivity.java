package ca.sfu.cmpt276.be.parentapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.cmpt276.be.parentapp.model.DataManager;

public class MainActivity extends AppCompatActivity {
    public static final String SP_CHILDREN_GSON = "ChildrenGSON";
    SharedPreferences saveSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpSaving();
        loadData();
        setUpChildButton();
    }

    private void setUpChildButton() {
        Button toChildren = findViewById(R.id.button);
        toChildren.setOnClickListener(v -> {
            Intent childActivity = ChildListActivity.makeIntent(MainActivity.this);
            startActivity(childActivity);
        });
    }

    private void setUpSaving() {
        DataManager dataManager = DataManager.getInstance();
        dataManager.setSaveOption(new DataManager.SaveManager() {
            @Override
            public String load() {
                saveSP = getApplicationContext().getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                return saveSP.getString(SP_CHILDREN_GSON, "");
            }

            @Override
            public void save(String saveJson) {
                saveSP = getApplicationContext().getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveSP.edit();
                editor.putString(MainActivity.SP_CHILDREN_GSON, saveJson);
                editor.apply();
            }
        });
    }

    private void loadData() {
        DataManager.getInstance().loadData();
    }
}