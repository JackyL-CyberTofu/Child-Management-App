package ca.sfu.cmpt276.be.parentapp.model;

/**
 * TimeoutManager contains a data to deal with countdown timer feature.
 */

public class TimeoutManager {
    private static TimeoutManager instance;
    private boolean isTimerRunning = false;
    private boolean firstState = true;
    private boolean isAlarmRunning = false;

    public boolean isAlarmRunning() {
        return isAlarmRunning;
    }

    public void setAlarmRunning(boolean alarmRunning) {
        isAlarmRunning = alarmRunning;
    }

    private long timeChosen = 0;
    private long tempTime = 0;

    public long getTimeChosen() {
        return timeChosen;
    }

    public void setTimeChosen(long timeChosen) {
        this.timeChosen = timeChosen;
    }

    public long getTempTime() {
        return tempTime;
    }

    public void setTempTime(long tempTime) {
        this.tempTime = tempTime;
    }

    public static TimeoutManager getInstance() {
        if (instance == null) {
            instance = new TimeoutManager();
        }
        return instance;
    }

    public boolean isTimerRunning() {
        return isTimerRunning;
    }

    public void setTimerRunning(Boolean isTimerRunning) {
        this.isTimerRunning = isTimerRunning;
    }

    public boolean isFirstState() {
        return firstState;
    }

    public void setFirstState(boolean firstState) {
        this.firstState = firstState;
    }
}