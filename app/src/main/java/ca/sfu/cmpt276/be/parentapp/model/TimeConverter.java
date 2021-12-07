package ca.sfu.cmpt276.be.parentapp.model;

/**
 * TimeConverter is a tool class for converting milliseconds to a second, minute and an hour.
 * It also has a feature of converting time left in milliseconds to String.
 */

public class TimeConverter {
    public static final int HOUR_CONVERTER_FOR_MILSECONDS = 3600000;
    public static final int MIN_CONVERTER_FOR_MILSECONDS = 60000;
    public static final int SECONDS_CONVERTER_FORMILSECONDS = 1000;
    public static final int TWENTY_FIVE_INT = 25;
    public static final int FIFTY_INT = 50;
    public static final int SEVCENTY_FIVE_INT = 75;
    public static final int HUNDRED_INT = 100;
    public static final int TWO_HUNDRED_INT = 200;
    public static final int THREE_HUNDRED_INT = 300;
    public static final int FOUR_HUNDRED_INT = 400;

    public static int getHourInMilSeconds() {
        return HOUR_CONVERTER_FOR_MILSECONDS;
    }

    public static int getMinInMilSeconds() {
        return MIN_CONVERTER_FOR_MILSECONDS;
    }

    public static int getSecondInMilSeconds() {
        return SECONDS_CONVERTER_FORMILSECONDS;
    }

    public static double getRelativeTimeLeft(SpeedRate before, SpeedRate after) {
        return getSpeedRateInPercentage(before) / (double) getSpeedRateInPercentage(after);
    }

    public static double getRelativeTimeLeft(Long timeLeft, SpeedRate rate) {
        // Relative time Left compared to the rate speed rate of 100%
        double timeLeftDouble = (double) timeLeft;
        switch(rate) {
            case TWNETY_FIVE:
                timeLeftDouble = timeLeftDouble / 4;
                break;
            case FIFTY:
                timeLeftDouble = timeLeftDouble / 2;
                break;
            case SEVENTY_FIVE:
                timeLeftDouble = 3 * timeLeftDouble / 4;
                break;
            case HUNDRED:
                break;
            case TWO_HUNDRED:
                timeLeftDouble = timeLeftDouble * 2;
                break;
            case THREE_HUNDRED:
                timeLeftDouble = timeLeftDouble * 3;
                break;
            case FOUR_HUNDRED:
                timeLeftDouble = timeLeftDouble * 4;
                break;
        }
        return timeLeftDouble;
    }

    public static double getInverseRelativeTimeLeft(Long timeLeft, SpeedRate rate) {
        // Relative time Left compared to the rate speed rate of 100%
        double timeLeftDouble = (double) timeLeft;
        switch(rate) {
            case TWNETY_FIVE:
                timeLeftDouble = timeLeftDouble * 4;
                break;
            case FIFTY:
                timeLeftDouble = timeLeftDouble * 2;
                break;
            case SEVENTY_FIVE:
                timeLeftDouble = 4 * timeLeftDouble / 3;
                break;
            case HUNDRED:
                break;
            case TWO_HUNDRED:
                timeLeftDouble = timeLeftDouble / 2;
                break;
            case THREE_HUNDRED:
                timeLeftDouble = timeLeftDouble / 3;
                break;
            case FOUR_HUNDRED:
                timeLeftDouble = timeLeftDouble / 4;
                break;
        }
        return timeLeftDouble;
    }

    public static String toStringForMilSeconds(long timeInMilSeconds) {
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

    public static int getSpeedRateInPercentage(SpeedRate rate) {
        switch(rate) {
            case TWNETY_FIVE:
                return TWENTY_FIVE_INT;
            case FIFTY:
                return FIFTY_INT;
            case SEVENTY_FIVE:
                return SEVCENTY_FIVE_INT;
            case HUNDRED:
                return HUNDRED_INT;
            case TWO_HUNDRED:
                return TWO_HUNDRED_INT;
            case THREE_HUNDRED:
                return THREE_HUNDRED_INT;
            case FOUR_HUNDRED:
                return FOUR_HUNDRED_INT;
            default:
                return -1;
        }
    }
}
