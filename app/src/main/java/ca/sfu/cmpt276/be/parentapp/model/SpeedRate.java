package ca.sfu.cmpt276.be.parentapp.model;

import androidx.annotation.NonNull;

public enum SpeedRate {
    TWENTY_FIVE, FIFTY, SEVENTY_FIVE, HUNDRED, TWO_HUNDRED, THREE_HUNDRED, FOUR_HUNDRED;

    @NonNull
    @Override
    public String toString() {
        return TimeConverter.getSpeedRateInPercentage(this) + "%";
    }
}
