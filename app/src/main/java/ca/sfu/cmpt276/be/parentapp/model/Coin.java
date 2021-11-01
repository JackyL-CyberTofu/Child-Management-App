package ca.sfu.cmpt276.be.parentapp.model;

public class Coin {

    String date;
    String picker;
    String pick;
    String result;
    int pickerWon;

    public Coin(String date, String picker, String pick, String result){
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

    public String getDate() { return date; }

    public String getPicker() { return picker; }

    public String getPick() { return pick; }

    public String getResult() { return result; }

    public int getPickerWon() { return pickerWon; }

}
