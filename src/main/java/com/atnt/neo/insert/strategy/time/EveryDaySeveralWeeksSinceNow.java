package com.atnt.neo.insert.strategy.time;

import java.util.Calendar;

public class EveryDaySeveralWeeksSinceNow implements TimePeriod {
    private final int howManyWeeks;

    public EveryDaySeveralWeeksSinceNow(int weeks) {
        this.howManyWeeks = weeks;
    }

    @Override
    public Calendar getFirstDay() {
        final Calendar result = Calendar.getInstance();
        result.add(Calendar.DAY_OF_YEAR, -7*howManyWeeks);
        return result;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        return cal;

    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }
}
