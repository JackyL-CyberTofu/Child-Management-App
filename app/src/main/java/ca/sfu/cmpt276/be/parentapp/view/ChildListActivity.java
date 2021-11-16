package ca.sfu.cmpt276.be.parentapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.model.Child;
import ca.sfu.cmpt276.be.parentapp.controller.ChildManager;

/**
 * ChildListActivity shows all the Children stored in the app.
 */
public class ChildListActivity extends AppCompatActivity {
    private static ChildManager childManager = new ChildManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.text_configureChildren);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showChildren();
        setUpAddButton();
        setUpListViewClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showChildren();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildListActivity.class);
    }

    private void setUpListViewClick() {
        ListView childList = findViewById(R.id.list_children);
        childList.setOnItemClickListener((parent, viewClicked, position, id) -> {
            Intent editChild = ChildEditActivity.makeIntent(ChildListActivity.this, position);
            startActivity(editChild);
        });
    }

    private void setUpAddButton() {
        FloatingActionButton addButton = findViewById(R.id.button_add_child);
        addButton.setOnClickListener(v -> {
            Intent launchEmptyEdit = ChildEditActivity.makeIntent(ChildListActivity.this);
            startActivity(launchEmptyEdit);
        });
    }
    private void showChildren() {
        ArrayAdapter<Child> childAdapter = new ChildListAdapter();
        ListView childList = findViewById(R.id.list_children);
        childList.setAdapter(childAdapter);
    }

    private class ChildListAdapter extends ArrayAdapter<Child> {

        public ChildListAdapter() {
            super(ChildListActivity.this, R.layout.layout_child_item, childManager.getAll());
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            Child currentChild = childManager.get(position);
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.layout_child_item, parent, false);
            }

            TextView nameView = itemView.findViewById(R.id.child_name);
            nameView.setText(currentChild.getName());
            return itemView;
        }

    }
}
