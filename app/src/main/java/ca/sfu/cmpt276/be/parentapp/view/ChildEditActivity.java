package ca.sfu.cmpt276.be.parentapp.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.model.Child;
import ca.sfu.cmpt276.be.parentapp.controller.ChildManager;

/**
 * ChildEditActivity manages the creation and edit of a single child in the app.
 */
public class ChildEditActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String EXTRA_CHILD_LOCATION = "childLocation";
    private static final String EXTRA_DO_EDIT = "doEdit";
    private static final int RESULT_LOAD_IMAGE = 1;

    private Button changeImage;
    private ImageView imageOfChild;
    private boolean doEdit;
    private int childPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.text_editChild);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getGalleryExtraction();
        getExtras();
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

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_add_image){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            imageOfChild.setImageURI(selectedImage);
        }
    }

    private void deleteChild() {
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
            childManager.add(new Child(newName));
        }
    }

    private void deleteAndExit() {
        deleteChild();
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
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.text_addChild);
        }
    }

    private void getGalleryExtraction() {
        imageOfChild = findViewById(R.id.image_child_portrait);
        changeImage = findViewById(R.id.button_add_image);
        changeImage.setOnClickListener(this);
    }
}
