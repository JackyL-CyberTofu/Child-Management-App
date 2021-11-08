package ca.sfu.cmpt276.be.parentapp.model;

import android.view.View;

public class TimeConverter {
    public static final int HOUR_CONVERTER_FOR_MILSECONDS = 3600000;
    public static final int MIN_CONVERTER_FOR_MILSECONDS = 60000;
    public static final int SECONDS_CONVERTER_FORMILSECONDS = 1000;
    public static int getHourInMilSeconds(){
        return HOUR_CONVERTER_FOR_MILSECONDS;
    }

    public static int getMinInMilSeconds(){
        return MIN_CONVERTER_FOR_MILSECONDS;
    }

    public static int getSecondInMilSeconds(){
        return SECONDS_CONVERTER_FORMILSECONDS;
    }

    public static String toStringForMilSeconds(long timeInMilSeconds){
        int hour = (int) timeInMilSeconds / TimeConverter.getHourInMilSeconds();
        int minutes = (int) timeInMilSeconds % TimeConverter.getHourInMilSeconds() / TimeConverter.getMinInMilSeconds();
        int seconds = (int) timeInMilSeconds % TimeConverter.getHourInMilSeconds() % TimeConverter.getMinInMilSeconds() / TimeConverter.getSecondInMilSeconds();

        StringBuilder stringBuilderTime = new StringBuilder();

        stringBuilderTime.append("");
        stringBuilderTime.append(hour);
        stringBuilderTime.append(":");

        if (minutes < 10) {
            stringBuilderTime.append("0");
        }
        stringBuilderTime.append(minutes);
        stringBuilderTime.append(":");

        if (seconds < 10) {
            stringBuilderTime.append("0");
        }
        stringBuilderTime.append(seconds);

        return stringBuilderTime.toString();
    }
}
