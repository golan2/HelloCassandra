package com.atnt.neo.insert.strategy.time;

import java.util.Calendar;

public class EveryDayForPeriod implements TimePeriod {
    private final Calendar firstDay;
    private final Calendar lastDay;

    public EveryDayForPeriod(Calendar firstDay, Calendar lastDay) {
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

    @Override
    public Calendar getFirstDay() {
        return firstDay;
    }

    @Override
    public Calendar getLastDay() {
        return lastDay;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }

}
