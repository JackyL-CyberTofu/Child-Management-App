package ca.sfu.cmpt276.be.parentapp.model;

import android.content.res.Resources;
import android.provider.ContactsContract;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.DataManager;

/**
 * Task represents a single task in the app. It holds a child that is currently in charge and manages
 * who the next child in charge will be.
 */
public class Task {

    private String name;
    private int taskChildIndex = 0;

    private final ArrayList<Child> childHistory = new ArrayList<>();
    private final ArrayList<LocalDateTime> timeHistory = new ArrayList<>();

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Child getChild(int index) { return childHistory.get(index); }

    public String getHistoryTime(int index) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = formatter.format(this.timeHistory.get(index));
        return childHistory.get(index).getName()+" "+time;
    }

    public ArrayList<Child> getHistory(){ return childHistory; }

    public void setName(String editName) {
        name = editName;
    }

    public Child getTaskedChild() {
        ArrayList<Child> childList = DataManager.getInstance().getChildList();
        if (childList.isEmpty()) {
            return new Child("No Children Found (You should fix this layer)");
        }

        if (taskChildIndex >= childList.size()) {
            taskChildIndex = childList.size() - 1;
        }
        return DataManager.getInstance().getChildList().get(taskChildIndex);
    }

    public void completeTask() {
        childHistory.add(0,DataManager.getInstance().getChildList().get(taskChildIndex));
        timeHistory.add(0, LocalDateTime.now());
        if (taskChildIndex < (DataManager.getInstance().getChildList().size() - 1)) {
            taskChildIndex++;
        } else {
            taskChildIndex = 0;
        }
    }


}
