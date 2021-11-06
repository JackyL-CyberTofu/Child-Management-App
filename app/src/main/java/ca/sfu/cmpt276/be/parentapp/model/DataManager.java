package ca.sfu.cmpt276.be.parentapp.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DataManager {
    public static final String CHILDREN_SAVENAME = "JsonChildren";
    public static final String COINFLIP_SAVENAME = "JsonCoinflip";
    private static DataManager instance;

    private ArrayList<Child> childList = new ArrayList<>();
    private ArrayList<Coin> coinFlipHistory = new ArrayList<>();

    private SaveManager saveOption;

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
        String gsonChildren = saveOption.load(CHILDREN_SAVENAME);
        String gsonCoinflip = saveOption.load(COINFLIP_SAVENAME);
        if (!gsonChildren.isEmpty()) {
            childList = gson.fromJson(gsonChildren, new TypeToken<ArrayList<Child>>(){}.getType());
        }
        if (!gsonCoinflip.isEmpty()) {
            coinFlipHistory = gson.fromJson(gsonCoinflip, new TypeToken<ArrayList<Coin>>(){}.getType());
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
        saveOption.save(COINFLIP_SAVENAME, gsonCoinflip);
    }

    public ArrayList<Child> getChildList() {
        return childList;
    }

    public ArrayList<Coin> getCoinFlipHistory() {
        return coinFlipHistory;
    }

}
