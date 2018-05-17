package com.atnt.neo.insert.strategy;

import java.util.Calendar;

public abstract class AbsStrategyInsertAggregated extends AbsStrategyInsert {

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.DECEMBER, 31);
        return cal;
    }

    @Override
    public int getBillingPoints(int month, int day, int hour) {
        return 10;
    }

    @Override
    public int getCounter(int month, int day, int hour) {
        return 20;
    }

    @Override
    public int getDataPoints(int month, int day, int hour) {
        return 30;
    }

    @Override
    public long getVolumeSize(int month, int day, int hour) {
        return 40;
    }


}