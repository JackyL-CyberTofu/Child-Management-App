package ca.sfu.cmpt276.be.parentapp.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.ChildManager;
import ca.sfu.cmpt276.be.parentapp.controller.ImageManager;
import ca.sfu.cmpt276.be.parentapp.model.Child;

/**
 * ChildEditActivity manages the creation and edit of a single child in the app.
 */
public class ChildEditActivity extends AppCompatActivity{
    private static final String EXTRA_CHILD_LOCATION = "childLocation";
    private static final String EXTRA_DO_EDIT = "doEdit";

    private boolean doEdit;
    private int childPosition;
    private Child currentChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.text_editChild);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getGalleryExtraction();
        getExtras();
        setUpPortrait();
    }

    private void setUpPortrait() {
        ImageView childPortrait = findViewById(R.id.image_child_portrait);
        ImageManager imageManager = new ImageManager();
        if (doEdit) {
            childPortrait.setImageBitmap(imageManager.getPortrait(ChildEditActivity.this, currentChild.getName()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_delete_appbar, menu);
        MenuItem deleteOverflow = menu.findItem(R.id.item_delete);
        if (!doEdit) {
            deleteOverflow.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_save) {
            saveAndExit();
        }

        if (item.getItemId() == R.id.item_delete) {
            deleteAndExit();
        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteExistingChild() {
        ChildManager childManager = new ChildManager();
        childManager.remove(childPosition);
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

    private void saveAndExit() {
        EditText childNameEditText = findViewById(R.id.field_child_name);
        String newName = childNameEditText.getText().toString();

        if (newName.isEmpty()) {
            Toast.makeText(this, "Please enter a name for the child.", Toast.LENGTH_SHORT).show();
        } else {
            save(newName);
            finish();
        }
    }

    private void save(String newName) {
        ChildManager childManager = new ChildManager();
        if (doEdit) {
            childManager.edit(childPosition, newName);
        } else {
            currentChild.setName(newName);
            childManager.add(currentChild);
        }
    }

    private void deleteAndExit() {
        if (currentChild.getName() == "null") {
            //TODO: delete image picked for the child
        }
        deleteExistingChild();
        finish();
    }

    private void getExtras() {
        ChildManager childManager = new ChildManager();
        Intent intent = getIntent();
        doEdit = intent.getBooleanExtra(EXTRA_DO_EDIT, false);
        childPosition = intent.getIntExtra(EXTRA_CHILD_LOCATION, -1);

        if (doEdit) {
            EditText childNameEditText = findViewById(R.id.field_child_name);
            String childName = childManager.get(childPosition).getName();
            childNameEditText.setText(childName);
            currentChild = childManager.get(childPosition);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.text_addChild);
            //todo: change to actually null when ids are implemented
            currentChild = new Child("null");
        }
    }

    private void getGalleryExtraction() {
        ImageView imageOfChild = findViewById(R.id.image_child_portrait);
        Button changeImage = findViewById(R.id.button_add_image);
        ActivityResultLauncher<String> getContent;

        getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
            //TODO: fix bug where leaving activity crashes the program.
            @Override
            public void onActivityResult(Uri result) {
                ImageManager imageManager = new ImageManager();
                imageManager.savePortraitBitmap(ChildEditActivity.this, result, currentChild.getName());
                imageOfChild.setImageBitmap(imageManager.loadPortraitBitmap(ChildEditActivity.this, currentChild.getName()));
            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContent.launch("image/*");
            }
        });

    }



}
