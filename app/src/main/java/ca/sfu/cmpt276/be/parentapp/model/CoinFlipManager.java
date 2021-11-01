package ca.sfu.cmpt276.be.parentapp.model;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class CoinFlipManager {

    private static CoinFlipManager instance;

    ArrayList<Coin> coinFlipHistory = new ArrayList<Coin>();
    int nextChild = 0;

    public static CoinFlipManager getInstance() {
        if (instance == null) {
            instance = new CoinFlipManager();
        }
        return instance;
    }

    public String flipRandomCoin(String userChoice){

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

        String childPicked = getLastChildPicked().getName();

        saveCoinFlip(result,userChoice, childPicked);

        return result;
    }

    public void saveCoinFlip(String result, String userChoice, String childPicked){

        LocalDateTime creationTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = creationTime.format(formatter);

        Coin coinflip = new Coin(time, childPicked, userChoice, result);
        coinFlipHistory.add(coinflip);

    }

    public Child getLastChildPicked(){
        ChildManager childManager = ChildManager.getInstance();
        this.nextChild = this.nextChild + 1;
        return childManager.get(this.nextChild-1);
    }

    public Coin getCoinFlipGame(int index){
        return this.coinFlipHistory.get(index);
    }

}
