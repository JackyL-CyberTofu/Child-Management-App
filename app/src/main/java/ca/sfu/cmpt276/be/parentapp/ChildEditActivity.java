package ca.sfu.cmpt276.be.parentapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.model.Child;
import ca.sfu.cmpt276.be.parentapp.model.ChildManager;

public class ChildEditActivity extends AppCompatActivity {

    private static final String EXTRA_CHILD_LOCATION = "childLocation";
    private static final String EXTRA_DO_EDIT = "doEdit";
    private final ChildManager childManager = ChildManager.getInstance();

    private boolean doEdit;
    private int childPosition;

    SharedPreferences saveSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Child");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveSP = getSharedPreferences("SaveData", Context.MODE_PRIVATE);


        setExtras();
        setUpSaveButton();
        setUpDeleteButton();
    }

    @Override
    public boolean onSupportNavigateUp() {
        saveAndExit();
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        saveData();
    }

    private void setUpDeleteButton() {
        Button deleteButton = findViewById(R.id.deleteButton);
        if (!doEdit) {
            deleteButton.setVisibility(View.GONE);
        }
        deleteButton.setOnClickListener(v -> deleteAndExit());
    }

    private void setUpSaveButton() {
        FloatingActionButton saveFab = findViewById(R.id.saveButton);
        saveFab.setOnClickListener(v -> saveAndExit());
    }

    private void deleteChild() {
        childManager.remove(childPosition);
    }

    private void saveAndExit() {
        EditText childNameEditText = findViewById(R.id.editChildName);
        String newName = childNameEditText.getText().toString();

        if (newName.isEmpty()) {
            Toast.makeText(this, "Please enter a name for the child.", Toast.LENGTH_SHORT).show();
        } else {
            save(newName);
            finish();
        }
    }

    private void save(String newName) {
        if (doEdit) {
            childManager.get(childPosition).setName(newName);
        } else {
            childManager.add(new Child(newName));
        }
    }

    private void deleteAndExit() {
        deleteChild();
        finish();
    }

    private void setExtras() {
        Intent intent = getIntent();
        doEdit = intent.getBooleanExtra(EXTRA_DO_EDIT, false);
        childPosition = intent.getIntExtra(EXTRA_CHILD_LOCATION, -1);

        if (doEdit) {
            EditText childNameEditText = findViewById(R.id.editChildName);
            String childName = childManager.get(childPosition).getName();
            childNameEditText.setText(childName);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Add Child");
        }
    }

    private void saveData() {
        ChildManager childManager = ChildManager.getInstance();

        SharedPreferences.Editor editor = saveSP.edit();
        Gson gson = new GsonBuilder().create();

        String gsonChildren = gson.toJson(childManager.getAll());

        editor.putString(MainActivity.SP_CHILDREN_GSON, gsonChildren);
        editor.apply();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildEditActivity.class);
    }

    public static Intent makeIntent(Context context, int childPosition) {
        Intent editChildIntent = new Intent(context, ChildEditActivity.class);

        editChildIntent.putExtra(EXTRA_CHILD_LOCATION, childPosition);
        editChildIntent.putExtra(EXTRA_DO_EDIT, true);

        return editChildIntent;
    }
}
