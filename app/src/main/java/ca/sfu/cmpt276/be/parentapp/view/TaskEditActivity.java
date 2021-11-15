package ca.sfu.cmpt276.be.parentapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.TaskManager;
import ca.sfu.cmpt276.be.parentapp.model.Task;

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
            findViewById(R.id.group_existing_task_info).setVisibility(View.GONE);
        }
    }

    private void setUpTaskName() {
        EditText taskName = findViewById(R.id.edit_text_task_name);
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
            TextView taskedChild = findViewById(R.id.text_view_tasked_child);
            taskedChild.setText(taskManager.getTaskedChild(taskNumber));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_delete_appbar, menu);
        MenuItem deleteOverflow = menu.findItem(R.id.delete_item);
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
        if (item.getItemId() == R.id.save_item) {
            saveAndExit();
        }

        if (item.getItemId() == R.id.delete_item) {
            deleteAndExit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndExit() {
        EditText taskName = findViewById(R.id.edit_text_task_name);
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

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskEditActivity.class);
    }

    public static Intent makeIntent(Context context, int taskNum) {
        Intent taskEditIntent = new Intent(context, TaskEditActivity.class);

        taskEditIntent.putExtra(EXTRA_TASK_NUMBER, taskNum);
        taskEditIntent.putExtra(EXTRA_IS_NEW_TASK, true);

        return taskEditIntent;
    }
}