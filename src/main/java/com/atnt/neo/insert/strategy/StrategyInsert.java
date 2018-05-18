package com.atnt.neo.insert.strategy;

import java.util.Calendar;
import java.util.Set;

public interface StrategyInsert {
    boolean shouldTruncateTableBeforeStart();

    /**
     * Which year to add rows to
     * This class is all about inserting data for a single year
     */
    int getYear();

    /**
     * The first day for which we want to insert data to.
     * Should be some day in the year as returned from {@link #getYear()}
     */
    Calendar getFirstDay();

    /**
     * The last day for which we want to insert data to.
     * Should be some day in the year as returned from {@link #getYear()}
     */
    Calendar getLastDay();

    /**
     * We iterate the  calendar and this is where  we decide if  we jump 1 day every iteration or two or a week...
     */
    void incrementCalendar(Calendar cal);

    /**
     * Generate device id
     */
    String getDeviceId(int year, int month, int day, int deviceIndex);

    /**
     * How many devices reporting in the given day
     */
    int getDeviceCountPerDay(Calendar cal);

    boolean isHourExist();

    /**
     * How many transaction per device in the given day.
     * Value should be between 1 and 24 (including both)
     * (the table holds one row per hour)
     */
    Set<Integer> getHoursArray();

    String getTableName();

    String getDeviceType(int year, int month, int day, int deviceIndex);

    int getBillingPoints(int month, int day, int hour);

    int getCounter(int month, int day, int hour);

    int getDataPoints(int month, int day, int hour);

    long getVolumeSize(int month, int day, int hour);

    Set<Integer> getMinutesArray();

    Set<Integer> getSecondsArray();
}
