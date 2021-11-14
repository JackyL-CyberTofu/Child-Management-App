package ca.sfu.cmpt276.be.parentapp.model;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import ca.sfu.cmpt276.be.parentapp.CoinFlipActivity;

/**
 * DataManager is a singleton that holds onto persistent data in the app such the list of Child and
 * the history of Coinflips. It also serializes this data into JSON.
 */
public class DataManager {
    public static final String CHILDREN_SAVENAME = "JsonChildren";
    public static final String COINFLIP_SAVENAME = "JsonCoinflip";
    public static final String CHILD_INDEX_SAVENAME = "JsonChildIndex";
    public static final String COINFLIP_QUEUE_SAVENAME = "JsonCoinFlipQueue";
    private static DataManager instance;

    private ArrayList<Child> childList = new ArrayList<>();
    private ArrayList<Coin> coinFlipHistory = new ArrayList<>();
    private int childFlipIndex = 0;
    private ArrayList<Child> coinFlipQueue = new ArrayList<>();

    private SaveManager saveOption;

    /**
     * SaveManager allows flexibility in the saving functionality of the app by allowing the JSON
     * strings generated by DataManager to be implemented by the client in the way they see fit.
     */
    public interface SaveManager {
        String load(String saveName);
        void save(String saveJson, String saveName);
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

    //Source: Dr. Victor Cheung, CMPT213 Fall 2021, Assignment 1.
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
    public void deserializeData() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJSONReader() {})
                .create();
        String jsonChildren = saveOption.load(CHILDREN_SAVENAME);
        String jsonCoinflip = saveOption.load(COINFLIP_SAVENAME);
        String jsonChildIndex = saveOption.load(CHILD_INDEX_SAVENAME);
        String jsonCoinFlipQueue = saveOption.load(COINFLIP_QUEUE_SAVENAME);
        if (!jsonChildren.isEmpty()) {
            childList = gson.fromJson(jsonChildren, new TypeToken<ArrayList<Child>>(){}.getType());
        }
        if (!jsonCoinflip.isEmpty()) {
            coinFlipHistory = gson.fromJson(jsonCoinflip, new TypeToken<ArrayList<Coin>>(){}.getType());
        }
        if (!jsonChildIndex.isEmpty()) {
            this.childFlipIndex = Integer.parseInt(jsonChildIndex);
        }
        if (!jsonCoinFlipQueue.isEmpty()) {
            this.coinFlipQueue = gson.fromJson(jsonChildren, new TypeToken<ArrayList<Child>>(){}.getType());
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
        String gsonCoinFlipQueue = gson.toJson(coinFlipQueue);
        saveOption.save(COINFLIP_SAVENAME, gsonCoinflip);
        saveOption.save(CHILD_INDEX_SAVENAME, Integer.toString(childFlipIndex));
        Log.i("save", String.valueOf(childFlipIndex));
        saveOption.save(COINFLIP_QUEUE_SAVENAME, gsonCoinFlipQueue);
    }

    public ArrayList<Child> getChildList() {
        return childList;
    }

    public ArrayList<Coin> getCoinFlipHistory() {
        return coinFlipHistory;
    }

    public Integer getChildFlipIndex() {
        return childFlipIndex;
    }

    public ArrayList<Child> getCoinFlipQueue() { return coinFlipQueue; }

    public void setChildFlipIndex(int set) {
        childFlipIndex = set;
    }



}
