package ca.sfu.cmpt276.be.parentapp.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * ChildManager keeps track of all the Children in the app. It's an ArrayList of Child objects
 * that can load, save, and edit children.
 */
public class ChildManager implements Iterable<Child> {
    private ArrayList<Child> allChildren = new ArrayList<>();
    private static ChildManager instance;

    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    public void add(Child addThis) {
        allChildren.add(addThis);
    }

    public Child get(int index) {
        return allChildren.get(index);
    }

    public void remove(int index) {
        allChildren.remove(index);
    }

    public void edit(int index, String editName) {
        allChildren.get(index).setName(editName);
    }

    public  ArrayList<Child> getAll() {
        return allChildren;
    }

    public void loadList(ArrayList<Child> replacement) {
        allChildren = replacement;
    }

    @NonNull
    @Override
    public Iterator<Child> iterator() {
        return allChildren.iterator();
    }
}
