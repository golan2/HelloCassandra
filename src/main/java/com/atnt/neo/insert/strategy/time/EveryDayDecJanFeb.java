package com.atnt.neo.insert.strategy.time;

import java.util.Calendar;

public class EveryDayDecJanFeb implements TimePeriod {
    private final int year;

    public EveryDayDecJanFeb(int year) {
        this.year = year;
    }

    @Override
    public Calendar getFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(this.year-1, Calendar.DECEMBER, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(this.year, Calendar.FEBRUARY, 28, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }

}
