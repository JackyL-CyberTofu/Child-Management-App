package ca.sfu.cmpt276.be.parentapp.controller;

import java.util.ArrayList;

import ca.sfu.cmpt276.be.parentapp.controller.DataManager;
import ca.sfu.cmpt276.be.parentapp.model.Task;

public class TaskManager {
    private final ArrayList<Task> taskList = DataManager.getInstance().getTaskList();

    public Task get(int index) {
        return taskList.get(index);
    }

    public void add(Task newTask) {
        taskList.add(newTask);
        save();
    }

    public void remove(int index) {
        taskList.remove(index);
        save();
    }

    public void edit(int index, String editName) {
        taskList.get(index).setName(editName);
        save();
    }

    public String getName(int index) {
        return taskList.get(index).getName();
    }

    public String getTaskedChild(int index) {
        return taskList.get(index).getTaskedChild().getName();
    }

    public ArrayList<Task> getAll() {
        return taskList;
    }

    public void completeTask(int index) {
        taskList.get(index).completeTask();
        save();
    }

    public int size() {
        return taskList.size();
    }

    public boolean isEmpty() {
        return taskList.isEmpty();
    }

    public void save() {
        DataManager.getInstance().serializeTasks();
    }
}
