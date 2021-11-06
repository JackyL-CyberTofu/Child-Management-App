package ca.sfu.cmpt276.be.parentapp.model;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import ca.sfu.cmpt276.be.parentapp.MainActivity;

public class JsonSaver {
    public static final String SP_CHILDREN_GSON = "ChildrenGSON";
    private final saveData test;

    public interface saveData {
        String load();
        void save(String saveJson);
    }

    public JsonSaver(saveData test) {
        this.test = test;
    }

    private void loadData() {
        ChildManager childManager = ChildManager.getInstance();
        Gson gson = new GsonBuilder().create();

        String gsonChildren = test.load();
        if (!gsonChildren.isEmpty()) {
            childManager.loadList(gson.fromJson(gsonChildren, new TypeToken<ArrayList<Child>>(){}.getType()));
        }
    }

    private void saveData() {
        ChildManager childManager = ChildManager.getInstance();
        Gson gson = new GsonBuilder().create();
        String gsonChildren = gson.toJson(childManager.getAll());
        test.save(gsonChildren);
    }

}
