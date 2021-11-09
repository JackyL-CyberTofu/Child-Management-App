package ca.sfu.cmpt276.be.parentapp.model;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
Handles the logic of the coin flip.
 Flips random side, stores the history of the coin flip, and
 remembers the last child that picked
 */

public class CoinFlipManager {

    private static CoinFlipManager instance;

    private DataManager dataManager = DataManager.getInstance();

    int childIndex = DataManager.getInstance().getChildFlipIndex();

    ArrayList<Coin> coinFlipHistory = DataManager.getInstance().getCoinFlipHistory();
    ChildManager childManager = new ChildManager();
    private List<CoinObserver> observers = new ArrayList<>();

    public static CoinFlipManager getInstance() {
        if (instance == null) {
            instance = new CoinFlipManager();
        }
        return instance;
    }

    public void registerChangeCallback(CoinObserver obs) {
        observers.add(obs);
    }

    public void unRegisterChangeCallback(CoinObserver obs) {
        observers.remove(obs);
    }

    private void notifyValueHasChanged() {
        for (CoinObserver obs : observers) {
            obs.notifyCounterChanged();
        }
    }

    public String flipRandomCoin(String userChoice) {

        Random rand = new Random();
        int randomInt = rand.nextInt(2);
        String result;

        switch (randomInt) {
            case 0:
                result = "Heads";
                break;
            case 1:
                result = "Tails";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + randomInt);
        }

        saveCoinFlip(result, userChoice, getNextChild());
        notifyValueHasChanged();

        return result;
    }

    @Nullable
    private String getNextChild() {
        String childPick;
        if (childManager.size() == 0) {
            childPick = null;
        } else {
            childPick = childManager.getName(childIndex);
            if (childManager.size() <= childIndex + 1) {
                childIndex = 0;
            } else {
                childIndex++;
            }
        }
        return childPick;
    }

    public void saveCoinFlip(String result, String userChoice, String childPicked) {

        LocalDateTime creationTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = creationTime.format(formatter);

        Coin coinGame = new Coin(creationTime, childPicked, userChoice, result);
        coinFlipHistory.add(0, coinGame);
        serializeCoinflips();
    }

    public Coin getCoinFlipGame(int index) {
        return this.coinFlipHistory.get(index);
    }

    public ArrayList<Coin> getCoinList() {
        return this.coinFlipHistory;
    }

    public int getChildIndex() {
        return childIndex;
    }

    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }

    private void serializeCoinflips() {
        dataManager.setChildFlipIndex(childIndex);
        dataManager.serializeCoinflips();
    }

    public interface CoinObserver {
        void notifyCounterChanged();
    }

}
