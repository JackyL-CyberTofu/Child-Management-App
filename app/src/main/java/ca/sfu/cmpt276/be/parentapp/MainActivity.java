package ca.sfu.cmpt276.be.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import ca.sfu.cmpt276.be.parentapp.model.Child;
import ca.sfu.cmpt276.be.parentapp.model.ChildManager;
import ca.sfu.cmpt276.be.parentapp.model.JsonSaver;

public class MainActivity extends AppCompatActivity {
    public static final String SP_CHILDREN_GSON = "ChildrenGSON";
    SharedPreferences saveSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveSP = getSharedPreferences("SaveData", Context.MODE_PRIVATE);

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

    private void loadData() {
        JsonSaver test = new JsonSaver(new JsonSaver.saveData() {
            @Override
            public String load() {
                return null;
            }

            @Override
            public void save(String saveJson) {

            }
        });

        ChildManager childManager = ChildManager.getInstance();
        Gson gson = new GsonBuilder().create();

        String gsonChildren = saveSP.getString(SP_CHILDREN_GSON, "");
        if (!gsonChildren.isEmpty()) {
            childManager.loadList(gson.fromJson(gsonChildren, new TypeToken<ArrayList<Child>>(){}.getType()));
        }
    }
}