package com.atnt.neo.insert.strategy;

import java.util.Calendar;
import java.util.Set;

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
    public int getDataPoints(int month, int day, int hour) {
        return 30;
    }

    @Override
    public long getVolumeSize(int month, int day, int hour) {
        return 40;
    }

    @Override
    public Set<Integer> getMinutesArray() {
        return StrategyUtil.generateNotApplicable();
    }

    @Override
    public Set<Integer> getSecondsArray() {
        return StrategyUtil.generateNotApplicable();
    }

    @Override
    public boolean includeTimeStamp() {
        return false;
    }

    @Override
    public boolean includeTxnId() {
        return false;
    }
}
