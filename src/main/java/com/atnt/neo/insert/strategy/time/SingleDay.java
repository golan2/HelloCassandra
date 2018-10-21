package com.atnt.neo.insert.strategy.time;

import java.util.Calendar;

public class SingleDay implements TimePeriod {
    private final int year;
    private final int month;
    private final int day;

    public SingleDay(int year) {
        this(year, Calendar.DECEMBER, 1);
    }

    private SingleDay(int year, int month, int day) {
        this.year = year;
        this.month = month-1;
        this.day = day;
    }

    @Override
    public Calendar getFirstDay() {
        return getLastDay();
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(this.year, month, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }

}
