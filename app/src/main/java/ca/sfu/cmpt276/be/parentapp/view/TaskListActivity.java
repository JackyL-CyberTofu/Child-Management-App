package ca.sfu.cmpt276.be.parentapp.view;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.ImageManager;
import ca.sfu.cmpt276.be.parentapp.model.Task;
import ca.sfu.cmpt276.be.parentapp.controller.TaskManager;

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

    @Override
    protected void onResume() {
        super.onResume();
        showTasks();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setUpAddButton() {
        FloatingActionButton addTask = findViewById(R.id.button_add_task);
        addTask.setOnClickListener(v -> {
            Intent newTask = TaskEditActivity.makeIntent(TaskListActivity.this);
            startActivity(newTask);
        });
    }

    private void setUpTaskSelection() {
        ListView taskList = findViewById(R.id.list_tasks);
        taskList.setOnItemClickListener((parent, viewClicked, position, id) -> {
            Intent openTask = TaskEditActivity.makeIntent(TaskListActivity.this, position);
            startActivity(openTask);
        });
    }

    private void showTasks() {
        ArrayAdapter<Task> taskAdapter = new TaskListAdapter();
        ListView taskList = findViewById(R.id.list_tasks);
        taskList.setAdapter(taskAdapter);
    }

    private class TaskListAdapter extends ArrayAdapter<Task> {
        public TaskListAdapter() {
            super(TaskListActivity.this, R.layout.layout_task, taskManager.getAll());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View taskView = convertView;
            Task currentTask = taskManager.get(position);
            if (taskView == null) {
                taskView = getLayoutInflater().inflate(R.layout.layout_task, parent, false);
            }

            if (taskManager.isChildren()) {
                TextView childView = taskView.findViewById(R.id.text_layout_tasked_child);
                childView.setText(currentTask.getTaskedChild().getName());
            }
            ImageManager imageManager = new ImageManager();
            TextView nameView = taskView.findViewById(R.id.text_layout_task);
            nameView.setText(currentTask.getName());
            ImageView childPortrait = taskView.findViewById(R.id.image_layout_child_portrait);
            childPortrait.setImageBitmap(imageManager.getPortrait(TaskListActivity.this, position));
            return taskView;
        }
    }
}
