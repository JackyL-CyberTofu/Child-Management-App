package ca.sfu.cmpt276.be.parentapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;
import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.ImageManager;
import ca.sfu.cmpt276.be.parentapp.controller.TaskManager;
import ca.sfu.cmpt276.be.parentapp.model.Child;
import ca.sfu.cmpt276.be.parentapp.model.Coin;
import ca.sfu.cmpt276.be.parentapp.model.Task;

/**
 * TaskEditActivity is where tasks are created, edited, and checked off when completed.
 */
public class TaskEditActivity extends AppCompatActivity {
    private static final String EXTRA_TASK_NUMBER = "taskNumber";
    private static final String EXTRA_IS_NEW_TASK = "doTaskEdit";

    private boolean isExistingTask;
    private int taskNumber;

    private final TaskManager taskManager = new TaskManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        getExtras();
        setUpAppBar();

        if (isExistingTask) {
            setUpTaskName();
            setUpButton();
            setTaskChild();
        } else {
            hideComponents();
        }

        ArrayAdapter<Child> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listview_task_history);
        list.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_delete_appbar, menu);
        MenuItem deleteOverflow = menu.findItem(R.id.item_delete);
        if (!isExistingTask) {
            deleteOverflow.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
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

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskEditActivity.class);
    }

    public static Intent makeIntent(Context context, int taskNum) {
        Intent taskEditIntent = new Intent(context, TaskEditActivity.class);
        taskEditIntent.putExtra(EXTRA_TASK_NUMBER, taskNum);
        taskEditIntent.putExtra(EXTRA_IS_NEW_TASK, true);

        return taskEditIntent;
    }

    private void setUpTaskName() {
        EditText taskName = findViewById(R.id.field_activity_task_name);
        taskName.setText(taskManager.getName(taskNumber));
    }

    private void setUpButton() {
        Button completeButton = findViewById(R.id.button_complete_task);
        completeButton.setOnClickListener(v -> {
            if (taskManager.isChildren()) {
                taskManager.completeTask(taskNumber);
            }
            finish();
        });
    }

    private void setTaskChild() {
        if (taskManager.isChildren()) {
            TextView taskedChild = findViewById(R.id.text_activity_tasked_child);
            taskedChild.setText(taskManager.getTaskedChildName(taskNumber));

            ImageManager imageManager = new ImageManager();
            ImageView childPortrait = findViewById(R.id.image_activity_tasked_child);
            childPortrait.setImageBitmap(imageManager.getPortrait(TaskEditActivity.this,
                    taskManager.getTaskedChildId(taskNumber)));
        } else {
            findViewById(R.id.group_tasked_child_info).setVisibility(View.GONE);
        }
    }

    private void setUpAppBar() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("New Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (isExistingTask) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Task");
        }
    }

    private void getExtras() {
        Intent intent = getIntent();

        isExistingTask = intent.getBooleanExtra(EXTRA_IS_NEW_TASK, false);
        taskNumber = intent.getIntExtra(EXTRA_TASK_NUMBER, 0);
    }

    private void hideComponents() {
        findViewById(R.id.group_existing_task_info).setVisibility(View.GONE);
        TextView whoseTurn = findViewById(R.id.text_tasked_child_label);
        whoseTurn.setText("");
    }

    private void saveAndExit() {
        EditText taskName = findViewById(R.id.field_activity_task_name);
        String newName = taskName.getText().toString();

        if (newName.isEmpty()) {
            Toast.makeText(this, "Please enter a name for the task.", Toast.LENGTH_SHORT).show();
        } else {
            save(newName);
            finish();
        }
    }

    private void save(String newName) {
        if (isExistingTask) {
            taskManager.edit(taskNumber, newName);
        } else {
            taskManager.add(new Task(newName));
        }
    }

    private void deleteAndExit() {
        taskManager.remove(taskNumber);
        finish();
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter() {
            super(TaskEditActivity.this, R.layout.layout_standard, taskManager.get(taskNumber).getAllChild());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.layout_standard, parent, false);
            }

            ImageView image_child = (ImageView) itemView.findViewById(R.id.image_child);
            ImageManager imageManager = new ImageManager();
            image_child.setImageBitmap(imageManager.getPortrait(TaskEditActivity.this, taskManager.get(taskNumber).getChild(position).getId()));

            TextView text_upper = (TextView) itemView.findViewById(R.id.text_child);
            if(isExistingTask) {
                text_upper.setText(taskManager.get(taskNumber).getChild(position).getName()+" @ "+taskManager.get(taskNumber).getTime(position));
            }


            //Fill the view
            return itemView;
        }
    }

}