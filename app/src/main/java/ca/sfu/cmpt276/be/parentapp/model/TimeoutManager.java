package ca.sfu.cmpt276.be.parentapp.model;

import android.os.CountDownTimer;

public class TimeoutManager {
    private static TimeoutManager instance;
    private CountDownTimer countDownTimer;

    public static TimeoutManager getInstance(){
        if(instance == null) {
            instance = new TimeoutManager();
        }
        return instance;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }
}
