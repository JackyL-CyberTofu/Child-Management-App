package ca.sfu.cmpt276.be.parentapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ChildEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Child");
    }
}
