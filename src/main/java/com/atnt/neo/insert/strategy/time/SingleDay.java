package com.atnt.neo.insert.strategy.time;

import java.util.Calendar;

public class SingleDay implements TimePeriod {
    private final int year;

    public SingleDay(int year) {
        this.year = year;
    }

    @Override
    public Calendar getFirstDay() {
        return getLastDay();
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
