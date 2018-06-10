package com.atnt.neo.insert.strategy.time;

import java.util.Calendar;

public class EveryWeekSeveralMonthsBeginOfYear implements TimePeriod {
    private final int year;
    private final int months;

    public EveryWeekSeveralMonthsBeginOfYear(int year, int howManyMonths) {
        this.year = year;
        this.months = howManyMonths;
    }

    @Override
    public Calendar getFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(this.year, Calendar.JANUARY, 1, 1, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    @Override
    public Calendar getLastDay() {
        final Calendar cal = getFirstDay();
        cal.add(Calendar.MONTH, this.months-1);
        return cal;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.WEEK_OF_YEAR, 1);
    }

}
