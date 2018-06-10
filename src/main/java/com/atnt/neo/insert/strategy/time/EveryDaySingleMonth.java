package com.atnt.neo.insert.strategy.time;

import java.util.Calendar;

public class EveryDaySingleMonth implements TimePeriod {
    private final int year;
    private final int month;

    public EveryDaySingleMonth(int year, int month) {
        this.year = year;
        this.month = month;
    }

    @Override
    public Calendar getFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(this.year, this.month, 1, 1, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    @Override
    public Calendar getLastDay() {
        final Calendar cal = getFirstDay();
        cal.add(Calendar.MONTH, 1);
        return cal;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }

}
