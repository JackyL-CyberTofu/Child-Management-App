package ca.sfu.cmpt276.be.parentapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
Represents a game played. Has date, person that picked, that they selected
and results
 */

public class Coin {

    private LocalDateTime date;
    private String picker;
    private String pick;
    private String result;
    private int pickerWon;

    public Coin(LocalDateTime date, String picker, String pick, String result){
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

    public String getPicker() { return picker; }

    public String getPick() { return pick; }

    public String getResult() { return result; }

    public int getPickerWon() { return pickerWon; }

}
