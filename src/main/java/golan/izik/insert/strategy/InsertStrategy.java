package golan.izik.insert.strategy;

import java.util.Calendar;

public interface InsertStrategy {
    boolean shouldTruncateTableBeforeStart();

    /**
     * Which year to add rows to
     * This class is all about inserting data for a single year
     */
    int getYear();

    /**
     * The last day for which we want to insert data to.
     * Should be some day in the year as returned from {@link #getYear()}
     */
    Calendar getLastDay();

    /**
     * The first day for which we want to insert data to.
     * Should be some day in the year as returned from {@link #getYear()}
     */
    Calendar getFirstDay();

    /**
     * We iterate the  calendar and this is where  we decide if  we jump 1 day every iteration or two or a week...
     */
    void incrementCalendar(Calendar cal);

    /**
     * Generate device id
     */
    String getDeviceId(int month, int day, int deviceIndex);

    /**
     * How many devices reporting in the given day
     */
    int getDeviceCountPerDay(Calendar cal);

    /**
     * How many transaction per device in the given day.
     * Value should be between 1 and 24 (including both)
     * (the table holds one row per hour)
     */
    int getDailyRowsCountPerDevice();

    /**
     * If we are in daily aggregation then there is no hour but for the hourly aggregation the returned value is true
     */
    boolean isHourExist();

    /**
     * The name of the table to which we insert data
     */
    String getTableName();
}
