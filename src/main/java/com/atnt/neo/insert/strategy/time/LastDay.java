package com.atnt.neo.insert.strategy.time;

import java.util.Calendar;

public class LastDay implements TimePeriod {
    private final Calendar now = Calendar.getInstance();
    @Override
    public Calendar getFirstDay() {
        final Calendar cal = (Calendar) now.clone();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal;
    }

    @Override
    public Calendar getLastDay() {
        return now;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }
}
