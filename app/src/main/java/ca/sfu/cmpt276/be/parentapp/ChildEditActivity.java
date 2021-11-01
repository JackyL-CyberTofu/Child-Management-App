package ca.sfu.cmpt276.be.parentapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ChildEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Child");
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildEditActivity.class);
    }
}
