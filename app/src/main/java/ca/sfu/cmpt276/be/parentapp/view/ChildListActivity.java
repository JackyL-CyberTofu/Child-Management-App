package ca.sfu.cmpt276.be.parentapp.view;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.ChildManager;
import ca.sfu.cmpt276.be.parentapp.controller.ImageManager;
import ca.sfu.cmpt276.be.parentapp.model.Child;

/**
 * ChildListActivity shows all the Children stored in the app.
 */
public class ChildListActivity extends AppCompatActivity {
    private static final ChildManager childManager = new ChildManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupAnimation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.text_configureChildren);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showChildren();
        setUpAddButton();
        setUpListViewClick();
        setUpNavBar();


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

    private void setupAnimation() {
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        getWindow().setSharedElementsUseOverlay(false);
    }

    private void setUpNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_child);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            bottomNavigationView.postDelayed(() -> MainActivity.navigate(this, item, R.id.item_child),0);
            return true;
        });
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
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ChildListActivity.this, addButton, "shared_container");
            Intent intent = ChildEditActivity.makeIntent(ChildListActivity.this);
            startActivity(intent,options.toBundle());
        });
    }
    private void showChildren() {
        ArrayAdapter<Child> childAdapter = new ChildListAdapter();
        ListView childList = findViewById(R.id.list_children);
        childList.setAdapter(childAdapter);
    }

    private class ChildListAdapter extends ArrayAdapter<Child> {

        public ChildListAdapter() {
            super(ChildListActivity.this, R.layout.layout_standard, childManager.getAll());
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            Child currentChild = childManager.get(position);
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.layout_standard, parent, false);
            }

            TextView nameView = itemView.findViewById(R.id.text_child);
            nameView.setText(currentChild.getName());

            ImageView image = itemView.findViewById(R.id.image_child);
            ImageManager imageManager = new ImageManager();
            image.setImageBitmap(imageManager.getPortrait(ChildListActivity.this, currentChild.getId()));

            return itemView;
        }

    }
}
