package ca.sfu.cmpt276.be.parentapp.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import ca.sfu.cmpt276.be.parentapp.model.Child;
import ca.sfu.cmpt276.be.parentapp.model.Coin;
import ca.sfu.cmpt276.be.parentapp.model.Task;

/**
 * DataManager is a singleton that holds onto persistent data in the app such the list of Child and
 * the history of Coinflips. It also serializes this data into JSON.
 */
public class DataManager {

    public static final String CHILDREN_SAVENAME = "JsonChildren";
    public static final String COINFLIP_SAVENAME = "JsonCoinflip";
    public static final String COINFLIP_QUEUE_SAVENAME = "JsonCoinFlipQueue";
    private static final String TASK_SAVENAME = "JsonTasks";
    private static DataManager instance;

    private ArrayList<Child> childList = new ArrayList<>();
    private ArrayList<Coin> coinFlipHistory = new ArrayList<>();
    private ArrayList<Task> taskList = new ArrayList<>();
    private ArrayList<Child> coinFlipQueue = new ArrayList<>();

    private SaveManager saveOption;

    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void setSaveOption(SaveManager saveInterface) {
        saveOption = saveInterface;
    }

    public void deserializeData() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJSONReader() {
                })
                .create();
        String jsonChildren = saveOption.load(CHILDREN_SAVENAME);
        if (!jsonChildren.isEmpty()) {
            childList = gson.fromJson(jsonChildren, new TypeToken<ArrayList<Child>>() {
            }.getType());
        }
        String jsonCoinFlipQueue = saveOption.load(COINFLIP_QUEUE_SAVENAME);
        if (!jsonCoinFlipQueue.isEmpty()) {
            this.coinFlipQueue = gson.fromJson(jsonCoinFlipQueue, new TypeToken<ArrayList<Child>>() {}.getType());
        }
        String jsonCoinflip = saveOption.load(COINFLIP_SAVENAME);
        if (!jsonCoinflip.isEmpty()) {
            coinFlipHistory = gson.fromJson(jsonCoinflip, new TypeToken<ArrayList<Coin>>() {
            }.getType());
        }
        reassignChildren();
        String jsonTasks = saveOption.load(TASK_SAVENAME);
        if (!jsonTasks.isEmpty()) {
            taskList = gson.fromJson(jsonTasks, new TypeToken<ArrayList<Task>>() {
            }.getType());
        }
    }

    public void serializeChildren() {
        Gson gson = new GsonBuilder()
                .create();
        String gsonChildren = gson.toJson(childList);
        saveOption.save(CHILDREN_SAVENAME, gsonChildren);
    }
    public void serializeCoinflips() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJSONReader() {})
                .create();
        String gsonCoinflip = gson.toJson(coinFlipHistory);
        String gsonCoinFlipQueue = gson.toJson(this.coinFlipQueue);
        saveOption.save(COINFLIP_SAVENAME, gsonCoinflip);
        saveOption.save(COINFLIP_QUEUE_SAVENAME, gsonCoinFlipQueue);
    }

    public void serializeTasks() {
        Gson gson = new GsonBuilder()
                .create();
        String gsonTasks = gson.toJson(taskList);
        saveOption.save(TASK_SAVENAME, gsonTasks);
    }

    public ArrayList<Child> getChildList() {
        return childList;
    }

    public ArrayList<Coin> getCoinFlipHistory() {
        return coinFlipHistory;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public ArrayList<Child> getCoinFlipQueue() { return coinFlipQueue; }

    public boolean checkIfInHistory(Child child) {
        for (Coin coin : coinFlipHistory) {
            if (coin.getPickerId().equals(child.getId())) {
                return true;
            }
        }
        return false;
    }

    private void reassignChildren() {
        for (Coin coin : coinFlipHistory) {
            for (Child child : childList) {
                if (coin.getPickerId().equals(child.getId())) {
                    coin.overridePicker(child);
                }
            }
        }
        int index = 0;
        for (Child child : coinFlipQueue) {
            for (Child child2 : childList) {
                if (child.getId().equals(child2.getId())){
                    coinFlipQueue.set(index,child2);
                }
            }
            index++;
        }
    }

    /**
     * LocalDateTimeJSONReader allows LocalDateTime objects to be saved to JSON.
     * Sourced: Dr. Victor Cheung, CMPT213 Fall 2021, Assignment 1.
     */
    private static class LocalDateTimeJSONReader extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter jsonWriter,
                          LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(localDateTime.toString());
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString());
        }
    }

    /**
     * SaveManager allows flexibility in the saving functionality of the app by allowing the JSON
     * strings generated by DataManager to be implemented by the client in the way they see fit.
     */
    public interface SaveManager {
        String load(String saveName);
        void save(String saveJson, String saveName);
    }
}
