package ca.sfu.cmpt276.be.parentapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
Represents one game of a coin flip
 Saves the time of the flip, chold and what they picked
 as well as the result of the flip.
 */

public class Coin {

    private final LocalDateTime date;
    private final String pick;
    private final Child picker;
    private final String result;
    private final int pickerWon;

    public Coin(LocalDateTime date, Child picker, String pick, String result){
        this.date = date;
        this.picker = picker;
        this.pick = pick;
        this.result = result;

        if (pick.equals(result)){
            this.pickerWon = 1;
        } else {
            this.pickerWon = 0;
        }

    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return formatter.format(this.date);
    }

    public String getPicker() { return picker.getName(); }

    public String getPick() { return pick; }

    public String getResult() { return result; }

    public int getPickerWon() { return pickerWon; }

}
