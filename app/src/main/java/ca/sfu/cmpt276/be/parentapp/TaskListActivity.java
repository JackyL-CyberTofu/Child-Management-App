package ca.sfu.cmpt276.be.parentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.model.Task;
import ca.sfu.cmpt276.be.parentapp.model.TaskManager;
import ca.sfu.cmpt276.be.parentapp.view.ChildEditActivity;
import ca.sfu.cmpt276.be.parentapp.view.ChildListActivity;

public class TaskListActivity extends AppCompatActivity {
    private final TaskManager taskManager = new TaskManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Tasks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showTasks();
        setUpAddButton();
        setUpTaskSelection();
    }

    private void setUpAddButton() {
        FloatingActionButton addTask = findViewById(R.id.add_task_button);
        addTask.setOnClickListener(v -> {
            Toast.makeText(this, "To be implemented!", Toast.LENGTH_SHORT).show();
        });
    }

    private void setUpTaskSelection() {
        ListView taskList = findViewById(R.id.task_listview);
        taskList.setOnItemClickListener((parent, viewClicked, position, id) -> {
            Toast.makeText(this, "To be implemented but you tapped " + id, Toast.LENGTH_SHORT).show();
        });
    }

    private void showTasks() {
        ArrayAdapter<Task> taskAdapter = new TaskListAdapter();
        ListView taskList = findViewById(R.id.task_listview);
        taskList.setAdapter(taskAdapter);
    }

    private class TaskListAdapter extends ArrayAdapter<Task> {
        public TaskListAdapter() {
            super(TaskListActivity.this, R.layout.layout_child_item, taskManager.getAll());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View taskView = convertView;
            Task currentTask = taskManager.get(position);
            if (taskView == null) {
                taskView = getLayoutInflater().inflate(R.layout.layout_task, parent, false);
            }

            TextView nameView = taskView.findViewById(R.id.task_name_textview);
            nameView.setText(currentTask.getName());
            TextView childView = taskView.findViewById(R.id.tasked_child_textview);
            childView.setText(currentTask.getTaskedChild().getName());
            return taskView;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showTasks();
    }
}