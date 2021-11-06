package ca.sfu.cmpt276.be.parentapp.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class DataManager {
    private static DataManager instance;

    private ArrayList<Child> childList = new ArrayList<>();
    private SaveManager saveOption;

    public interface SaveManager {
        String load();
        void save(String saveJson);
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private DataManager() {}

    public void setSaveOption(SaveManager saveInterface) {
        saveOption = saveInterface;
    }

    public void loadData() {
        Gson gson = new GsonBuilder().create();
        String gsonChildren = saveOption.load();
        if (!gsonChildren.isEmpty()) {
            childList = gson.fromJson(gsonChildren, new TypeToken<ArrayList<Child>>(){}.getType());
        }
    }

    public void saveData() {
        Gson gson = new GsonBuilder().create();
        String gsonChildren = gson.toJson(childList);
        saveOption.save(gsonChildren);
    }

    public ArrayList<Child> getChildList() {
        return childList;
    }

}
