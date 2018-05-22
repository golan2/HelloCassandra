package com.atnt.neo.insert.strategy.time;

import java.util.Calendar;

public class EveryDaySeveralDaysEndOfYear implements TimePeriod {
    private final int year;
    private final int howManyDays;

    public EveryDaySeveralDaysEndOfYear(int year, int howManyDays) {
        this.year = year;
        this.howManyDays = howManyDays;
    }

    @Override
    public Calendar getFirstDay() {
        final Calendar result = (Calendar) getLastDay().clone();
        result.add(Calendar.DAY_OF_WEEK, -1*howManyDays);
        return result;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(this.year, Calendar.DECEMBER, 31);
        return cal;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }
}
