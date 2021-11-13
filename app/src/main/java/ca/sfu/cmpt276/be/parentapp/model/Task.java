package ca.sfu.cmpt276.be.parentapp.model;


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
        return DataManager.getInstance().getChildList().get(taskChildIndex);
    }

    public void completeTask() {
        if (taskChildIndex >= DataManager.getInstance().getChildList().size()) {
            taskChildIndex = 0;
        } else {
            taskChildIndex++;
        }
    }

}
