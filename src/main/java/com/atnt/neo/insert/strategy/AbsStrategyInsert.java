package com.atnt.neo.insert.strategy;

import java.util.Calendar;

public abstract class AbsStrategyInsert implements StrategyInsert {
    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return true;
    }

    @Override
    public Calendar getFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.JANUARY, 1, 1, 0);
        return cal;
    }

    @Override
    public void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return String.format("device_%4d_%2d_%2d_%d", year, month, day, deviceIndex);
    }

    @Override
    public String getDeviceType(int year, int month, int day, int deviceIndex) {
        return String.format("device_type_%2d", day % 10);
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
