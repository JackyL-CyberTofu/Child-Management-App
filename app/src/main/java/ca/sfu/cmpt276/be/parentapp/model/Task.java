package ca.sfu.cmpt276.be.parentapp.model;

import java.util.ArrayList;

import ca.sfu.cmpt276.be.parentapp.controller.DataManager;

public class Task {
    private String name;
    private int taskChildIndex = 0;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String editName) {
        name = editName;
    }

    public Child getTaskedChild() {
        ArrayList<Child> childList = DataManager.getInstance().getChildList();
        if (childList.isEmpty()) {
            return new Child("No Children Found (You should fix this layer)");
        }
        return DataManager.getInstance().getChildList().get(taskChildIndex);
    }

    public void completeTask() {
        if (taskChildIndex < (DataManager.getInstance().getChildList().size() - 1)) {
            taskChildIndex++;
        } else {
            taskChildIndex = 0;
        }
    }


}
